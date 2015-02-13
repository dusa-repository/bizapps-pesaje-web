package servicio.maestros;

import interfacedao.maestros.IClienteDAO;

import java.util.List;

import modelo.maestros.Cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SCliente")
public class SCliente {
	
	@Autowired
	private IClienteDAO clienteDAO;

	public void guardar(Cliente cliente) {
		clienteDAO.save(cliente);
		
	}

	public List<Cliente> buscarTodos() {
		return clienteDAO.findAll();
	}

	public void eliminarUno(String id) {
		clienteDAO.delete(id);
	}

	public void eliminarVarios(List<Cliente> eliminarLista) {
		clienteDAO.delete(eliminarLista);
	}

	public Cliente buscar(String value) {
		// TODO Auto-generated method stub
		return clienteDAO.findOne(value);
	}

}
