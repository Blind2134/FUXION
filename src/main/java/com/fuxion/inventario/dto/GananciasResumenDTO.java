package com.fuxion.inventario.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class GananciasResumenDTO {
    private BigDecimal comisionTotal;      // Total acumulado
    private BigDecimal comisionMes;        // Del mes actual
    private Integer pedidosEntregados;     // Cantidad de pedidos
    private Integer pedidosMes;            // Pedidos del mes
    private BigDecimal montoVentasTotal;   // Suma de todas las ventas
}