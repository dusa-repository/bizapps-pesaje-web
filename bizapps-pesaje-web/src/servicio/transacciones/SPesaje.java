package servicio.transacciones;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import interfacedao.transacciones.IPesajeDAO;

import modelo.transacciones.Pesaje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SPesaje")
public class SPesaje {

	@Autowired
	private IPesajeDAO pesajeDAO;

	public void guardar(Pesaje pesaje) {
		pesajeDAO.save(pesaje);
		
	}

	public List<Pesaje> buscarPorEstatus(String estatus) {
		return pesajeDAO.findByEstatus(estatus);
	}

	public Pesaje buscar(long boleto) {

		return pesajeDAO.findOne(boleto);
	}

	public Pesaje buscarUltimo() {
		long id = pesajeDAO.findMaxPesaje();
		if (id != 0)
			return pesajeDAO.findOne(id);
		return null;
	}


	public List<Pesaje> buscarEntreFechasYParametrosDevoluciones(Date desde,
			Date hasta, String boleto, String vehiculo, String conductor,
			String producto, String balanza) {
		return pesajeDAO.buscarEntreFechasyPesajesDevoluciones(desde,hasta,boleto,vehiculo,conductor,producto,balanza);
	}

	public List<Pesaje> buscarEntreFechasYParametros(Date desde, Date hasta,
			String boleto, String vehiculo, String conductor, String producto,
			String balanza, String estatus) {
		return pesajeDAO.buscarEntreFechasyPesajes(desde,hasta,boleto,vehiculo,conductor,producto,balanza,estatus);
	}


}
