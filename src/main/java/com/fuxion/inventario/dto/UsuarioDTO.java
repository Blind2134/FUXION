package com.fuxion.inventario.dto;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UsuarioDTO {
    private Long idUsuario;
    private String nombre;
    private String email;
    private String rol; // "ALMACENERO", "SOCIO_DUENO"
    private String token; // Para JWT más adelante
}