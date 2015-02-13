package interfacedao.maestros;

import modelo.maestros.Almacen;
import modelo.maestros.Vehiculo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IVehiculoDAO  extends JpaRepository<Vehiculo, String>  {

}
