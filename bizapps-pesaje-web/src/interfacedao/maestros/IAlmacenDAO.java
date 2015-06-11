package interfacedao.maestros;

import modelo.maestros.Almacen;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IAlmacenDAO extends JpaRepository<Almacen, Long> {

}
