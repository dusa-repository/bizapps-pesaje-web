package servicio.transacciones;

import interfacedao.transacciones.IPesajeDAO;

import java.util.Date;
import java.util.List;

import modelo.maestros.Almacen;
import modelo.maestros.Balanza;
import modelo.maestros.Conductor;
import modelo.maestros.Producto;
import modelo.maestros.Transporte;
import modelo.maestros.Vehiculo;
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

	public List<Pesaje> buscarPorTransporte(List<Transporte> eliminarLista) {
		// TODO Auto-generated method stub
		return pesajeDAO.findByTransporteIn(eliminarLista);
	}

	public List<Pesaje> buscarPorTransporte(Transporte transporte) {
		// TODO Auto-generated method stub
		return pesajeDAO.findByTransporte(transporte);
	}

	public List<Pesaje> buscarPorVehiculo(List<Vehiculo> eliminarLista) {
		return pesajeDAO.findByVehiculoIn(eliminarLista);
	}

	public List<Pesaje> buscarPorVehiculo(Vehiculo vehiculo) {
		return pesajeDAO.findByVehiculo(vehiculo);
	}

	public List<Pesaje> buscarPorConductor(List<Conductor> eliminarLista) {
		return pesajeDAO.findByConductorIn(eliminarLista);
	}

	public List<Pesaje> buscarPorConductor(Conductor conductor) {
		return pesajeDAO.findByConductor(conductor);
	}

	public List<Pesaje> buscarPorProducto(List<Producto> eliminarLista) {
		return pesajeDAO.findByProductoIn(eliminarLista);
	}

	public List<Pesaje> buscarPorProducto(Producto producto) {
		return pesajeDAO.findByProducto(producto);
	}

	public List<Pesaje> buscarPorBalanza(List<Balanza> eliminarLista) {
		return pesajeDAO.findByBalanzaIn(eliminarLista);
	}

	public List<Pesaje> buscarPorBalanza(Balanza balanza) {
		return pesajeDAO.findByBalanza(balanza);
	}

	public List<Pesaje> buscarPorAlmacen(List<Almacen> eliminarLista) {
		return pesajeDAO.findByAlmacenIn(eliminarLista);
	}

	public List<Pesaje> buscarPorAlmacen(Almacen almacen) {
		return pesajeDAO.findByAlmacen(almacen);
	}


}
