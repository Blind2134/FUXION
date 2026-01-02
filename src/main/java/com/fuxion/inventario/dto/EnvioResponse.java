package com.fuxion.inventario.dto;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class EnvioResponse {
    private Long idEnvio;
    private LocalDateTime fechaEnvio;
    private String nombreSocio;
    private String codigoRastreo;
    private String estado; // EN_TRANSITO
    private Integer totalItems; // Suma rápida
    private String observacion;
}