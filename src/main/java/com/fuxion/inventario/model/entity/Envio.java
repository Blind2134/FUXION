package com.fuxion.inventario.model.entity;

import com.fuxion.inventario.model.enums.EstadoEnvio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "envios")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEnvio;

    private String codigoRastreo;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaRecepcion;

    @ManyToOne
    @JoinColumn(name = "id_socio_emisor")
    private Usuario socioEmisor;

    @ManyToOne
    @JoinColumn(name = "id_almacen_destino")
    private Almacen almacenDestino;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estado;

    private String observacion;

    @OneToMany(mappedBy = "envio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleEnvio> detalles;
}