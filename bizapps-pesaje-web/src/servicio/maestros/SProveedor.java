package servicio.maestros;

import interfacedao.maestros.IProveedorDAO;

import java.util.List;

import modelo.maestros.Proveedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("SProveedor")
public class SProveedor {
	
	@Autowired
	private IProveedorDAO proveedorDAO;

	public void guardar(Proveedor proveedor) {
		proveedorDAO.save(proveedor);
		
	}

	public List<Proveedor> buscarTodos() {
		return proveedorDAO.findAll();
	}

	public void eliminarUno(String id) {
		proveedorDAO.delete(id);
	}

	public void eliminarVarios(List<Proveedor> eliminarLista) {
		proveedorDAO.delete(eliminarLista);
	}

}
