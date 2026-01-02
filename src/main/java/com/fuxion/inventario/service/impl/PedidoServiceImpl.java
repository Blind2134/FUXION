package com.fuxion.inventario.service.impl;

/*
import com.fuxion.inventario.dto.PedidoRequest;
import com.fuxion.inventario.dto.PedidoResponse;
import com.fuxion.inventario.model.entity.*;
import com.fuxion.inventario.model.enums.EstadoPedido;
import com.fuxion.inventario.model.enums.TipoMovimiento;
import com.fuxion.inventario.repository.*;
import com.fuxion.inventario.service.InventarioService;
import com.fuxion.inventario.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final MovimientoStockRepository movimientoStockRepository;
    private final InventarioService inventarioService;

    @Override
    @Transactional
    public PedidoResponse registrarPedido(PedidoRequest request) {
        // 1. Validar datos
        Almacen almacen = almacenRepository.findById(request.getIdAlmacenOrigen())
                .orElseThrow(() -> new RuntimeException("Almacén no existe"));
        Usuario vendedor = usuarioRepository.findById(request.getIdVendedor())
                .orElseThrow(() -> new RuntimeException("Vendedor no existe"));

        // 2. Crear pedido
        Pedido pedido = Pedido.builder()
                .codigoPedido("PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .fechaCreacion(LocalDateTime.now())
                .almacenOrigen(almacen)
                .vendedor(vendedor)
                .clienteNombre(request.getClienteNombre())
                .clienteTelefono(request.getClienteTelefono())
                .clienteDireccion(request.getClienteDireccion())
                .ubicacionMaps(request.getUbicacionMaps())
                .aplicativoDelivery(request.getAplicativoDelivery())
                .nombreMotorizado(request.getNombreMotorizado())
                .estado(EstadoPedido.PENDIENTE)
                .build();

        pedido = pedidoRepository.save(pedido);

        BigDecimal montoTotal = BigDecimal.ZERO;

        // 3. Procesar productos y DESCONTAR STOCK
        for (PedidoRequest.DetallePedidoRequest item : request.getDetalles()) {
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Descontar stock
            inventarioService.descontarStockEstricto(almacen, vendedor, producto, item.getCantidad());

            // Registrar movimiento
            MovimientoStock movimiento = MovimientoStock.builder()
                    .fecha(LocalDateTime.now())
                    .almacen(almacen)
                    .producto(producto)
                    .dueno(vendedor)
                    .tipo(TipoMovimiento.SALIDA)
                    .cantidad(item.getCantidad())
                    .referenciaTipo("PEDIDO")
                    .referenciaId(pedido.getIdPedido())
                    .build();
            movimientoStockRepository.save(movimiento);

            // Guardar detalle
            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(producto)
                    .duenoStock(vendedor)
                    .cantidad(item.getCantidad())
                    .build();
            detallePedidoRepository.save(detalle);

            // Calcular monto
            BigDecimal subtotal = producto.getPrecioReferencial()
                    .multiply(new BigDecimal(item.getCantidad()));
            montoTotal = montoTotal.add(subtotal);
        }

        // 4. Calcular comisión (3% con mínimo 1 y máximo 5)
        BigDecimal comision = montoTotal.multiply(new BigDecimal("0.03"));
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
            // Todos los pedidos del almacén
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

 */