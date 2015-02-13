package servicio.maestros;

import interfacedao.maestros.IProductoDAO;
import interfacedao.maestros.IProveedorDAO;

import java.util.List;

import modelo.maestros.Producto;
import modelo.maestros.Proveedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("SProducto")
public class SProducto {
	
	@Autowired
	private IProductoDAO productoDAO;

	public void guardar(Producto producto) {
		productoDAO.save(producto);
		
	}

	public List<Producto> buscarTodos() {
		return productoDAO.findAll();
	}

	public void eliminarUno(String id) {
		productoDAO.delete(id);
	}

	public void eliminarVarios(List<Producto> eliminarLista) {
		productoDAO.delete(eliminarLista);
	}

	public Producto buscar(String value) {

		return productoDAO.findOne(value);
	}

}
