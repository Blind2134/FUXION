package com.fuxion.inventario.dto;


import lombok.Builder;
import lombok.Data;

@Data @Builder
public class InventarioDTO {
    private Long idInventario;
    private String nombreProducto;
    private String nombreDueno; // "Jeampier"
    private String nombreAlmacen; // "Hub Arequipa"
    private Integer cantidadSticks;

    // Campo calculado útil para el front:
    // Ejemplo: "1 Caja + 20 Sueltos"
    private String desgloseVisual;
}