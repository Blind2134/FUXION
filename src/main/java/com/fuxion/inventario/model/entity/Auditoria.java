package com.fuxion.inventario.model.entity;

import jakarta.persistence.*; // Si usas Spring Boot 2, cambia a javax.persistence
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLog;

    private LocalDateTime fecha;

    // Guardamos solo el ID del usuario para no sobrecargar las consultas de logs
    // y mantener el historial incluso si el usuario se borra fisicamente.
    @Column(name = "id_usuario")
    private Long idUsuario;

    private String accion; // Ej: "CREAR_PEDIDO", "MODIFICAR_STOCK"

    @Column(name = "tabla_afectada")
    private String tablaAfectada; // Ej: "pedidos", "inventario"

    @Column(name = "id_referencia")
    private Long idReferencia; // El ID del pedido o producto que se tocó

    @Column(name = "detalle_json", columnDefinition = "TEXT")
    private String detalleJson; // Guardamos el cambio en texto (JSON)

    // Se ejecuta automáticamente antes de guardar
    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }
}