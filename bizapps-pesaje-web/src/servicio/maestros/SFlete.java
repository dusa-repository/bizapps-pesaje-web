package servicio.maestros;

import java.util.List;
import interfacedao.maestros.IFleteDAO;

import modelo.maestros.Flete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SFlete")
public class SFlete {
	
	@Autowired
	private IFleteDAO fleteDAO;

	public void guardar(Flete flete) {
		fleteDAO.save(flete);
		
	}

	public List<Flete> buscarTodos() {
		return fleteDAO.findAll();
	}

	public void eliminarUno(int id) {
		fleteDAO.delete(id);
	}

	public void eliminarVarios(List<Flete> eliminarLista) {
		fleteDAO.delete(eliminarLista);
	}

}