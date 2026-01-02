package com.fuxion.inventario.model.entity;

import com.fuxion.inventario.model.enums.EstadoPedido;
import com.fuxion.inventario.model.enums.EstadoPrestamo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrestamo;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private Almacen almacen;

    @ManyToOne
    @JoinColumn(name = "id_socio_deudor")
    private Usuario socioDeudor;

    @ManyToOne
    @JoinColumn(name = "id_socio_acreedor")
    private Usuario socioAcreedor;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    private EstadoPrestamo estado;
}