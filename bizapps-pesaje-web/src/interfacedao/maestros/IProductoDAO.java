package interfacedao.maestros;

import modelo.maestros.Producto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductoDAO extends JpaRepository<Producto, String>{

}
