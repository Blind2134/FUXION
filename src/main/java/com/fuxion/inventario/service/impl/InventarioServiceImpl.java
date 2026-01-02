package com.fuxion.inventario.service.impl;

import com.fuxion.inventario.dto.EntradaStockRequest;
import com.fuxion.inventario.dto.InventarioDTO;
import com.fuxion.inventario.dto.MovimientoStockDTO;
import com.fuxion.inventario.model.entity.*;
import com.fuxion.inventario.model.enums.TipoMovimiento;
import com.fuxion.inventario.repository.*;
import com.fuxion.inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final MovimientoStockRepository movimientoStockRepository;
    private final AlmacenRepository almacenRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public void registrarEntrada(EntradaStockRequest request) {
        // 1. Validar entidades
        Almacen almacen = almacenRepository.findById(request.getIdAlmacen())
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
        Usuario dueno = usuarioRepository.findById(request.getIdDueno())
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado"));
        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // 2. Calcular cantidad en sticks
        int sticksPorCaja = producto.getSticksPorCaja() != null ? producto.getSticksPorCaja() : 28;
        int totalSticks = request.getCantidadCajas() * sticksPorCaja;

        // 3. Aumentar stock
        aumentarStock(almacen, dueno, producto, totalSticks);

        // 4. Registrar movimiento
        MovimientoStock movimiento = MovimientoStock.builder()
                .fecha(LocalDateTime.now())
                .almacen(almacen)
                .producto(producto)
                .dueno(dueno)
                .tipo(TipoMovimiento.ENTRADA)
                .cantidad(totalSticks)
                .referenciaTipo("ENTRADA_MANUAL")
                .observacion(request.getObservacion())
                .build();
        movimientoStockRepository.save(movimiento);
    }

    @Override
    @Transactional
    public void descontarStockEstricto(Almacen almacen, Usuario vendedor, Producto producto, Integer cantidad) {
        Inventario inv = inventarioRepository
                .findByAlmacenAndDuenoAndProducto(almacen, vendedor, producto)
                .orElseThrow(() -> new RuntimeException("No hay stock de " + producto.getNombre()));

        if (inv.getCantidadSticks() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + inv.getCantidadSticks());
        }

        inv.setCantidadSticks(inv.getCantidadSticks() - cantidad);
        inventarioRepository.save(inv);
    }

    @Override
    @Transactional
    public void aumentarStock(Almacen almacen, Usuario dueno, Producto producto, Integer cantidad) {
        Inventario inv = inventarioRepository
                .findByAlmacenAndDuenoAndProducto(almacen, dueno, producto)
                .orElse(Inventario.builder()
                        .almacen(almacen)
                        .dueno(dueno)
                        .producto(producto)
                        .cantidadSticks(0)
                        .build());

        inv.setCantidadSticks(inv.getCantidadSticks() + cantidad);
        inventarioRepository.save(inv);
    }

    @Override
    public Integer consultarStockActual(Almacen almacen, Usuario dueno, Producto producto) {
        return inventarioRepository
                .findByAlmacenAndDuenoAndProducto(almacen, dueno, producto)
                .map(Inventario::getCantidadSticks)
                .orElse(0);
    }

    @Override
    public List<InventarioDTO> listarTodoStock(Long idAlmacen) {
        Almacen almacen = almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

        return inventarioRepository.findByAlmacen(almacen).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoStockDTO> listarMovimientos(Long idAlmacen) {
        Almacen almacen = almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

        return movimientoStockRepository.findByAlmacenOrderByFechaDesc(almacen).stream()
                .map(m -> MovimientoStockDTO.builder()
                        .idMovimiento(m.getIdMovimiento())
                        .fecha(m.getFecha())
                        .nombreProducto(m.getProducto().getNombre())
                        .tipo(m.getTipo().name())
                        .cantidad(m.getCantidad())
                        .observacion(m.getObservacion())
                        .referenciaTipo(m.getReferenciaTipo())
                        .referenciaId(m.getReferenciaId())
                        .build())
                .collect(Collectors.toList());
    }

    private InventarioDTO mapToDTO(Inventario inv) {
        int total = inv.getCantidadSticks();
        int porCaja = inv.getProducto().getSticksPorCaja() != null ?
                inv.getProducto().getSticksPorCaja() : 28;
        int cajas = total / porCaja;
        int sueltos = total % porCaja;
        String visual = cajas > 0 ? cajas + " Cajas + " + sueltos + " Sticks" : sueltos + " Sticks";

        return InventarioDTO.builder()
                .idInventario(inv.getIdInventario())
                .nombreProducto(inv.getProducto().getNombre())
                .nombreDueno(inv.getDueno().getNombre())
                .nombreAlmacen(inv.getAlmacen().getNombre())
                .cantidadSticks(inv.getCantidadSticks())
                .desgloseVisual(visual)
                .build();
    }
}