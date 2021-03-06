package servicio.maestros;

import interfacedao.maestros.IAlmacenDAO;

import java.util.List;

import modelo.maestros.Almacen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SAlmacen")
public class SAlmacen {
	
	@Autowired
	private IAlmacenDAO almacenDAO;

	public void guardar(Almacen almacen) {
		almacenDAO.save(almacen);
	}

	public List<Almacen> buscarTodos() {
		return almacenDAO.findAll();
	}

	public void eliminarUno(Long id) {
		almacenDAO.delete(id);
	}

	public void eliminarVarios(List<Almacen> eliminarLista) {
		almacenDAO.delete(eliminarLista);
	}

	public Almacen buscar(long idAlmacen) {
	return	almacenDAO.findOne(idAlmacen);
	}

}