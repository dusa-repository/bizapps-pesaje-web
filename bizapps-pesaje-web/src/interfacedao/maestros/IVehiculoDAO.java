package interfacedao.maestros;

import java.util.List;

import modelo.maestros.Vehiculo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IVehiculoDAO  extends JpaRepository<Vehiculo, String>  {

	@Query("select v from Vehiculo v where not exists (select p.vehiculo.placa from Pesaje p where p.estatus=?1 and p.vehiculo.placa=v.placa)")
	List<Vehiculo> pesajesCerrados(String cerrado);

}
