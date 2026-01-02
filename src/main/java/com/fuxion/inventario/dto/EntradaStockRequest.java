package com.fuxion.inventario.dto;

import lombok.Data;

@Data
public class EntradaStockRequest {
    private Long idAlmacen;      // Tu almacén (siempre 1 por ahora)
    private Long idDueno;        // Tú como almacenero (siempre 1 por ahora)
    private Long idProducto;     // Qué producto recibiste
    private Integer cantidadCajas; // Cuántas cajas llegaron
    private String observacion;  // Opcional: "Llegó de Lima"
}