package com.fuxion.inventario.service.impl;
/*
import com.fuxion.inventario.model.entity.*;
import com.fuxion.inventario.model.enums.EstadoPrestamo;
import com.fuxion.inventario.repository.*;
import com.fuxion.inventario.service.InventarioService;
import com.fuxion.inventario.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final InventarioService inventarioService;

    // --- AGREGAMOS ESTOS REPOSITORIOS PARA PODER BUSCAR POR ID ---
    private final AlmacenRepository almacenRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public void solicitarPrestamo(Long idAlmacen, Long idDeudor, Long idAcreedor, Long idProducto, Integer cantidad) {

        // 1. BUSCAR LAS ENTIDADES COMPLETAS (Aquí estaba el error, faltaba esto)
        Almacen almacen = almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado: " + idAlmacen));

        Usuario deudor = usuarioRepository.findById(idDeudor)
                .orElseThrow(() -> new RuntimeException("Socio deudor no encontrado: " + idDeudor));

        Usuario acreedor = usuarioRepository.findById(idAcreedor)
                .orElseThrow(() -> new RuntimeException("Socio acreedor no encontrado: " + idAcreedor));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + idProducto));

        // 2. CREAR EL PRÉSTAMO CON LOS OBJETOS ENCONTRADOS
        Prestamo prestamo = Prestamo.builder()
                .fecha(LocalDateTime.now())
                .almacen(almacen)        // Ahora sí pasamos el objeto, no null
                .socioDeudor(deudor)     // Pasamos el objeto usuario
                .socioAcreedor(acreedor) // Pasamos el objeto usuario
                .producto(producto)      // Pasamos el objeto producto
                .cantidad(cantidad)
                .estado(EstadoPrestamo.PENDIENTE)
                .build();

        prestamoRepository.save(prestamo);
    }

    @Override
    @Transactional
    public void aprobarPrestamo(Long idPrestamo) {
        Prestamo p = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RuntimeException("Solicitud no existe"));

        if(p.getEstado() != EstadoPrestamo.PENDIENTE) throw new RuntimeException("Ya fue procesada");

        // 1. Descontar stock al Acreedor (Betzaida)
        inventarioService.descontarStockEstricto(p.getAlmacen(), p.getSocioAcreedor(), p.getProducto(), p.getCantidad());

        // 2. Aumentar stock al Deudor (Jeampier)
        inventarioService.aumentarStock(p.getAlmacen(), p.getSocioDeudor(), p.getProducto(), p.getCantidad());

        // 3. Actualizar estado
        p.setEstado(EstadoPrestamo.APROBADO);
        prestamoRepository.save(p);
    }

    @Override
    public List<Prestamo> listarPendientes(Long idAlmacen) {
        // Asegúrate de tener este método en PrestamoRepository: findByAlmacenIdAlmacenAndEstado
        // Si no lo tienes, créalo en la interfaz del repositorio.
        // return prestamoRepository.findByAlmacenIdAlmacenAndEstado(idAlmacen, EstadoPrestamo.PENDIENTE);
        return prestamoRepository.findByAlmacenIdAlmacenAndEstado(idAlmacen, EstadoPrestamo.PENDIENTE);    }
}


 */