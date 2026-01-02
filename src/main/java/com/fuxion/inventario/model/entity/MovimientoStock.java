package com.fuxion.inventario.model.entity;

import com.fuxion.inventario.model.enums.TipoMovimiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_stock")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MovimientoStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_dueno", nullable = false)
    private Usuario dueno;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo; // ENTRADA, SALIDA...

    private Integer cantidad;

    // Referencias para saber de dónde vino el movimiento
    private String referenciaTipo; // "PEDIDO", "ENVIO"
    private Long referenciaId;     // ID del Pedido #105

    private String observacion;
}