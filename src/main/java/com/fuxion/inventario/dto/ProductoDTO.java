package com.fuxion.inventario.dto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data @Builder
public class ProductoDTO {
    private Long idProducto;
    private String nombre;
    private String sku;
    private String imgUrl;
    private Integer sticksPorCaja;
    private BigDecimal precioReferencial;
}