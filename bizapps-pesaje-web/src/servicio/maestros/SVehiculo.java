package servicio.maestros;


import interfacedao.maestros.IVehiculoDAO;

import java.util.List;


import modelo.maestros.Vehiculo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SVehiculo")
public class SVehiculo {
	
	@Autowired
	private IVehiculoDAO vehiculoDAO;

	public void guardar(Vehiculo vehiculo) {
		vehiculoDAO.save(vehiculo);
		
	}

	public List<Vehiculo> buscarTodos() {
		return vehiculoDAO.findAll();
	}

	public void eliminarUno(String id) {
		vehiculoDAO.delete(id);
	}

	public void eliminarVarios(List<Vehiculo> eliminarLista) {
		vehiculoDAO.delete(eliminarLista);
	}

	public Vehiculo buscar(String value) {
		// TODO Auto-generated method stub
		return vehiculoDAO.findOne(value);
	}


}
