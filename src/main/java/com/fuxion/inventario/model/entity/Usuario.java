package com.fuxion.inventario.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String nombre;
    private String email;
    private String password;
    private String telefono;
    private Boolean activo;
    private LocalDateTime fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;
}