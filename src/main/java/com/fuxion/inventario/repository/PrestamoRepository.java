package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.Prestamo;
import com.fuxion.inventario.model.entity.Usuario;
import com.fuxion.inventario.model.enums.EstadoPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // "Muéstrame quién me debe productos" (Jeampier ve quién le debe)
    List<Prestamo> findBySocioAcreedorAndEstado(Usuario acreedor, EstadoPrestamo estado);

    // "Muéstrame a quién le debo" (Jeampier ve sus deudas)
    List<Prestamo> findBySocioDeudorAndEstado(Usuario deudor, EstadoPrestamo estado);

    List<Prestamo> findByAlmacenIdAlmacenAndEstado(Long idAlmacen, EstadoPrestamo estado);
}