package com.fuxion.inventario.service.impl;

import com.fuxion.inventario.dto.GananciasResumenDTO;
import com.fuxion.inventario.dto.PedidoResponse;
import com.fuxion.inventario.model.entity.Almacen;
import com.fuxion.inventario.model.entity.Pedido;
import com.fuxion.inventario.model.enums.EstadoPedido;
import com.fuxion.inventario.repository.AlmacenRepository;
import com.fuxion.inventario.repository.PedidoRepository;
import com.fuxion.inventario.service.GananciasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GananciasServiceImpl implements GananciasService {

    private final PedidoRepository pedidoRepository;
    private final AlmacenRepository almacenRepository;

    @Override
    public GananciasResumenDTO obtenerResumen(Long idAlmacenero, Integer mes, Integer anio) {
        // Por ahora asumimos que el almacenero solo tiene 1 almacén (ID 1)
        Almacen almacen = almacenRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

        // Todos los pedidos entregados
        List<Pedido> pedidosEntregados = pedidoRepository
                .findByAlmacenOrigenAndEstado(almacen, EstadoPedido.ENTREGADO);

        BigDecimal comisionTotal = pedidosEntregados.stream()
                .map(Pedido::getComisionAlmacenero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal montoVentasTotal = pedidosEntregados.stream()
                .map(Pedido::getMontoProductos)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Filtrar del mes si se especifica
        BigDecimal comisionMes = BigDecimal.ZERO;
        Integer pedidosMes = 0;

        if (mes != null && anio != null) {
            LocalDateTime inicioMes = LocalDateTime.of(anio, mes, 1, 0, 0);
            LocalDateTime finMes = inicioMes.plusMonths(1);

            List<Pedido> pedidosDelMes = pedidosEntregados.stream()
                    .filter(p -> p.getFechaCreacion().isAfter(inicioMes)
                            && p.getFechaCreacion().isBefore(finMes))
                    .collect(Collectors.toList());

            comisionMes = pedidosDelMes.stream()
                    .map(Pedido::getComisionAlmacenero)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            pedidosMes = pedidosDelMes.size();
        }

        return GananciasResumenDTO.builder()
                .comisionTotal(comisionTotal)
                .comisionMes(comisionMes)
                .pedidosEntregados(pedidosEntregados.size())
                .pedidosMes(pedidosMes)
                .montoVentasTotal(montoVentasTotal)
                .build();
    }

    @Override
    public List<PedidoResponse> obtenerHistorial(Long idAlmacenero) {
        Almacen almacen = almacenRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

        return pedidoRepository.findByAlmacenOrigenAndEstado(almacen, EstadoPedido.ENTREGADO)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PedidoResponse mapToResponse(Pedido p) {
        return PedidoResponse.builder()
                .idPedido(p.getIdPedido())
                .codigoPedido(p.getCodigoPedido())
                .fechaCreacion(p.getFechaCreacion())
                .clienteNombre(p.getClienteNombre())
                .estado(p.getEstado().name())
                .montoTotal(p.getMontoProductos())
                .miComision(p.getComisionAlmacenero())
                .build();
    }
}