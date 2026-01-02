package com.fuxion.inventario.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventario", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_almacen", "id_dueno", "id_producto"})
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInventario;

    @ManyToOne
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;

    @ManyToOne
    @JoinColumn(name = "id_dueno", nullable = false)
    private Usuario dueno; // Jeampier o Betzaida

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad_sticks")
    private Integer cantidadSticks;
}