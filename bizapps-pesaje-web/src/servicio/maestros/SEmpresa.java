package servicio.maestros;

import interfacedao.maestros.IEmpresaDAO;

import java.util.List;

import modelo.maestros.Empresa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SEmpresa")
public class SEmpresa {
	
	
	@Autowired
	private IEmpresaDAO empresaDAO;


	public void guardar(Empresa empresa) {
		empresaDAO.save(empresa);
	}

	public List<Empresa> buscarTodos() {
		return empresaDAO.findAll();
	}

	public void eliminarUno(String id) {
		empresaDAO.delete(id);
	}

	public void eliminarVarios(List<Empresa> eliminarLista) {
		empresaDAO.delete(eliminarLista);
	}

	public Empresa buscar(String id) {
	return	empresaDAO.findOne(id);
	}
}
