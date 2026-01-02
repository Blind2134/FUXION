package com.fuxion.inventario.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_envio")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DetalleEnvio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_envio")
    private Envio envio;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private Integer cantidadEnviada;
    private Integer cantidadRecibida;
}