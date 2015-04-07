package interfacedao.maestros;

import java.util.List;

import modelo.maestros.Estado;
import modelo.maestros.Pais;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadoDAO extends JpaRepository<Estado, Long> {

	List<Estado> findByNombreStartingWithAllIgnoreCase(String valor);

	Estado findByNombre(String value);

	List<Estado> findByPais(Pais pais);

	List<Estado> findByPaisNombreStartingWithAllIgnoreCase(String valor);

}
