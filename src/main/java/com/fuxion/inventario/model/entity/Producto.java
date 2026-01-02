package com.fuxion.inventario.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    private String nombre;
    private String sku;
    private String imgUrl;

    @Column(name = "sticks_por_caja")
    private Integer sticksPorCaja;

    @Column(name = "precio_referencial")
    private BigDecimal precioReferencial;
}