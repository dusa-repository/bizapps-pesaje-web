package interfacedao.maestros;

import java.util.List;

import modelo.maestros.Ciudad;
import modelo.maestros.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteDAO extends JpaRepository<Cliente, String>{

	List<Cliente> findByCiudad(Ciudad ciudad);

	List<Cliente> findByCiudadIn(List<Ciudad> eliminarLista);

}
