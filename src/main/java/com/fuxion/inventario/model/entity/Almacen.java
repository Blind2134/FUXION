package com.fuxion.inventario.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "almacenes")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Almacen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlmacen;

    private String nombre;
    private String direccion;
    private String ciudad;

    @ManyToOne
    @JoinColumn(name = "id_encargado")
    private Usuario encargado; // Tú (Bryan)
}