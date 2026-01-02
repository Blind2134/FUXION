package com.fuxion.inventario.service.impl;

import com.fuxion.inventario.dto.PedidoRequest;
import com.fuxion.inventario.dto.PedidoResponse;
import com.fuxion.inventario.model.entity.*;
import com.fuxion.inventario.model.enums.EstadoPedido;
import com.fuxion.inventario.repository.*;
import com.fuxion.inventario.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlmacenRepository almacenRepository;
    private final ProductoRepository productoRepository;
    // Necesitamos el repositorio directo para buscar stock y hacer préstamos
    private final InventarioRepository inventarioRepository;

    @Override
    @Transactional
    public PedidoResponse registrarPedido(PedidoRequest request) {
        // 1. Validar datos básicos
        Almacen almacen = almacenRepository.findById(request.getIdAlmacenOrigen())
                .orElseThrow(() -> new RuntimeException("Almacén no existe"));
        Usuario vendedor = usuarioRepository.findById(request.getIdVendedor())
                .orElseThrow(() -> new RuntimeException("Vendedor no existe"));

        // 2. Crear la Cabecera del Pedido
        Pedido pedido = Pedido.builder()
                .codigoPedido("PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .fechaCreacion(LocalDateTime.now())
                .almacenOrigen(almacen)
                .vendedor(vendedor)
                .clienteNombre(request.getClienteNombre())
                .clienteTelefono(request.getClienteTelefono())
                .clienteDireccion(request.getClienteDireccion())
                .ubicacionMaps(request.getUbicacionMaps()) // Usado para Tipo Entrega (Delivery/Recojo)
                .aplicativoDelivery(request.getAplicativoDelivery())
                .nombreMotorizado(request.getNombreMotorizado())
                .estado(EstadoPedido.PENDIENTE)
                .montoProductos(BigDecimal.ZERO)
                .comisionAlmacenero(BigDecimal.ZERO)
                .build();

        pedido = pedidoRepository.save(pedido);

        BigDecimal montoTotal = BigDecimal.ZERO;

        // 3. Procesar Productos: CALCULAR STICKS Y DESCONTAR
        for (PedidoRequest.DetallePedidoRequest item : request.getDetalles()) {
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // --- PASO CRÍTICO: CONVERSIÓN DE CAJAS A STICKS ---
            // Si piden 1 Caja de Prunex, son 28 sticks.
            int factorConversion = producto.getSticksPorCaja() != null ? producto.getSticksPorCaja() : 28;
            int cantidadSticksReales = item.getCantidad() * factorConversion;
            // --------------------------------------------------

            // --- LÓGICA DE STOCK INTELIGENTE ---

            // A) Intentar descontar stock del Vendedor (Jeampier)
            Inventario invVendedor = inventarioRepository.findByAlmacenAndDuenoAndProducto(
                    almacen, vendedor, producto
            ).orElse(null);

            Usuario duenoStockReal = vendedor;

            if (invVendedor != null && invVendedor.getCantidadSticks() >= cantidadSticksReales) {
                // ¡TIENE STOCK! Descontamos los sticks
                invVendedor.setCantidadSticks(invVendedor.getCantidadSticks() - cantidadSticksReales);
                inventarioRepository.save(invVendedor);
            } else {
                // B) NO TIENE STOCK -> BUSCAR PRÉSTAMO (Betzaida u otros)
                Inventario invPrestamo = inventarioRepository.findFirstByAlmacenAndProductoAndCantidadSticksGreaterThanEqual(
                        almacen, producto, cantidadSticksReales
                ).orElseThrow(() -> new RuntimeException("¡Stock Insuficiente! Nadie tiene " + cantidadSticksReales + " sticks de " + producto.getNombre()));

                // Descontamos al prestamista
                invPrestamo.setCantidadSticks(invPrestamo.getCantidadSticks() - cantidadSticksReales);
                inventarioRepository.save(invPrestamo);

                // Marcamos que el stock salió de otro socio
                duenoStockReal = invPrestamo.getDueno();
            }

            // --- GUARDAR DETALLE ---
            // Guardamos la cantidad en CAJAS (lo que pidió el cliente) para que el recibo se vea bien "1 Prunex"
            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(producto)
                    .duenoStock(duenoStockReal) // Registramos quién puso el producto (para deudas)
                    .cantidad(item.getCantidad()) // Guardamos 1 (Caja)
                    .build();
            detallePedidoRepository.save(detalle);

            // Calcular monto (Precio Caja * Cantidad Cajas)
            BigDecimal subtotal = producto.getPrecioReferencial()
                    .multiply(new BigDecimal(item.getCantidad()));
            montoTotal = montoTotal.add(subtotal);
        }

        // 4. Calcular comisión (3%)
        BigDecimal comision = montoTotal.multiply(new BigDecimal("0.03"));
        // Regla de negocio: Mínimo 1 sol, Máximo 5 soles (opcional, según tu lógica anterior)
        if (comision.compareTo(new BigDecimal("1.00")) < 0) comision = new BigDecimal("1.00");
        if (comision.compareTo(new BigDecimal("5.00")) > 0) comision = new BigDecimal("5.00");

        pedido.setMontoProductos(montoTotal);
        pedido.setComisionAlmacenero(comision);
        pedidoRepository.save(pedido);

        return mapToResponse(pedido);
    }

    @Override
    public void cambiarEstado(Long idPedido, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(EstadoPedido.valueOf(nuevoEstado));
        pedidoRepository.save(pedido);
    }

    @Override
    public List<PedidoResponse> listarPedidos(Long idAlmacen, String estado) {
        Almacen almacen = almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

        List<Pedido> pedidos;
        if (estado != null && !estado.isEmpty()) {
            pedidos = pedidoRepository.findByAlmacenOrigenAndEstado(
                    almacen, EstadoPedido.valueOf(estado));
        } else {
            pedidos = pedidoRepository.findByAlmacenOrigen(almacen);
        }

        return pedidos.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PedidoResponse obtenerPorId(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return mapToResponse(pedido);
    }

    private PedidoResponse mapToResponse(Pedido p) {
        return PedidoResponse.builder()
                .idPedido(p.getIdPedido())
                .codigoPedido(p.getCodigoPedido())
                .fechaCreacion(p.getFechaCreacion())
                .nombreVendedor(p.getVendedor().getNombre())
                .nombreAlmacen(p.getAlmacenOrigen().getNombre())
                .clienteNombre(p.getClienteNombre())
                .estado(p.getEstado().name())
                .montoTotal(p.getMontoProductos())
                .miComision(p.getComisionAlmacenero())
                .build();
    }
}