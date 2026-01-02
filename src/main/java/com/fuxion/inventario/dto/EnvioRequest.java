package com.fuxion.inventario.dto;
import lombok.Data;
import java.util.List;

@Data
public class EnvioRequest {
    private Long idSocioEmisor;
    private Long idAlmacenDestino;
    private String codigoRastreo; // Factura Shalom
    private String observacion;

    private List<ItemEnvio> items;

    @Data
    public static class ItemEnvio {
        private Long idProducto;
        private Integer cantidad;
    }
}