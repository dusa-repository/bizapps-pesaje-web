package servicio.transacciones;

import interfacedao.transacciones.IDevolucionDAO;
import modelo.transacciones.Devolucion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SDevolucion")
public class SDevolucion {
	
	@Autowired
	private IDevolucionDAO devolucionDAO;

	public void guardar(Devolucion devolucion) {
		devolucionDAO.save(devolucion);
		
	}

}
