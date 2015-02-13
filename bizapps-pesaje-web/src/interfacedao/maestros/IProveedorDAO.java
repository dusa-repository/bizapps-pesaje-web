package interfacedao.maestros;


import modelo.maestros.Proveedor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IProveedorDAO extends JpaRepository<Proveedor, String>{

}
