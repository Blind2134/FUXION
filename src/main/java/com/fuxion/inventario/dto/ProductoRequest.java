package com.fuxion.inventario.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoRequest {
    private String nombre;
    private String sku;
    private String imgUrl;
    private Integer sticksPorCaja;
    private BigDecimal precioReferencial;
}