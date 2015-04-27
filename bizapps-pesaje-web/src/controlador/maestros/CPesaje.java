package controlador.maestros;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import modelo.maestros.Almacen;
import modelo.maestros.Balanza;
import modelo.maestros.Conductor;
import modelo.maestros.Producto;
import modelo.maestros.Transporte;
import modelo.maestros.Vehiculo;
import modelo.seguridad.Usuario;
import modelo.transacciones.Pesaje;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;

public class CPesaje extends CGenerico {
	@Wire
	private Div divPesaje;
	@Wire
	private Div divBotoneraPesaje;
	@Wire
	private Div divCatalogoPesaje;
//	@Wire
//	private Div divCatalogoPesajeCerrado;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	@Wire
	private Textbox txtBoleto;
	@Wire
	private Div divCatalogoAlmacen;
	@Wire
	private Textbox txtAlmacen;
	@Wire
	private Label lblAlmacen;
	@Wire
	private Div divCatalogoVehiculo;
	@Wire
	private Textbox txtVehiculo;
	@Wire
	private Label lblVehiculo;
	@Wire
	private Div divCatalogoTransporte;
	@Wire
	private Textbox txtTransporte;
	@Wire
	private Label lblTransporte;
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
	private Button btnBuscarProducto;
	@Wire
	private Button btnBuscarConductor;
	@Wire
	private Button btnBuscarTransporte;
	@Wire
	private Button btnBuscarAlmacen;
	@Wire
	private Button btnBuscarVehiculo;
	@Wire
	private Button btnAutomatico;
	@Wire
	private Button btnManual;
	@Wire
	private Button btnBuscarBalanza;
	@Wire
	private Button btnDevolucion;
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

	Catalogo<Almacen> catalogoAlmacen;
	Catalogo<Producto> catalogoProducto;
	Catalogo<Vehiculo> catalogoVehiculo;
	Catalogo<Conductor> catalogoConductor;
	Catalogo<Transporte> catalogoTransporte;
	Catalogo<Balanza> catalogoBalanza;
	Catalogo<Pesaje> catalogoPesaje;
//	Catalogo<Pesaje> catalogoPesajeCerrado;
	long id = 0;
//	long idCerrado = 0;
	String idVehiculo = "";
	long idTransporte = 0;
	String idConductor = "";
	String idProducto = "";
	long idAlmacen = 0;
	long idBalanza = 0;
	private List<Pesaje> listaGeneral = new ArrayList<Pesaje>();
	String ip_balanza = "";
	int port_balanza = 0;

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

		mostrarCatalogoPesaje();

		Usuario usuario = usuarioSesion(nombreUsuarioSesion());
		if (usuario.isVerPesajeYEditar())
			btnManual.setDisabled(false);
		else
			btnManual.setDisabled(true);

		btnDevolucion.setDisabled(true);

		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogoPesaje.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						limpiarCampos();
						abrirRegistro();
						Pesaje tipo = catalogoPesaje
								.objetoSeleccionadoDelCatalogo();
						id = tipo.getBoleto();
						llenarCamposPesaje(tipo);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}

			}

			@Override
			public void salir() {
				cerrarVentana(divPesaje, cerrar, tabs);

			}

			@Override
			public void reporte() {

				if (id != 0) {
					Clients.evalJavaScript("window.open('"
							+ damePath()
							+ "Generador?valor=1&valor2="
							+ String.valueOf(id)
							+ "','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')");
				}
//				else {
//					if (idCerrado != 0)
//						Clients.evalJavaScript("window.open('"
//								+ damePath()
//								+ "Generador?valor=1&valor2="
//								+ String.valueOf(idCerrado)
//								+ "','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')");
//					else
//						msj.mensajeError("Debe Seleccionar un Pesaje");
//				}
			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
				editable(false);
			}

			@Override
			public void guardar() {
			//	if (idCerrado == 0) {
					if (validar()) {
						Pesaje pesaje = new Pesaje();
						Balanza balanza = servicioBalanza.buscar(idBalanza);
						if (id == 0) {
							Producto producto = servicioProducto
									.buscar(idProducto);
							Vehiculo vehiculo = servicioVehiculo
									.buscar(idVehiculo);
							Conductor conductor = servicioConductor
									.buscar(idConductor);
							Transporte transporte = servicioTransporte
									.buscar(idTransporte);
							Almacen almacen = new Almacen();
							if (idAlmacen != 0) {
								almacen = servicioAlmacen.buscar(idAlmacen);
								pesaje.setAlmacen(almacen);
							}

							pesaje.setConductor(conductor);
							pesaje.setEntrada(dbxVehiculoEntrada.getValue());
							pesaje.setTransporte(transporte);
							pesaje.setVehiculo(vehiculo);
							pesaje.setProducto(producto);
							pesaje.setBalanza(balanza);
							Timestamp fechaPesaje = new Timestamp(
									dtbFechaEntrada.getValue().getTime());
							pesaje.setFechaPesaje(fechaPesaje);
							pesaje.setHoraPesaje(metodoHora());
							pesaje.setEstatus("Activo");
							pesaje.setNroFactura(txtNroFactura.getValue());

						} else {
							pesaje = servicioPesaje.buscar(id);
							Timestamp fechaPesaje = new Timestamp(
									dtbFechaSalida.getValue().getTime());
							pesaje.setFechaPesajeSalida(fechaPesaje);
							Double pesoSalida = dbxVehiculoSalida.getValue();
							pesaje.setSalida(pesoSalida);
							pesaje.setEstatus("Cerrado");
							pesaje.setPesoPTSalida(dbxPesoPTSalida.getValue());
							pesaje.setBalanza(balanza);
							Clients.evalJavaScript("window.open('"
									+ damePath()
									+ "Generador?valor=1&valor2="
									+ String.valueOf(id)
									+ "','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')");
						}
						pesaje.setNroPredespacho(txtNroPredespacho.getValue());
						pesaje.setDespachador(txtDespachador.getValue());
						pesaje.setDestino(txtDestino.getValue());
						pesaje.setProcedencia(txtProcedencia.getValue());
						if (spnCajas.getValue() != null)
							pesaje.setCantCajas(spnCajas.getValue());
						if (dbsPesoOrigen.getValue() != null)
							pesaje.setPesoOrigen(dbsPesoOrigen.getValue());
						pesaje.setObservacion(txtObservacion.getValue());
						pesaje.setNroFactura(txtNroFactura.getValue());
						pesaje.setHoraAuditoria(metodoHora());
						pesaje.setFechaAuditoria(metodoFecha());
						servicioPesaje.guardar(pesaje);
						if (id == 0) {
							Pesaje pesaje2 = servicioPesaje.buscarUltimo();
							Clients.evalJavaScript("window.open('"
									+ damePath()
									+ "Generador?valor=1&valor2="
									+ String.valueOf(pesaje2.getBoleto())
									+ "','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')");
						}
						limpiar();
						listaGeneral = servicioPesaje
								.buscarPorEstatus("Activo");
						catalogoPesaje.actualizarLista(listaGeneral, true);
						msj.mensajeInformacion(Mensaje.guardado);
					}
				//}
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void buscar() {
				abrirCatalogo();

			}

			@Override
			public void ayuda() {
			//	mostrarCatalogoPesajeCerrados();

			}

			@Override
			public void annadir() {
				abrirRegistro();
				mostrarBotones(false);

			}
		};

		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(3).setVisible(false);
		botonera.getChildren().get(5).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		divBotoneraPesaje.appendChild(botonera);
	}

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(2).setVisible(bol);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(!bol);
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(3).setVisible(!bol);
		botonera.getChildren().get(5).setVisible(!bol);
	}

	@Listen("onClick = #gpxRegistro")
	public void abrirRegistro() {
		gpxDatos.setOpen(false);
		gpxRegistro.setOpen(true);
		mostrarBotones(false);
	}

	@Listen("onOpen = #gpxDatos")
	public void abrirCatalogo() {
		gpxDatos.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatos.setOpen(false);
								gpxRegistro.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatos.setOpen(true);
									gpxRegistro.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatos.setOpen(true);
			gpxRegistro.setOpen(false);
			limpiarCampos();
			mostrarBotones(true);
		}
	}

	public boolean camposEditando() {
		return false;
	}

	public boolean validarSeleccion() {
		List<Pesaje> seleccionados = catalogoPesaje.obtenerSeleccionados();
		if (seleccionados == null) {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			return false;
		} else {
			if (seleccionados.isEmpty()) {
				msj.mensajeAlerta(Mensaje.noSeleccionoItem);
				return false;
			} else {
				return true;
			}
		}
	}

	protected boolean validar() {
		if (id == 0) {
			if (dtbFechaEntrada.getValue() == null
					|| dbxVehiculoEntrada.getText().compareTo("") == 0
					|| dbxPesoPTEntrada.getText().compareTo("") == 0
					|| idConductor.equals("") || idProducto.equals("")
					|| idTransporte == 0 || idVehiculo.equals("")
					|| idBalanza == 0 || idAlmacen == 0
					|| txtDestino.getText().compareTo("") == 0
					|| txtProcedencia.getText().compareTo("") == 0) {
				msj.mensajeError(Mensaje.camposVacios);
				return false;
			} else
				return true;
		} else {
			if (dtbFechaSalida.getValue() == null
					|| dbxVehiculoSalida.getText().compareTo("") == 0
					|| dbxPesoPTSalida.getText().compareTo("") == 0) {
				msj.mensajeError(Mensaje.camposVacios);
				return false;
			} else
				return true;
		}
	}

	public void limpiarCampos() {

		btnDevolucion.setDisabled(true);
		id = 0;
//		idCerrado = 0;
		idVehiculo = "";
		idTransporte = 0;
		idConductor = "";
		idProducto = "";
		idAlmacen = 0;
		idBalanza = 0;
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
		inhabilitar(false);
		inhabilitarParaSalida(false);
	}

	public void mostrarCatalogoPesaje() {
		listaGeneral = servicioPesaje.buscarPorEstatus("Activo");
		catalogoPesaje = new Catalogo<Pesaje>(divCatalogoPesaje,
				"Catalogo de Pesajes", listaGeneral, false, false, false,
				"Boleto", "Fecha", "Producto", "Placa Vehiculo", "Conductor",
				"Estatus") {

			@Override
			protected List<Pesaje> buscar(List<String> valores) {

				List<Pesaje> lista = new ArrayList<Pesaje>();

				for (Pesaje tipo : listaGeneral) {
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
						lista.add(tipo);
					}
				}
				return lista;
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
		catalogoPesaje.setParent(divCatalogoPesaje);
	}

	@Listen("onClick = #btnBuscarAlmacen")
	public void mostrarCatalogoAlmacen() {
		final List<Almacen> lista = servicioAlmacen.buscarTodos();
		catalogoAlmacen = new Catalogo<Almacen>(divCatalogoAlmacen,
				"Catalogo de Almacenes", lista, true, false, false,
				"Descripcion") {

			@Override
			protected List<Almacen> buscar(List<String> valores) {

				List<Almacen> listaa = new ArrayList<Almacen>();

				for (Almacen tipo : lista) {
					if (tipo.getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())) {
						listaa.add(tipo);
					}
				}
				return listaa;
			}

			@Override
			protected String[] crearRegistros(Almacen tipo) {
				String[] registros = new String[1];
				registros[0] = tipo.getDescripcion();
				return registros;
			}
		};
		catalogoAlmacen.setParent(divCatalogoAlmacen);
		catalogoAlmacen.doModal();
	}

	@Listen("onSeleccion = #divCatalogoAlmacen")
	public void seleccionarAlmacen() {
		Almacen tipo = catalogoAlmacen.objetoSeleccionadoDelCatalogo();
		txtAlmacen.setValue(String.valueOf(tipo.getIdAlmacen()));
		lblAlmacen.setValue(tipo.getDescripcion());
		idAlmacen = tipo.getIdAlmacen();
		catalogoAlmacen.setParent(null);
	}

	@Listen("onClick = #btnBuscarVehiculo")
	public void mostrarCatalogoVehiculo() {

		final List<Vehiculo> lista = servicioVehiculo.buscarTodos();
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
		lblVehiculo.setValue(tipo.getDescripcion()+"   "+"Placa Chuto:"+"   "+tipo.getPlacaChuto()+"   "+"Placa Batea:"+"   "+tipo.getPlacaBatea());
		catalogoVehiculo.setParent(null);
	}

	@Listen("onClick = #btnBuscarTransporte")
	public void mostrarCatalogoTransporte() {
		final List<Transporte> lista = servicioTransporte.buscarTodos();
		catalogoTransporte = new Catalogo<Transporte>(divCatalogoTransporte,
				"Catalogo de Transportes", lista, true, false, false, "Codigo",
				"Descripcion") {

			@Override
			protected List<Transporte> buscar(List<String> valores) {

				List<Transporte> listaa = new ArrayList<Transporte>();

				for (Transporte tipo : lista) {
					if (tipo.getCodigo().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& tipo.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())) {
						listaa.add(tipo);
					}
				}
				return listaa;
			}

			@Override
			protected String[] crearRegistros(Transporte tipo) {
				String[] registros = new String[2];
				registros[0] = tipo.getCodigo();
				registros[1] = tipo.getDescripcion();
				return registros;
			}
		};
		catalogoTransporte.setParent(divCatalogoTransporte);
		catalogoTransporte.doModal();
	}

	@Listen("onSeleccion = #divCatalogoTransporte")
	public void seleccionarTransporte() {
		Transporte tipo = catalogoTransporte.objetoSeleccionadoDelCatalogo();
		idTransporte = tipo.getIdTransporte();
		txtTransporte.setValue(String.valueOf(tipo.getCodigo()));
		lblTransporte.setValue(tipo.getDescripcion());
		catalogoTransporte.setParent(null);
	}

	@Listen("onClick = #btnBuscarConductor")
	public void mostrarCatalogoConductor() {
		final List<Conductor> lista = servicioConductor.buscarTodos();
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
		final List<Producto> lista = servicioProducto.buscarTodos();
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
		final List<Balanza> lista = servicioBalanza.buscarTodos();
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
		ip_balanza = tipo.getIp();
		port_balanza = Integer.parseInt(tipo.getPuerto());
		catalogoBalanza.setParent(null);
	}
//
//	public void mostrarCatalogoPesajeCerrados() {
//
//		final List<Pesaje> lista = servicioPesaje.buscarPorEstatus("Cerrado");
//		catalogoPesajeCerrado = new Catalogo<Pesaje>(divCatalogoPesajeCerrado,
//				"Catalogo de Pesajes Cerrados", lista, true, false, false,
//				"Boleto", "Fecha", "Producto", "Placa Vehiculo", "Conductor",
//				"Estatus") {
//
//			@Override
//			protected List<Pesaje> buscar(List<String> valores) {
//
//				List<Pesaje> listaa = new ArrayList<Pesaje>();
//
//				for (Pesaje tipo : lista) {
//					if (String.valueOf(tipo.getBoleto()).toLowerCase()
//							.contains(valores.get(0).toLowerCase())
//							&& String.valueOf(tipo.getFechaPesaje())
//									.toLowerCase()
//									.contains(valores.get(1).toLowerCase())
//							&& String
//									.valueOf(
//											tipo.getProducto().getDescripcion())
//									.toLowerCase()
//									.contains(valores.get(2).toLowerCase())
//							&& String.valueOf(tipo.getVehiculo().getPlaca())
//									.toLowerCase()
//									.contains(valores.get(3).toLowerCase())
//							&& String.valueOf(tipo.getConductor().getNombres())
//									.toLowerCase()
//									.contains(valores.get(4).toLowerCase())
//							&& String.valueOf(tipo.getEstatus()).toLowerCase()
//									.contains(valores.get(5).toLowerCase())) {
//						listaa.add(tipo);
//					}
//				}
//				return listaa;
//			}
//
//			@Override
//			protected String[] crearRegistros(Pesaje tipo) {
//				String[] registros = new String[6];
//				registros[0] = String.valueOf(tipo.getBoleto());
//				registros[1] = String.valueOf(tipo.getFechaPesaje());
//				registros[2] = String.valueOf(tipo.getProducto()
//						.getDescripcion());
//				registros[3] = String.valueOf(tipo.getVehiculo().getPlaca());
//				registros[4] = String.valueOf(tipo.getConductor().getNombres()
//						+ "  " + tipo.getConductor().getApellidos());
//				registros[5] = String.valueOf(tipo.getEstatus());
//				return registros;
//			}
//		};
//		catalogoPesajeCerrado.setParent(divCatalogoPesajeCerrado);
//		catalogoPesajeCerrado.doModal();
//	}
//
//	@Listen("onSeleccion = #divCatalogoPesajeCerrado")
//	public void seleccionarCerrado() {
//		limpiarCampos();
//		Pesaje tipo = catalogoPesajeCerrado.objetoSeleccionadoDelCatalogo();
//		idCerrado = tipo.getBoleto();
//		llenarCamposPesaje(tipo);
//		catalogoPesajeCerrado.setParent(null);
//	}

	public void llenarCamposPesaje(Pesaje tipo) {

		if (tipo.getBalanza() != null)
			idBalanza = tipo.getBalanza().getIdBalanza();
		if (tipo.getAlmacen() != null)
			idAlmacen = tipo.getAlmacen().getIdAlmacen();

		btnDevolucion.setDisabled(false);

		txtBoleto.setValue(String.valueOf(tipo.getBoleto()));
		if (tipo.getAlmacen() != null) {
			txtAlmacen.setValue(String
					.valueOf(tipo.getAlmacen().getIdAlmacen()));
			lblAlmacen.setValue(tipo.getAlmacen().getDescripcion());
			idAlmacen = tipo.getAlmacen().getIdAlmacen();
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
			idBalanza = tipo.getBalanza().getIdBalanza();
			ip_balanza = tipo.getBalanza().getIp();
			port_balanza = Integer.parseInt(tipo.getBalanza().getPuerto());

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
		editable(false);
		inhabilitarParaSalida(true);
//		if (idCerrado != 0) {
//			if (tipo.getSalida() != null) {
//				dbxVehiculoSalida.setValue(tipo.getSalida());
//				dbxTotalSalida.setValue(tipo.getSalida());
//				if (tipo.getEntrada() != null) {
//					double entrada = tipo.getEntrada();
//					Double total = (double) 0;
//					if (entrada > tipo.getSalida())
//						total = entrada - tipo.getSalida();
//					else
//						total = tipo.getSalida() - entrada;
//					dbxTotal.setValue(total);
//					Double diferencia = (double) 0;
//					if (tipo.getPesoPTSalida() != null) {
//						dbxPesoPTSalida.setValue(tipo.getPesoPTSalida());
//						diferencia = tipo.getEntrada() + tipo.getPesoPTSalida()
//								- tipo.getSalida();
//						if (diferencia < 0)
//							diferencia = diferencia * (-1);
//						dbxDiferenciaSalida.setValue(diferencia);
//					}
//				}
//				dtbFechaSalida.setDisabled(true);
//				dtbFechaSalida.setValue(tipo.getFechaPesajeSalida());
//			}
//			inhabilitar(true);
//			btnDevolucion.setDisabled(true);
//		}

	}

	@Listen("onClick = #btnAutomatico")
	public void automatico() {
		editable(false);

		if (txtBalanza.getText().compareTo("") != 0) {

			if (id == 0) {
				dbxDiferenciaEntrada.setValue(0);
				dbxPesoPTEntrada.setValue(0);
				dbxPesoPTSalida.setReadonly(true);
				dtbFechaEntrada.setValue(fechaHora);
				Double peso = obtenerPeso(ip_balanza, port_balanza);
				dbxVehiculoEntrada.setValue(peso);
				dbxTotalEntrada.setValue(peso);
				dbxTotal.setValue(peso);
			} else {
				if (dbxPesoPTSalida.getValue() != null) {
					dtbFechaSalida.setValue(fechaHora);
					Double peso = obtenerPeso(ip_balanza, port_balanza);
					dbxVehiculoSalida.setValue(peso);
					dbxTotalSalida.setValue(peso);
					Double total = (double) 0;
					Pesaje pesaje = servicioPesaje.buscar(id);
					double entrada = pesaje.getEntrada();
					if (entrada > peso)
						total = entrada - peso;
					else
						total = peso - entrada;

					dbxTotal.setValue(total);
					Double diferencia = (double) 0;
					diferencia = entrada + dbxPesoPTSalida.getValue() - peso;
					if (diferencia < 0)
						diferencia = diferencia * (-1);
					dbxDiferenciaSalida.setValue(diferencia);

				} else
					msj.mensajeError("Debe Ingresar el Peso PT");
			}
		} else {

			msj.mensajeError("Debe seleccionar una balanza");
		}

	}

	@Listen("onChange = #dbxVehiculoEntrada")
	public void colocoEntrada() {
		if (id == 0) {
			dbxTotal.setValue(dbxVehiculoEntrada.getValue());
			dbxTotalEntrada.setValue(dbxVehiculoEntrada.getValue());
			dbxTotal.setReadonly(true);
			dbxTotalEntrada.setReadonly(true);
		}
	}

	@Listen("onClick = #btnManual")
	public void pendiente() {
		if (id == 0) {
			limpiarCamposLectura();
			editable(true);
			dbxDiferenciaEntrada.setValue(0);
			dbxPesoPTEntrada.setValue(0);
			dbxPesoPTSalida.setReadonly(true);
			dbxVehiculoSalida.setReadonly(true);
			dbxTotalSalida.setReadonly(true);
			dtbFechaSalida.setReadonly(true);
		} else {
			editable(false);
			dbxPesoPTSalida.setReadonly(false);
			dbxVehiculoSalida.setReadonly(false);
			dbxTotalSalida.setReadonly(false);
			dtbFechaSalida.setReadonly(false);
			dtbFechaSalida.setDisabled(false);
			dtbFechaSalida.setValue(fechaHora);
		}
	}

	@Listen("onChange = #dbxVehiculoSalida")
	public void manualSalida() {
		if (id != 0) {
			if (dbxPesoPTSalida.getValue() != null) {
				dtbFechaSalida.setValue(fechaHora);
				Pesaje pesaje = servicioPesaje.buscar(id);
				double entrada = pesaje.getEntrada();
				dbxTotalSalida.setValue(dbxVehiculoSalida.getValue());
				dbxTotalSalida.setReadonly(true);
				Double diferencia = (double) 0;
				diferencia = entrada + dbxPesoPTSalida.getValue()
						- dbxVehiculoSalida.getValue();
				if (diferencia < 0)
					diferencia = diferencia * (-1);
				dbxDiferenciaSalida.setValue(diferencia);
				Double total = (double) 0;
				if (entrada > dbxVehiculoSalida.getValue())
					total = entrada - dbxVehiculoSalida.getValue();
				else
					total = dbxVehiculoSalida.getValue() - entrada;
				dbxTotal.setValue(total);
			} else {
				msj.mensajeError("Debe Ingresar el Peso PT");
				dbxVehiculoSalida.setValue(null);
			}
		}

	}

	private Double obtenerPeso(String ip, int port) {

		Socket client = null;
		PrintWriter output = null;
		BufferedReader in = null;
		byte[] bytes = new byte[1];
		String strx = new String();
		Double numero = 0.0;

		try {
			// client = new Socket("172.23.22.12", 1600);
			// System.out.println(ip);
			// System.out.println(port);
			client = new Socket(ip, port);
			output = new PrintWriter(client.getOutputStream(), false);
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));

			int contadorA = 0;
			while (true) {
				int caracter = in.read();
				// System.out.println(caracter);

				if (caracter >= 48 && caracter <= 57) {
					bytes[0] = (byte) caracter;
					String numero_aux = new String(bytes, "UTF-8");
					strx += numero_aux;
				}

				if (caracter == 65) {
					contadorA++;
				}

				if (contadorA >= 2)
					break;

			}

			// String numero=new String(bytes, "UTF-8");

			try {
				numero = Double.parseDouble(strx);
			} catch (Exception ex) {
				numero = 0.0;
			}
			// System.out.println(numero);
			// System.out.println(Float.parseFloat(numero));

			output.close();
			in.close();
			client.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return numero;
	}

	public void editable(boolean a) {
		dbxTotalEntrada.setReadonly(!a);
		dbxTotalSalida.setReadonly(!a);
		dbxVehiculoEntrada.setReadonly(!a);
		dbxVehiculoSalida.setReadonly(!a);
		dbxTotal.setReadonly(!a);
		dtbFechaEntrada.setReadonly(!a);
		dtbFechaSalida.setReadonly(!a);
		dbxPesoPTEntrada.setReadonly(true);
		dbxPesoPTSalida.setReadonly(false);
		dtbFechaEntrada.setDisabled(!a);
		dtbFechaSalida.setDisabled(!a);
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
		dbxPesoPTSalida.setReadonly(false);
		dtbFechaEntrada.setValue(fechaHora);
		dtbFechaSalida.setValue(null);
	}

	public void inhabilitarParaSalida(boolean a) {
		btnBuscarAlmacen.setDisabled(a);
		btnBuscarConductor.setDisabled(a);
		btnBuscarProducto.setDisabled(a);
		btnBuscarTransporte.setDisabled(a);
		btnBuscarVehiculo.setDisabled(a);
	}

	public void inhabilitar(boolean a) {
		dbxPesoPTSalida.setReadonly(a);
		txtNroFactura.setDisabled(a);
		txtObservacion.setDisabled(a);
		btnAutomatico.setDisabled(a);
		btnBuscarAlmacen.setDisabled(a);
		btnBuscarConductor.setDisabled(a);
		btnBuscarProducto.setDisabled(a);
		btnBuscarTransporte.setDisabled(a);
		btnBuscarVehiculo.setDisabled(a);
		btnBuscarBalanza.setDisabled(a);
		Usuario usuario = usuarioSesion(nombreUsuarioSesion());

		if (usuario.isVerPesajeYEditar())
			btnManual.setDisabled(false);
		else
			btnManual.setDisabled(a);

		if (usuario.isVerPesajeYEditar()) {
			if (btnManual.isDisabled())
				btnManual.setDisabled(false);
		} else {
			if (a && !btnManual.isDisabled())
				btnManual.setDisabled(a);
		}

//		if (idCerrado != 0) {
//			if (!btnManual.isDisabled())
//				btnManual.setDisabled(true);
//		}

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

	@Listen("onClick = #btnDevolucion")
	public void devolucion() {
		if (id != 0) {
			Pesaje pesaje = servicioPesaje.buscar(id);
			if (pesaje != null) {
				HashMap<String, Object> mapa = new HashMap<String, Object>();

				mapa.put("boleto", String.valueOf(pesaje.getBoleto()));
				mapa.put("c", this);
				Sessions.getCurrent().setAttribute("devolucion", mapa);
				Window window = (Window) Executions.createComponents(
						"/vistas/transacciones/VDevolucion.zul", null, mapa);
				window.doModal();
			}
		}
	}
}
