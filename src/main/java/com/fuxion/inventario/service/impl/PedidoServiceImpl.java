package com.fuxion.inventario.service.impl;

import com.fuxion.inventario.dto.*;
import com.fuxion.inventario.model.entity.*;
import com.fuxion.inventario.model.enums.EstadoPedido;
import com.fuxion.inventario.repository.*;
import com.fuxion.inventario.service.InventarioService;
import com.fuxion.inventario.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlmacenRepository almacenRepository;
    private final ProductoRepository productoRepository;
    private final InventarioService inventarioService; // Usamos el servicio que ya creamos

    @Override
    @Transactional
    public PedidoResponse registrarPedido(PedidoRequest request) {

        // 1. Validar Datos
        Almacen almacen = almacenRepository.findById(request.getIdAlmacenOrigen())
                .orElseThrow(() -> new RuntimeException("Almacén no existe"));
        Usuario vendedor = usuarioRepository.findById(request.getIdVendedor())
                .orElseThrow(() -> new RuntimeException("Vendedor no existe"));

        // 2. Crear Cabecera
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
        List<PedidoResponse.DetallePedidoResponse> detallesResp = new ArrayList<>();

        // 3. Procesar Productos
        for (PedidoRequest.DetallePedidoRequest item : request.getDetalles()) {
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // A. DESCONTAR STOCK
            inventarioService.descontarStockEstricto(almacen, vendedor, producto, item.getCantidad());

            // B. Guardar Detalle
            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(producto)
                    .duenoStock(vendedor)
                    .cantidad(item.getCantidad())
                    .build();
            detallePedidoRepository.save(detalle);

            // C. Sumar montos
            BigDecimal subtotal = producto.getPrecioReferencial()
                    .multiply(new BigDecimal(item.getCantidad()));
            montoTotal = montoTotal.add(subtotal);

            // D. Agregar al DTO de respuesta (AQUÍ FALTABA EL DUEÑO)
            detallesResp.add(PedidoResponse.DetallePedidoResponse.builder()
                    .nombreProducto(producto.getNombre())
                    .cantidad(item.getCantidad())
                    .duenoOriginalStock(vendedor.getNombre()) // <--- AGREGADO
                    .build());
        }

        // 4. CALCULAR COMISIÓN
        BigDecimal comision = calcularComision(montoTotal);

        pedido.setMontoProductos(montoTotal);
        pedido.setComisionAlmacenero(comision);
        pedidoRepository.save(pedido);

        // 5. Retornar DTO (AQUÍ FALTABAN LOS CAMPOS NULL)
        return PedidoResponse.builder()
                .idPedido(pedido.getIdPedido())
                .codigoPedido(pedido.getCodigoPedido())
                .fechaCreacion(pedido.getFechaCreacion()) // <--- AGREGADO
                .nombreVendedor(vendedor.getNombre())     // <--- AGREGADO
                .nombreAlmacen(almacen.getNombre())       // <--- AGREGADO
                .clienteNombre(pedido.getClienteNombre()) // <--- AGREGADO
                .estado(pedido.getEstado().name())
                .montoTotal(montoTotal)                   // <--- AGREGADO
                .miComision(comision)
                .detalles(detallesResp)
                .build();
    }

    @Override
    public void cambiarEstado(Long idPedido, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(EstadoPedido.valueOf(nuevoEstado));
        pedidoRepository.save(pedido);
    }

    private BigDecimal calcularComision(BigDecimal montoVenta) {
        BigDecimal tresPorCiento = montoVenta.multiply(new BigDecimal("0.03"));
        BigDecimal min = new BigDecimal("1.00");
        BigDecimal max = new BigDecimal("5.00");

        if (tresPorCiento.compareTo(min) < 0) return min;
        if (tresPorCiento.compareTo(max) > 0) return max;
        return tresPorCiento;
    }
}