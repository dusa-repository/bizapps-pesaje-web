package interfacedao.maestros;

import java.util.List;

import modelo.maestros.Producto;
import modelo.maestros.Proveedor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductoDAO extends JpaRepository<Producto, String>{

	List<Producto> findByProveedor(Proveedor proveedor);

	List<Producto> findByProveedorIn(List<Proveedor> eliminarLista);

}
