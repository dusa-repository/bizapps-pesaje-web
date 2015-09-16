package interfacedao.maestros;


import java.util.List;

import modelo.maestros.Ciudad;
import modelo.maestros.Proveedor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IProveedorDAO extends JpaRepository<Proveedor, String>{

	Proveedor findByIdProveedor(String idProveedor);

	List<Proveedor> findByCiudad(Ciudad ciudad);

	List<Proveedor> findByCiudadIn(List<Ciudad> eliminarLista);

}
