package com.fuxion.inventario.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MovimientoStockDTO {
    private Long idMovimiento;
    private LocalDateTime fecha;
    private String nombreProducto;
    private String tipo; // "ENTRADA", "SALIDA"
    private Integer cantidad;
    private String observacion;
    private String referenciaTipo; // "PEDIDO", "ENTRADA_MANUAL"
    private Long referenciaId; // ID del pedido si aplica
}