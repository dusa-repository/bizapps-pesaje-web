package servicio.maestros;

import interfacedao.maestros.IBalanzaDAO;

import java.util.List;

import modelo.maestros.Balanza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SBalanza")
public class SBalanza {
	
	@Autowired
	private IBalanzaDAO balanzaDAO;

	public void guardar(Balanza balanza) {
		balanzaDAO.save(balanza);
		
	}

	public List<Balanza> buscarTodos() {
		return balanzaDAO.findAll();
	}

	public void eliminarUno(Long id) {
		balanzaDAO.delete(id);
	}

	public void eliminarVarios(List<Balanza> eliminarLista) {
		balanzaDAO.delete(eliminarLista);
	}

	public Balanza buscar(long idBalanza) {
		// TODO Auto-generated method stub
		return balanzaDAO.findOne(idBalanza);
	}

}
