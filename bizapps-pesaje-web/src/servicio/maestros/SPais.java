package servicio.maestros;

import interfacedao.maestros.IPaisDAO;

import java.util.List;

import modelo.maestros.Pais;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SPais")
public class SPais {

	@Autowired
	private IPaisDAO paisDAO;

	public void guardar(Pais pais) {
		paisDAO.save(pais);
	}

	public Pais buscar(long id) {
		return paisDAO.findOne(id);
	}

	public void eliminar(Pais pais) {
		paisDAO.delete(pais);
	}

	public List<Pais> buscarTodos() {
		return paisDAO.findAll();
	}

	public List<Pais> filtroNombre(String valor) {
		return paisDAO.findByNombreStartingWithAllIgnoreCase(valor);
	}

	public Pais buscarPorNombre(String value) {
		return paisDAO.findByNombre(value);
	}
}
