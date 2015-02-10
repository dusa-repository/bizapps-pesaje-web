package interfacedao.maestros;

import modelo.maestros.Almacen;
import modelo.maestros.Balanza;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IAlmacenDAO extends JpaRepository<Almacen, Integer> {

}
