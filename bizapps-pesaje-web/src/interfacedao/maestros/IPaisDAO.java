package interfacedao.maestros;

import java.util.List;

import modelo.maestros.Pais;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaisDAO extends JpaRepository<Pais, Long>{

	List<Pais> findByNombreStartingWithAllIgnoreCase(String valor);

	Pais findByNombre(String value);

}
