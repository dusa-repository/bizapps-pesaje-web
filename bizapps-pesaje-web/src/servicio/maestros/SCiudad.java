package servicio.maestros;


import interfacedao.maestros.ICiudadDAO;

import java.util.List;

import modelo.maestros.Ciudad;
import modelo.maestros.Estado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SCiudad")
public class SCiudad {

	@Autowired
	private ICiudadDAO ciudadDAO;

	public List<Ciudad> buscarPorEstado(Estado estado) {
		return ciudadDAO.findByEstado(estado);
	}

	public void guardar(Ciudad ciudad) {
		ciudadDAO.save(ciudad);
	}

	public Ciudad buscar(long id) {
		return ciudadDAO.findOne(id);
	}

	public void eliminar(Ciudad ciudad) {
		ciudadDAO.delete(ciudad);
	}

	public List<Ciudad> buscarTodas() {
		return ciudadDAO.findAll();
	}

	public List<Ciudad> filtroNombre(String valor) {
		return ciudadDAO.findByNombreStartingWithAllIgnoreCase(valor);
	}

	public List<Ciudad> filtroEstado(String valor) {
		return ciudadDAO.findByEstadoNombreStartingWithAllIgnoreCase(valor);
	}

	public Ciudad buscarPorNombre(String value) {
		return ciudadDAO.findByNombre(value);
	}

	public List<Ciudad> filtroPais(String valor) {
		return ciudadDAO.findByEstadoPaisNombreStartingWithAllIgnoreCase(valor);
	}
}
