package com.fuxion.inventario.model.entity;

import com.fuxion.inventario.model.enums.EstadoLiquidacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "liquidaciones")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Liquidacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLiquidacion;

    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaPago;

    @ManyToOne
    @JoinColumn(name = "id_socio")
    private Usuario socio;

    @ManyToOne
    @JoinColumn(name = "id_almacenero")
    private Usuario almacenero;

    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    private EstadoLiquidacion estado;

    private String comprobanteUrl;
    private String observacion;
}