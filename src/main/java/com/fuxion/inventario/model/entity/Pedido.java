package com.fuxion.inventario.model.entity;

import com.fuxion.inventario.model.enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "pedidos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @Column(unique = true)
    private String codigoPedido; // PED-1001

    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private Almacen almacenOrigen;

    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    private Usuario vendedor; // Jeampier

    // Cliente Final
    private String clienteNombre;
    private String clienteTelefono;
    private String clienteDireccion;
    private String ubicacionMaps;

    // Logística
    private String aplicativoDelivery;
    private String nombreMotorizado;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    // Finanzas
    private BigDecimal montoProductos;
    private BigDecimal comisionAlmacenero; // Tu ganancia

    // Relación con Detalle (Cascade para guardar todo junto)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;
}