package com.fuxion.inventario.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class DetalleEnvioDTO {
    private Long idProducto;
    private String nombreProducto;
    private Integer cantidad; // Total en sticks (ej. 56)

    // CAMPO NUEVO 👇
    private Integer sticksPorCaja; // Factor de conversión (ej. 28)
}