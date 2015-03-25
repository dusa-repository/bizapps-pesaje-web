package interfacedao.transacciones;

import java.util.List;

import modelo.transacciones.Pesaje;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPesajeDAO extends JpaRepository<Pesaje, Long>{

	List<Pesaje> findByEstatus(String estatus);

	@Query("select coalesce(max(p.boleto), '0') from Pesaje p")
	long findMaxPesaje();

}
