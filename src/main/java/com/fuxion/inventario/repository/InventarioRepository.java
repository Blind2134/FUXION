package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.Almacen;
import com.fuxion.inventario.model.entity.Inventario;
import com.fuxion.inventario.model.entity.Producto;
import com.fuxion.inventario.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // 1. BUSCAR STOCK EXACTO (Para saber si se puede vender)
    // "Búscame si Jeampier tiene Prunex en el Almacén de Arequipa"
    Optional<Inventario> findByAlmacenAndDuenoAndProducto(Almacen almacen, Usuario dueno, Producto producto);

    // 2. VER MI STOCK (Para el Dashboard del Socio)
    // "Soy Jeampier, muéstrame todo lo que tengo en Arequipa"
    List<Inventario> findByAlmacenAndDueno(Almacen almacen, Usuario dueno);

    // 3. VER TODO EL STOCK DEL ALMACÉN (Para ti, el Almacenero)
    // "Muéstrame todo lo que hay en mi casa (sin importar el dueño)"
    List<Inventario> findByAlmacen(Almacen almacen);

    Optional<Inventario> findFirstByAlmacenAndProductoAndCantidadSticksGreaterThanEqual(
            Almacen almacen, Producto producto, Integer cantidad
    );

    List<Inventario> findByAlmacenAndProducto(Almacen almacen, Producto producto);
}
