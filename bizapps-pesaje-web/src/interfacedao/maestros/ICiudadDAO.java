package interfacedao.maestros;

import java.util.List;

import modelo.maestros.Ciudad;
import modelo.maestros.Estado;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICiudadDAO extends JpaRepository<Ciudad, Long> {

	List<Ciudad> findByEstado(Estado estado);

	List<Ciudad> findByNombreStartingWithAllIgnoreCase(String valor);

	List<Ciudad> findByEstadoNombreStartingWithAllIgnoreCase(String valor);

	Ciudad findByNombre(String value);

	List<Ciudad> findByEstadoPaisNombreStartingWithAllIgnoreCase(String valor);

}
