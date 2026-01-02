package com.fuxion.inventario.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class LiquidacionRequest {
    private Long idSocioDeudor; // A quién le cobras
    private Long idAlmacenero;  // Tú
    private BigDecimal montoSolicitado;

    // Opcional: Lista de IDs de pedidos que estás cobrando
    // private List<Long> idsPedidosAsociados;
}