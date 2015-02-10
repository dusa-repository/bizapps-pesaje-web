package servicio.maestros;

import java.util.List;

import interfacedao.maestros.IBalanzaDAO;
import interfacedao.maestros.ITransporteDAO;


import modelo.maestros.Transporte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("STransporte")
public class STransporte {
	
	@Autowired
	private ITransporteDAO transporteDAO;

	public void guardar(Transporte trans) {
		transporteDAO.save(trans);
		
	}

	public List<Transporte> buscarTodos() {
		return transporteDAO.findAll();
	}

	public void eliminarUno(int id) {
		transporteDAO.delete(id);
	}

	public void eliminarVarios(List<Transporte> eliminarLista) {
		transporteDAO.delete(eliminarLista);
	}

}