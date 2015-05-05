package interfacedao.transacciones;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import modelo.transacciones.Pesaje;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPesajeDAO extends JpaRepository<Pesaje, Long> {

	List<Pesaje> findByEstatus(String estatus);

	@Query("select coalesce(max(p.boleto), '0') from Pesaje p")
	long findMaxPesaje();

	@Query(value = "select * from Pesaje p where p.fecha_pesaje between ?1 and ?2 and p.boleto like ?3 and p.placa_vehiculo like ?4 "
			+ "and p.cedula_conductor like ?5 and p.id_producto like ?6 "
			+ "and p.id_balanza like ?7 and p.estatus like ?8  " + "order by p.boleto asc", nativeQuery = true)
	List<Pesaje> buscarEntreFechasyPesajes(Date desde, Date hasta,
			String boleto, String vehiculo, String conductor, String producto,
			String balanza, String estatus);


	@Query(value = "select * from Pesaje p where p.fecha_pesaje between ?1 and ?2 and p.boleto like ?3 and p.placa_vehiculo like ?4 "
			+ "and p.cedula_conductor like ?5 and p.id_producto like ?6 "
			+ "and p.id_balanza like ?7 and p.boleto in (select d.boleto from Devolucion d) " + "order by p.boleto asc", nativeQuery = true)
	List<Pesaje> buscarEntreFechasyPesajesDevoluciones(Date desde, Date hasta,
			String boleto, String vehiculo, String conductor, String producto,
			String balanza);

}
