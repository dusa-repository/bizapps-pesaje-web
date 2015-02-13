package servicio.maestros;

import interfacedao.maestros.IAlmacenDAO;
import interfacedao.maestros.IConductorDAO;
import interfacedao.maestros.IVehiculoDAO;

import java.util.List;

import modelo.maestros.Almacen;
import modelo.maestros.Conductor;
import modelo.maestros.Vehiculo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SConductor")
public class SConductor {
	

	@Autowired
	private IConductorDAO conductorDAO;

	public void guardar(Conductor conductor) {
		conductorDAO.save(conductor);
		
	}

	public List<Conductor> buscarTodos() {
		return conductorDAO.findAll();
	}

	public void eliminarUno(String id) {
		conductorDAO.delete(id);
	}

	public void eliminarVarios(List<Conductor> eliminarLista) {
		conductorDAO.delete(eliminarLista);
	}

	public Conductor buscar(String value) {
		// TODO Auto-generated method stub
		return conductorDAO.findOne(value);
	}

}
