package servicio.maestros;

import interfacedao.maestros.ITransporteDAO;

import java.util.List;

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

	public void eliminarUno(Long id) {
		transporteDAO.delete(id);
	}

	public void eliminarVarios(List<Transporte> eliminarLista) {
		transporteDAO.delete(eliminarLista);
	}

	public Transporte buscar(long idTransporte) {
		// TODO Auto-generated method stub
		return transporteDAO.findOne(idTransporte);
	}

}