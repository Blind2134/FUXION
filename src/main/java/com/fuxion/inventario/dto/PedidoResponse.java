package com.fuxion.inventario.dto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class PedidoResponse {
    private Long idPedido;
    private String codigoPedido;
    private LocalDateTime fechaCreacion;

    private String nombreVendedor;
    private String nombreAlmacen;

    private String clienteNombre;
    private String estado; // PENDIENTE, EN_RUTA...

    private BigDecimal montoTotal;
    private BigDecimal miComision; // Solo visible si eres el Almacenero

    private List<DetallePedidoResponse> detalles;

    @Data @Builder
    public static class DetallePedidoResponse {
        private String nombreProducto;
        private Integer cantidad;
        private String duenoOriginalStock; // Para ver si hubo préstamo
    }
}