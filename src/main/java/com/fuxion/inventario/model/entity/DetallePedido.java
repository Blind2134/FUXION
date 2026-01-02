package com.fuxion.inventario.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "detalle_pedido")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido; // OJO: Usar @JsonIgnore si usas Jackson directamente para evitar loop infinito

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_dueno_stock", nullable = false)
    private Usuario duenoStock; // Clave para detectar préstamos

    private Integer cantidad;
}