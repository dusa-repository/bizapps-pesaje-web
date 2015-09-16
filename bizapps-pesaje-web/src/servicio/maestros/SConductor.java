package servicio.maestros;

import interfacedao.maestros.IConductorDAO;

import java.util.List;

import modelo.maestros.Ciudad;
import modelo.maestros.Conductor;
import modelo.maestros.Transporte;

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

	public List<Conductor> buscarPorCiudad(Ciudad ciudad) {
		// TODO Auto-generated method stub
		return conductorDAO.findByCiudad(ciudad);
	}

	public List<Conductor> buscarPorCiudad(List<Ciudad> eliminarLista) {
		return conductorDAO.findByCiudadIn(eliminarLista);
	}

	public List<Conductor> buscarPorTransporte(List<Transporte> eliminarLista) {
		return conductorDAO.findByTransporteIn(eliminarLista);
	}

	public List<Conductor> buscarPorTransporte(Transporte transporte) {
		// TODO Auto-generated method stub
		return  conductorDAO.findByTransporte(transporte);
	}

}
