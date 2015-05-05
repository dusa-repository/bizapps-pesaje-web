package controlador.reporte;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import modelo.maestros.Almacen;
import modelo.maestros.Balanza;
import modelo.maestros.Conductor;
import modelo.maestros.Producto;
import modelo.maestros.Transporte;
import modelo.maestros.Vehiculo;
import modelo.transacciones.Pesaje;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Convertidor;
import componente.Mensaje;
import controlador.maestros.CGenerico;

public class CReportePesajes extends CGenerico {

	private static final long serialVersionUID = -6868106910332150746L;
	@Wire
	private Div divReporte;
	@Wire
	private Div divBotoneraPesaje;
	@Wire
	private Textbox txtBoleto;
	@Wire
	private Div divCatalogoVehiculo;
	@Wire
	private Textbox txtVehiculo;
	@Wire
	private Label lblVehiculo;
	@Wire
	private Div divCatalogoConductor;
	@Wire
	private Textbox txtConductor;
	@Wire
	private Label lblConductor;
	@Wire
	private Div divCatalogoProducto;
	@Wire
	private Textbox txtProducto;
	@Wire
	private Label lblProducto;
	@Wire
	private Div divCatalogoBalanza;
	@Wire
	private Textbox txtBalanza;
	@Wire
	private Label lblBalanza;
	@Wire
	private Datebox dtbDesde;
	@Wire
	private Datebox dtbHasta;
	@Wire
	private Combobox cmbTipo;
	@Wire
	private Combobox cmbMovimiento;

	Catalogo<Producto> catalogoProducto;
	Catalogo<Vehiculo> catalogoVehiculo;
	Catalogo<Conductor> catalogoConductor;
	Catalogo<Transporte> catalogoTransporte;
	Catalogo<Balanza> catalogoBalanza;
	String idVehiculo = "";
	String idConductor = "";
	String idProducto = "";
	long idBalanza = 0;

	Botonera botonera;
	long id = 0;

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
				cerrarVentana(divReporte, cerrar, tabs);
			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				limpiarCampos();
			}

			@Override
			public void guardar() {
				if (validar()) {

					Date desde = dtbDesde.getValue();
					Date hasta = agregarDia(dtbHasta.getValue());

					String movimiento = "";
					movimiento = cmbMovimiento.getValue();
					String boleto = "";
					if (!txtBoleto.getValue().equals("TODOS"))
						boleto = txtBoleto.getValue();
					else
						boleto = "%";
					String vehiculo = "";
					if (!txtVehiculo.getValue().equals("TODOS"))
						vehiculo = idVehiculo;
					else
						vehiculo = "%";
					String conductor = "";
					if (!txtConductor.getValue().equals("TODOS"))
						conductor = idConductor;
					else
						conductor = "%";
					String producto = "";
					if (!txtProducto.getValue().equals("TODOS"))
						producto = String.valueOf(idProducto);
					else
						producto = "%";
					String balanza = "";
					if (!txtBalanza.getValue().equals("TODOS"))
						balanza = String.valueOf(idBalanza);
					else
						balanza = "%";
					String tipo = "";
					tipo = cmbTipo.getValue();

					String estatus = "%";
					if (movimiento.equals("ENTRADAS"))
						estatus = "Activo";
					if (movimiento.equals("SALIDAS"))
						estatus = "Cerrado";

					List<Pesaje> pesajes = new ArrayList<Pesaje>();

					if (!movimiento.equals("DEVOLUCIONES"))
						pesajes = servicioPesaje.buscarEntreFechasYParametros(
								desde, hasta, boleto, vehiculo, conductor,
								producto, balanza, estatus);
					else
						pesajes = servicioPesaje
								.buscarEntreFechasYParametrosDevoluciones(
										desde, hasta, boleto, vehiculo,
										conductor, producto, balanza);

					System.out.println(pesajes.size());
					if (!pesajes.isEmpty()) {

						DateFormat fecha = new SimpleDateFormat("dd-MM-yyyy");
						String fecha11 = fecha.format(dtbDesde.getValue());
						String fecha22 = fecha.format(dtbHasta.getValue());

						if (boleto.equals("%"))
							boleto = "TODOS";
						if (vehiculo.equals("%"))
							vehiculo = "TODOS";
						if (conductor.equals("%"))
							conductor = "TODOS";
						if (producto.equals("%"))
							producto = "TODOS";
						if (balanza.equals("%"))
							balanza = "TODOS";

						Clients.evalJavaScript("window.open('"
								+ damePath()
								+ "Generador?valor=2"
								+ "&valor2="
								+ fecha11
								+ "&valor3="
								+ fecha22
								+ "&valor4="
								+ movimiento
								+ "&valor5="
								+ boleto
								+ "&valor6="
								+ vehiculo
								+ "&valor7="
								+ conductor
								+ "&valor8="
								+ producto
								+ "&valor9="
								+ balanza
								+ "&valor20="
								+ tipo
								+ "','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')");

					} else
						msj.mensajeAlerta(Mensaje.noHayRegistros);
				}

			}

			@Override
			public void eliminar() {

			}

			@Override
			public void buscar() {
			}

			@Override
			public void ayuda() {

			}

			@Override
			public void annadir() {
			}
		};
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		Button guardar = (Button) botonera.getChildren().get(3);
		guardar.setLabel("Reporte");
		guardar.setImage("/public/imagenes/botones/reporte.png");
		divBotoneraPesaje.appendChild(botonera);
	}

	public void limpiarCampos() {
		txtBoleto.setValue("TODOS");
		txtBalanza.setValue("TODOS");
		txtConductor.setValue("TODOS");
		txtProducto.setValue("TODOS");
		txtVehiculo.setValue("TODOS");
		dtbDesde.setValue(fecha);
		dtbHasta.setValue(fecha);
		cmbMovimiento.setValue("TODOS");
		cmbTipo.setValue("PDF");
		lblVehiculo.setValue("");
		lblBalanza.setValue("");
		lblConductor.setValue("");
		lblProducto.setValue("");
	}

	protected boolean validar() {
		if (txtBoleto.getText().compareTo("") == 0) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else
			return true;
	}

	@Listen("onClick = #btnBuscarVehiculo")
	public void mostrarCatalogoVehiculo() {

		final List<Vehiculo> lista = new ArrayList<Vehiculo>();

		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setPlaca("TODOS");
		vehiculo.setDescripcion("TODOS");
		lista.add(vehiculo);
		lista.addAll(servicioVehiculo.buscarTodos());

		catalogoVehiculo = new Catalogo<Vehiculo>(divCatalogoVehiculo,
				"Catalogo de Vehiculos", lista, true, false, false, "Placa",
				"Descripcion", "Peso") {

			@Override
			protected List<Vehiculo> buscar(List<String> valores) {

				List<Vehiculo> listaa = new ArrayList<Vehiculo>();

				for (Vehiculo tipo : lista) {
					if (tipo.getPlaca().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& tipo.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& String.valueOf(tipo.getPeso()).toLowerCase()
									.contains(valores.get(2).toLowerCase())) {
						listaa.add(tipo);
					}
				}
				return listaa;
			}

			@Override
			protected String[] crearRegistros(Vehiculo tipo) {
				String[] registros = new String[3];
				registros[0] = tipo.getPlaca();
				registros[1] = tipo.getDescripcion();
				registros[2] = String.valueOf(tipo.getPeso());
				return registros;
			}
		};
		catalogoVehiculo.setParent(divCatalogoVehiculo);
		catalogoVehiculo.doModal();
	}

	@Listen("onSeleccion = #divCatalogoVehiculo")
	public void seleccionarDoctor() {
		Vehiculo tipo = catalogoVehiculo.objetoSeleccionadoDelCatalogo();
		idVehiculo = tipo.getPlaca();
		txtVehiculo.setValue(String.valueOf(tipo.getPlaca()));
		lblVehiculo.setValue(tipo.getDescripcion());
		catalogoVehiculo.setParent(null);
	}

	@Listen("onClick = #btnBuscarConductor")
	public void mostrarCatalogoConductor() {

		final List<Conductor> lista = new ArrayList<Conductor>();

		Conductor conductor = new Conductor();
		conductor.setApellidos("TODOS");
		conductor.setNombres("TODOS");
		conductor.setCedula("TODOS");
		lista.add(conductor);
		lista.addAll(servicioConductor.buscarTodos());

		catalogoConductor = new Catalogo<Conductor>(divCatalogoConductor,
				"Catalogo de Conductores", lista, true, false, false, "Cedula",
				"Nombres", "Apellidos") {

			@Override
			protected List<Conductor> buscar(List<String> valores) {

				List<Conductor> listaa = new ArrayList<Conductor>();

				for (Conductor tipo : lista) {
					if (tipo.getCedula().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& tipo.getNombres().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& tipo.getApellidos().toLowerCase()
									.contains(valores.get(2).toLowerCase())) {
						listaa.add(tipo);
					}
				}
				return listaa;
			}

			@Override
			protected String[] crearRegistros(Conductor tipo) {
				String[] registros = new String[3];
				registros[0] = tipo.getCedula();
				registros[1] = tipo.getNombres();
				registros[2] = tipo.getApellidos();
				return registros;
			}
		};
		catalogoConductor.setParent(divCatalogoConductor);
		catalogoConductor.doModal();
	}

	@Listen("onSeleccion = #divCatalogoConductor")
	public void seleccionarConductor() {
		Conductor tipo = catalogoConductor.objetoSeleccionadoDelCatalogo();
		idConductor = tipo.getCedula();
		txtConductor.setValue(String.valueOf(tipo.getCedula()));
		lblConductor.setValue(tipo.getNombres() + "  " + tipo.getApellidos());
		catalogoConductor.setParent(null);
	}

	@Listen("onClick = #btnBuscarProducto")
	public void mostrarCatalogoProducto() {

		final List<Producto> lista = new ArrayList<Producto>();

		Producto producto = new Producto();
		producto.setDescripcion("TODOS");
		producto.setIdProducto("TODOS");
		lista.add(producto);
		lista.addAll(servicioProducto.buscarTodos());

		catalogoProducto = new Catalogo<Producto>(divCatalogoProducto,
				"Catalogo de Productos", lista, true, false, false, "Codigo",
				"Descripcion") {

			@Override
			protected List<Producto> buscar(List<String> valores) {

				List<Producto> listaa = new ArrayList<Producto>();

				for (Producto tipo : lista) {
					if (tipo.getIdProducto().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& tipo.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())) {
						listaa.add(tipo);
					}
				}
				return listaa;
			}

			@Override
			protected String[] crearRegistros(Producto tipo) {
				String[] registros = new String[2];
				registros[0] = tipo.getIdProducto();
				registros[1] = tipo.getDescripcion();
				return registros;
			}
		};
		catalogoProducto.setParent(divCatalogoProducto);
		catalogoProducto.doModal();
	}

	@Listen("onSeleccion = #divCatalogoProducto")
	public void seleccionarProducto() {
		Producto tipo = catalogoProducto.objetoSeleccionadoDelCatalogo();
		idProducto = tipo.getIdProducto();
		txtProducto.setValue(String.valueOf(tipo.getIdProducto()));
		lblProducto.setValue(tipo.getDescripcion());
		catalogoProducto.setParent(null);
	}

	@Listen("onClick = #btnBuscarBalanza")
	public void mostrarCatalogoBalanza() {

		final List<Balanza> lista = new ArrayList<Balanza>();

		Balanza balanza = new Balanza();
		balanza.setDescripcion("TODOS");
		lista.add(balanza);
		lista.addAll(servicioBalanza.buscarTodos());

		catalogoBalanza = new Catalogo<Balanza>(divCatalogoBalanza,
				"Catalogo de Balanzas", lista, true, false, false,
				"Descripcion") {

			@Override
			protected List<Balanza> buscar(List<String> valores) {

				List<Balanza> listaa = new ArrayList<Balanza>();

				for (Balanza tipo : lista) {
					if (tipo.getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())) {
						listaa.add(tipo);
					}
				}
				return listaa;
			}

			@Override
			protected String[] crearRegistros(Balanza tipo) {
				String[] registros = new String[1];
				registros[0] = tipo.getDescripcion();
				return registros;
			}
		};
		catalogoBalanza.setParent(divCatalogoBalanza);
		catalogoBalanza.doModal();
	}

	@Listen("onSeleccion = #divCatalogoBalanza")
	public void seleccionarBalanza() {
		Balanza tipo = catalogoBalanza.objetoSeleccionadoDelCatalogo();
		idBalanza = tipo.getIdBalanza();
		txtBalanza.setValue(String.valueOf(tipo.getIdBalanza()));
		lblBalanza.setValue(tipo.getDescripcion());
		catalogoBalanza.setParent(null);
	}

	public byte[] mostrarReporte(String fecha1, String fecha2,
			String movimiento, String boleto, String vehiculo,
			String conductor, String producto, String balanza, String tipo) {

		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		Date desde = null;
		try {
			desde = formato.parse(fecha1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date hasta = null;
		try {
			hasta = formato.parse(fecha2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

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

		hasta = agregarDia(hasta);

		Map<String, Object> p = new HashMap<String, Object>();
		p.put("desde", fecha1);
		p.put("hasta", fecha2);
		p.put("movimiento", movimiento);
		p.put("imagen", imageBytes);

		String estatus = "%";
		if (movimiento.equals("ENTRADAS"))
			estatus = "Activo";
		if (movimiento.equals("SALIDAS"))
			estatus = "Cerrado";

		if (boleto.equals("TODOS"))
			boleto = "%";
		if (vehiculo.equals("TODOS"))
			vehiculo = "%";
		if (conductor.equals("TODOS"))
			conductor = "%";
		if (producto.equals("TODOS"))
			producto = "%";
		if (balanza.equals("TODOS"))
			balanza = "%";
		
		List<Pesaje> pesajes = new ArrayList<Pesaje>();

		if (movimiento.equals("DEVOLUCIONES"))
			pesajes = getServicioPesaje()
					.buscarEntreFechasYParametrosDevoluciones(desde, hasta,
							boleto, vehiculo, conductor, producto, balanza);
		else
			pesajes = getServicioPesaje().buscarEntreFechasYParametros(desde,
					hasta, boleto, vehiculo, conductor, producto, balanza,
					estatus);
		
		p.put("totalMovimientos", pesajes.size());
		
		return generarReporteGenerico(p, pesajes,
				"/reporte/ReportePesajes.jasper", tipo);
	}

}