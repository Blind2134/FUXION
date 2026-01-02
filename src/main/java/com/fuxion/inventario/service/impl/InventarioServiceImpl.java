package com.fuxion.inventario.service.impl;
import com.fuxion.inventario.dto.InventarioDTO; // <--- IMPORTANTE
import com.fuxion.inventario.exception.StockInsuficienteException;
import com.fuxion.inventario.model.entity.*;
import com.fuxion.inventario.repository.AlmacenRepository;
import com.fuxion.inventario.repository.InventarioRepository;
import com.fuxion.inventario.repository.UsuarioRepository;
import com.fuxion.inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final AlmacenRepository almacenRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void descontarStockEstricto(Almacen almacen, Usuario vendedor, Producto producto, Integer cantidad) {

        // 1. Buscamos el stock del VENDEDOR (Jeampier)
        Optional<Inventario> stockPropio = inventarioRepository
                .findByAlmacenAndDuenoAndProducto(almacen, vendedor, producto);

        // Verificamos si existe el registro y si tiene cantidad suficiente
        if (stockPropio.isPresent() && stockPropio.get().getCantidadSticks() >= cantidad) {
            // TIENE STOCK: Descontamos felices
            Inventario inv = stockPropio.get();
            inv.setCantidadSticks(inv.getCantidadSticks() - cantidad);
            inventarioRepository.save(inv);
        } else {
            // NO TIENE STOCK: Empieza la búsqueda de un prestamista
            buscarPrestamistaYLanzarError(almacen, producto, cantidad);
        }
    }

    private void buscarPrestamistaYLanzarError(Almacen almacen, Producto producto, Integer cantidadRequerida, Long idVendedorOriginal) {

        // A. Traemos TODO el inventario de ese producto en ese almacén
        List<Inventario> vecinos = inventarioRepository.findByAlmacenAndProducto(almacen, producto);

        // B. Recorremos para ver si alguien (que no sea yo) tiene stock
        for (Inventario vecino : vecinos) {

            // Verificamos que no sea el mismo vendedor (por si acaso aparezca en la lista con stock bajo)
            boolean esOtroSocio = !vecino.getDueno().getIdUsuario().equals(idVendedorOriginal);
            boolean tieneSuficiente = vecino.getCantidadSticks() >= cantidadRequerida;

            if (esOtroSocio && tieneSuficiente) {
                // ¡ENCONTRAMOS UN PRESTAMISTA! -> Lanzamos Error 409
                throw new StockInsuficienteException(
                        "Stock insuficiente. " + vecino.getDueno().getNombre() + " tiene unidades disponibles. ¿Solicitar préstamo?",
                        vecino.getDueno().getIdUsuario(), // ID del Acreedor (Betzaida)
                        vecino.getDueno().getNombre()     // Nombre para el modal
                );
            }
        }

        // C. SI LLEGAMOS AQUÍ, NADIE TIENE STOCK -> Lanzamos Error 400 (Mensaje Rojo)
        throw new RuntimeException("No hay stock disponible de " + producto.getNombre() + " en este almacén.");
    }

    private void buscarPrestamistaYLanzarError(Almacen almacen, Producto producto, Integer cantidad) {
        // Buscamos en la BD si ALGUIEN MÁS tiene stock suficiente de ese producto en ese almacén
        // El repositorio debe tener un método para buscar "Cualquiera con stock > X"
        Optional<Inventario> posiblePrestamista = inventarioRepository
                .findFirstByAlmacenAndProductoAndCantidadSticksGreaterThanEqual(
                        almacen, producto, cantidad
                );

        if (posiblePrestamista.isPresent()) {
            Usuario socioPrestamista = posiblePrestamista.get().getDueno();

            // ¡ENCONTRAMOS UNO! (Ej: Betzaida)
            // Lanzamos la excepción especial para avisar al Controller
            throw new StockInsuficienteException(
                    "Stock insuficiente. " + socioPrestamista.getNombre() + " tiene unidades disponibles. ¿Solicitar préstamo?",
                    socioPrestamista.getIdUsuario(),
                    socioPrestamista.getNombre()
            );
        } else {
            // NADIE TIENE STOCK
            throw new RuntimeException("No hay stock disponible de " + producto.getNombre() + " en este almacén.");
        }
    }

    @Override
    @Transactional
    public void aumentarStock(Almacen almacen, Usuario dueno, Producto producto, Integer cantidad) {
        // Buscamos si ya tiene inventario, si no, creamos la fila
        Inventario inventario = inventarioRepository
                .findByAlmacenAndDuenoAndProducto(almacen, dueno, producto)
                .orElse(Inventario.builder()
                        .almacen(almacen)
                        .dueno(dueno)
                        .producto(producto)
                        .cantidadSticks(0)
                        .build());

        inventario.setCantidadSticks(inventario.getCantidadSticks() + cantidad);
        inventarioRepository.save(inventario);
    }

    @Override
    public Integer consultarStockActual(Almacen almacen, Usuario dueno, Producto producto) {
        return inventarioRepository
                .findByAlmacenAndDuenoAndProducto(almacen, dueno, producto)
                .map(Inventario::getCantidadSticks)
                .orElse(0);
    }


    @Override
    public List<InventarioDTO> listarStockDueno(Long idAlmacen, Long idDueno) {
        // 1. Validar que existan
        Almacen almacen = almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
        Usuario dueno = usuarioRepository.findById(idDueno)
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado"));

        // 2. Buscar en BD
        List<Inventario> lista = inventarioRepository.findByAlmacenAndDueno(almacen, dueno);

        // 3. Convertir Entidad -> DTO
        return lista.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventarioDTO> listarTodoStock(Long idAlmacen) {
        Almacen almacen = almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

        List<Inventario> lista = inventarioRepository.findByAlmacen(almacen);

        return lista.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar privado para no repetir código de conversión
    private InventarioDTO mapToDTO(Inventario inv) {
        // Lógica visual: calcular cajas y sueltos
        int total = inv.getCantidadSticks();
        int porCaja = inv.getProducto().getSticksPorCaja() != null ? inv.getProducto().getSticksPorCaja() : 28;

        int cajas = total / porCaja;
        int sueltos = total % porCaja;

        String visual = cajas + " Cajas + " + sueltos + " Sticks";
        if (cajas == 0) visual = sueltos + " Sticks";

        return InventarioDTO.builder()
                .idInventario(inv.getIdInventario())
                .nombreProducto(inv.getProducto().getNombre())
                .nombreDueno(inv.getDueno().getNombre())
                .nombreAlmacen(inv.getAlmacen().getNombre())
                .cantidadSticks(inv.getCantidadSticks())
                .desgloseVisual(visual) // ¡Muy útil para tu Frontend!
                .build();
    }
}