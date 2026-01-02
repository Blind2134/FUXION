package com.fuxion.inventario.dto;
import lombok.Data;
import java.util.List;

@Data
public class PedidoRequest {
    private Long idAlmacenOrigen;
    private Long idVendedor; // Quién vende

    // Datos Cliente
    private String clienteNombre;
    private String clienteTelefono;
    private String clienteDireccion;
    private String ubicacionMaps;

    // Logística
    private String aplicativoDelivery; // "Indriver"
    private String nombreMotorizado;

    // El Carrito de compras
    private List<DetallePedidoRequest> detalles;

    @Data
    public static class DetallePedidoRequest {
        private Long idProducto;
        private Integer cantidad;
    }
}