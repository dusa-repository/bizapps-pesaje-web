package controlador.maestros;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import modelo.transacciones.Pesaje;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Label;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;

public class CCerrados  extends CGenerico {
	@Wire
	private Div divPesaje;
	@Wire
	private Div divBotoneraPesaje;
	@Wire
	private Div divCatalogoPesajeCerrado;
	@Wire
	private Textbox txtBoleto;
	@Wire
	private Textbox txtAlmacen;
	@Wire
	private Label lblAlmacen;
	@Wire
	private Textbox txtVehiculo;
	@Wire
	private Label lblVehiculo;
	@Wire
	private Textbox txtTransporte;
	@Wire
	private Label lblTransporte;
	@Wire
	private Textbox txtConductor;
	@Wire
	private Label lblConductor;
	@Wire
	private Textbox txtProducto;
	@Wire
	private Label lblProducto;
	@Wire
	private Textbox txtBalanza;
	@Wire
	private Label lblBalanza;
	@Wire
	private Textbox txtNroFactura;
	@Wire
	private Textbox txtObservacion;
	@Wire
	private Datebox dtbFechaEntrada;
	@Wire
	private Datebox dtbFechaSalida;
	@Wire
	private Doublebox dbxPesoPTEntrada;
	@Wire
	private Doublebox dbxVehiculoEntrada;
	@Wire
	private Doublebox dbxDiferenciaEntrada;
	@Wire
	private Doublebox dbxTotalEntrada;
	@Wire
	private Doublebox dbxPesoPTSalida;
	@Wire
	private Doublebox dbxVehiculoSalida;
	@Wire
	private Doublebox dbxDiferenciaSalida;
	@Wire
	private Doublebox dbxTotalSalida;
	@Wire
	private Doublebox dbxTotal;
	@Wire
	private Doublespinner dbsPesoOrigen;
	@Wire
	private Spinner spnCajas;
	@Wire
	private Textbox txtDespachador;
	@Wire
	private Textbox txtProcedencia;
	@Wire
	private Textbox txtDestino;
	@Wire
	private Textbox txtNroPredespacho;

	Botonera botonera;

	Catalogo<Pesaje> catalogoPesajeCerrado;
	long idCerrado = 0;


	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				cerrar = (String) map.get("titulo");
				map.clear();
				map = null;
			}
		}

		botonera = new Botonera() {

			@Override
			public void seleccionar() {
			}

			@Override
			public void salir() {
				cerrarVentana(divPesaje, cerrar, tabs);

			}

			@Override
			public void reporte() {

					if (idCerrado != 0)
						Clients.evalJavaScript("window.open('"
								+ damePath()
								+ "Generador?valor=1&valor2="
								+ String.valueOf(idCerrado)
								+ "','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')");
					else
						msj.mensajeError("Debe Seleccionar un Pesaje");
				
			}

			@Override
			public void limpiar() {
				limpiarCampos();
			}

			@Override
			public void guardar() {
	
			}

			@Override
			public void eliminar() {

			}

			@Override
			public void buscar() {

			}

			@Override
			public void ayuda() {
				mostrarCatalogoPesajeCerrados();

			}

			@Override
			public void annadir() {

			}
		};

		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(3).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		divBotoneraPesaje.appendChild(botonera);
	}

	public void limpiarCampos() {
		idCerrado = 0;
		txtVehiculo.setValue("");
		txtBalanza.setValue("");
		txtBoleto.setValue("");
		txtConductor.setValue("");
		txtTransporte.setValue("");
		txtAlmacen.setValue("");
		txtProducto.setValue("");
		lblVehiculo.setValue("");
		lblBalanza.setValue("");
		lblConductor.setValue("");
		lblTransporte.setValue("");
		lblAlmacen.setValue("");
		lblProducto.setValue("");
		txtObservacion.setValue("");
		txtNroFactura.setValue("");
		txtNroPredespacho.setValue("");
		txtDespachador.setValue("");
		txtDestino.setValue("");
		txtProcedencia.setValue("");
		dbsPesoOrigen.setValue(0.0);
		spnCajas.setValue(0);
		limpiarCamposLectura();
	}

	public void mostrarCatalogoPesajeCerrados() {

		final List<Pesaje> lista = servicioPesaje.buscarPorEstatus("Cerrado");
		catalogoPesajeCerrado = new Catalogo<Pesaje>(divCatalogoPesajeCerrado,
				"Catalogo de Pesajes Cerrados", lista, true, false, false,
				"Boleto", "Fecha", "Producto", "Placa Vehiculo", "Conductor",
				"Estatus") {

			@Override
			protected List<Pesaje> buscar(List<String> valores) {

				List<Pesaje> listaa = new ArrayList<Pesaje>();

				for (Pesaje tipo : lista) {
					if (String.valueOf(tipo.getBoleto()).toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& String.valueOf(tipo.getFechaPesaje())
									.toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& String
									.valueOf(
											tipo.getProducto().getDescripcion())
									.toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& String.valueOf(tipo.getVehiculo().getPlaca())
									.toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& String.valueOf(tipo.getConductor().getNombres())
									.toLowerCase()
									.contains(valores.get(4).toLowerCase())
							&& String.valueOf(tipo.getEstatus()).toLowerCase()
									.contains(valores.get(5).toLowerCase())) {
						listaa.add(tipo);
					}
				}
				return listaa;
			}

			@Override
			protected String[] crearRegistros(Pesaje tipo) {
				String[] registros = new String[6];
				registros[0] = String.valueOf(tipo.getBoleto());
				registros[1] = String.valueOf(tipo.getFechaPesaje());
				registros[2] = String.valueOf(tipo.getProducto()
						.getDescripcion());
				registros[3] = String.valueOf(tipo.getVehiculo().getPlaca());
				registros[4] = String.valueOf(tipo.getConductor().getNombres()
						+ "  " + tipo.getConductor().getApellidos());
				registros[5] = String.valueOf(tipo.getEstatus());
				return registros;
			}
		};
		catalogoPesajeCerrado.setParent(divCatalogoPesajeCerrado);
		catalogoPesajeCerrado.doModal();
	}

	@Listen("onSeleccion = #divCatalogoPesajeCerrado")
	public void seleccionarCerrado() {
		limpiarCampos();
		Pesaje tipo = catalogoPesajeCerrado.objetoSeleccionadoDelCatalogo();
		idCerrado = tipo.getBoleto();
		llenarCamposPesaje(tipo);
		catalogoPesajeCerrado.setParent(null);
	}

	public void llenarCamposPesaje(Pesaje tipo) {

		txtBoleto.setValue(String.valueOf(tipo.getBoleto()));
		if (tipo.getAlmacen() != null) {
			txtAlmacen.setValue(String
					.valueOf(tipo.getAlmacen().getIdAlmacen()));
			lblAlmacen.setValue(tipo.getAlmacen().getDescripcion());
		}
		txtVehiculo.setValue(String.valueOf(tipo.getVehiculo().getPlaca()));
		lblVehiculo.setValue(tipo.getVehiculo().getDescripcion()+"   "+"Placa Chuto:"+"   "+tipo.getVehiculo().getPlacaChuto()+"   "+"Placa Batea:"+"   "+tipo.getVehiculo().getPlacaBatea());
		txtTransporte
				.setValue(String.valueOf(tipo.getTransporte().getCodigo()));
		lblTransporte.setValue(tipo.getTransporte().getDescripcion());
		txtConductor.setValue(String.valueOf(tipo.getConductor().getCedula()));
		lblConductor.setValue(tipo.getConductor().getNombres() + "  "
				+ tipo.getConductor().getApellidos());
		txtProducto
				.setValue(String.valueOf(tipo.getProducto().getIdProducto()));
		lblProducto.setValue(tipo.getProducto().getDescripcion());
		if (tipo.getBalanza() != null) {
			txtBalanza.setValue(String
					.valueOf(tipo.getBalanza().getIdBalanza()));
			lblBalanza.setValue(tipo.getBalanza().getDescripcion());

		}
		txtObservacion.setValue(tipo.getObservacion());
		txtNroFactura.setValue(tipo.getNroFactura());
		txtProcedencia.setValue(tipo.getProcedencia());
		txtDestino.setValue(tipo.getDestino());
		txtNroPredespacho.setValue(tipo.getNroPredespacho());
		txtDespachador.setValue(tipo.getDespachador());
		if(tipo.getPesoOrigen()!=null)
		dbsPesoOrigen.setValue(tipo.getPesoOrigen());
		if(tipo.getCantCajas()!=null)
			spnCajas.setValue(tipo.getCantCajas());
		dbxDiferenciaEntrada.setValue(0);
		dbxPesoPTEntrada.setValue(0);
		if (tipo.getEntrada() != null) {
			dbxVehiculoEntrada.setValue(tipo.getEntrada());
			dbxTotalEntrada.setValue(tipo.getEntrada());
			if (tipo.getSalida() == null)
				dbxTotal.setValue(tipo.getEntrada());
			dtbFechaEntrada.setDisabled(true);
			dtbFechaEntrada.setValue(tipo.getFechaPesaje());
		}
		if (tipo.getSalida() != null) {
			dbxVehiculoSalida.setValue(tipo.getSalida());
			dbxTotalSalida.setValue(tipo.getSalida());
			if (tipo.getEntrada() != null) {
				double entrada = tipo.getEntrada();
				Double total = (double) 0;
				if (entrada > tipo.getSalida())
					total = entrada - tipo.getSalida();
				else
					total = tipo.getSalida() - entrada;
				dbxTotal.setValue(total);
				Double diferencia = (double) 0;
				if (tipo.getPesoPTSalida() != null) {
					dbxPesoPTSalida.setValue(tipo.getPesoPTSalida());
					diferencia = tipo.getEntrada() + tipo.getPesoPTSalida()
							- tipo.getSalida();
					if (diferencia < 0)
						diferencia = diferencia * (-1);
					dbxDiferenciaSalida.setValue(diferencia);
				}
			}
			dtbFechaSalida.setDisabled(true);
			dtbFechaSalida.setValue(tipo.getFechaPesajeSalida());
		}
	}

	public void limpiarCamposLectura() {
		dbxDiferenciaEntrada.setValue(null);
		dbxDiferenciaSalida.setValue(null);
		dbxPesoPTEntrada.setValue(null);
		dbxPesoPTSalida.setValue(null);
		dbxTotal.setValue(null);
		dbxTotalEntrada.setValue(null);
		dbxTotalSalida.setValue(null);
		dbxVehiculoEntrada.setValue(null);
		dbxVehiculoSalida.setValue(null);
		dtbFechaEntrada.setValue(fechaHora);
		dtbFechaSalida.setValue(null);
	}

	public byte[] mostrarReporte(String a) throws JRException {
		byte[] fichero = null;

		Pesaje pesaje = getServicioPesaje().buscar(Long.parseLong(a));
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("boleto", pesaje.getBoleto());
		p.put("status", pesaje.getEstatus());
		p.put("vehiculo", pesaje.getVehiculo().getPlaca());
		
		p.put("placaBatea", pesaje.getVehiculo().getPlacaBatea());
		p.put("placaChuto", pesaje.getVehiculo().getPlacaChuto());
		p.put("destino", pesaje.getDestino());
		p.put("procedencia", pesaje.getProcedencia());
		p.put("despachador", pesaje.getDespachador());
		p.put("nroPredespacho", pesaje.getNroPredespacho());
		if(pesaje.getAlmacen()!=null)
		p.put("almacen", pesaje.getAlmacen().getDescripcion());
		
		if(pesaje.getPesoOrigen()!=null)
		p.put("pesoOrigen", String.valueOf(pesaje.getPesoOrigen()));
		if(pesaje.getCantCajas()!=null)
		p.put("cajas", String.valueOf(pesaje.getCantCajas()));
		
		p.put("vehiculo", pesaje.getVehiculo().getPlaca());
		p.put("transporte", pesaje.getTransporte().getDescripcion());
		p.put("producto", pesaje.getProducto().getIdProducto() + " , "
				+ pesaje.getProducto().getDescripcion());
		p.put("conductor", pesaje.getConductor().getCedula() + " , "
				+ pesaje.getConductor().getNombres() + " "
				+ pesaje.getConductor().getApellidos());
		p.put("status", pesaje.getEstatus());
		p.put("observacion", pesaje.getObservacion());

		URL url = getClass().getResource("/reporte/LogoDusa.png");
		BufferedImage image = null;
		try {
			image = ImageIO.read(url);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", baos);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			baos.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] imageBytes = baos.toByteArray();

		p.put("imagen", imageBytes);

		p.put("fechaEntrada", pesaje.getFechaPesaje().toString());
		if (pesaje.getFechaPesajeSalida() != null)
			p.put("fechaSalida", pesaje.getFechaPesajeSalida().toString());

		p.put("pesoEntrada", pesaje.getEntrada());
		if (pesaje.getSalida() != null)
			p.put("pesoSalida", pesaje.getSalida());

		if (pesaje.getSalida() == null)
			p.put("total", pesaje.getEntrada());
		else {
			Double diferencia = (double) 0;
			double entrada = pesaje.getEntrada();
			double salida = pesaje.getSalida();
			if (entrada > salida)
				diferencia = entrada - salida;
			else
				diferencia = salida - entrada;
			p.put("total", diferencia);
		}
		JasperReport reporte = (JasperReport) JRLoader.loadObject(getClass()
				.getResource("/reporte/RPesaje.jasper"));
		fichero = JasperRunManager.runReportToPdf(reporte, p);
		return fichero;
	}
}
