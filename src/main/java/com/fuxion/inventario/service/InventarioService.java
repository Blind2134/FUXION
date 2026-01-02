package com.fuxion.inventario.service;

import com.fuxion.inventario.dto.InventarioDTO; // <--- IMPORTANTE
import com.fuxion.inventario.model.entity.Almacen;
import com.fuxion.inventario.model.entity.Producto;
import com.fuxion.inventario.model.entity.Usuario;

import java.util.List; // <--- IMPORTANTE

public interface InventarioService {

    void descontarStockEstricto(Almacen almacen, Usuario vendedor, Producto producto, Integer cantidad);

    void aumentarStock(Almacen almacen, Usuario dueno, Producto producto, Integer cantidad);

    Integer consultarStockActual(Almacen almacen, Usuario dueno, Producto producto);

    // --- AQUÍ ESTABA EL ERROR SEGURAMENTE ---
    // Debe decir List<InventarioDTO>, no List<Inventario>
    List<InventarioDTO> listarStockDueno(Long idAlmacen, Long idDueno);

    List<InventarioDTO> listarTodoStock(Long idAlmacen);
}