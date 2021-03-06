package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Ciudad;
import modelo.maestros.Conductor;
import modelo.maestros.Transporte;
import modelo.maestros.Vehiculo;
import modelo.transacciones.Pesaje;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;
import componente.Validador;

public class CConductor extends CGenerico {

	private static final long serialVersionUID = -6868106910332150746L;
	@Wire
	private Textbox txtNombres;
	@Wire
	private Textbox txtApellidos;
	@Wire
	private Textbox txtCedula;
	@Wire
	private Div divConductor;
	@Wire
	private Div botoneraConductor;
	@Wire
	private Div divCatalogoConductor;
	@Wire
	private Combobox cmbCiudad;
	@Wire
	private Combobox cmbTransporte;
	@Wire
	private Textbox txtDireccion;
	@Wire
	private Textbox txtTelefono1;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;

	Botonera botonera;
	Catalogo<Conductor> catalogo;
	String id = "";
	private List<Conductor> listaGeneral = new ArrayList<Conductor>();

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
		txtNombres.setFocus(true);
		mostrarCatalogo();

		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Conductor tipo = catalogo
								.objetoSeleccionadoDelCatalogo();
						id = tipo.getCedula();
						txtApellidos.setValue(tipo.getApellidos());
						txtNombres.setValue(tipo.getNombres());
						txtCedula.setValue(tipo.getCedula());
						txtCedula.setDisabled(true);
						txtDireccion.setValue(tipo.getDireccion());
						txtTelefono1.setValue(tipo.getTelefono());
						if (tipo.getCiudad() != null)
							cmbCiudad.setValue(tipo.getCiudad().getNombre());
						if (tipo.getTransporte() != null)
							cmbTransporte.setValue(tipo.getTransporte().getDescripcion());
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divConductor, cerrar, tabs);

			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
				id = "";
			}

			@Override
			public void guardar() {
				if (validar()) {
					if (id.equals("") && !idLibre())
						msj.mensajeError(Mensaje.cedulaUsada);
					else {
						Ciudad ciudad = null;
						if (cmbCiudad.getSelectedItem() != null)
							ciudad = servicioCiudad.buscar(Long
									.parseLong(cmbCiudad.getSelectedItem()
											.getContext()));
						Transporte transporte = null;
						if (cmbTransporte.getSelectedItem() != null)
							transporte = servicioTransporte.buscar(Long
									.parseLong(cmbTransporte.getSelectedItem()
											.getContext()));
						String nombres = txtNombres.getValue();
						String apellidos = txtApellidos.getValue();
						id = txtCedula.getValue();
						Conductor conductor = new Conductor();
						conductor.setApellidos(apellidos);
						conductor.setNombres(nombres);
						conductor.setCedula(id);
						conductor.setDireccion(txtDireccion.getValue());
						conductor.setTelefono(txtTelefono1.getValue());
						conductor.setCiudad(ciudad);
						conductor.setTransporte(transporte);
						conductor.setUsuarioAuditoria(nombreUsuarioSesion());
						conductor.setFechaAuditoria(fechaHora);
						conductor.setHoraAuditoria(horaAuditoria);
						servicioConductor.guardar(conductor);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();
						listaGeneral = servicioConductor.buscarTodos();
						catalogo.actualizarLista(listaGeneral, true);
						abrirCatalogo();
					}
				}
			}

			@Override
			public void eliminar() {

				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Conductor> eliminarLista = catalogo
								.obtenerSeleccionados();
						List<Pesaje> pesajes = servicioPesaje
								.buscarPorConductor(eliminarLista);
						if (pesajes.isEmpty()) {
						Messagebox
								.show("�Desea Eliminar los "
										+ eliminarLista.size() + " Registros?",
										"Alerta",
										Messagebox.OK | Messagebox.CANCEL,
										Messagebox.QUESTION,
										new org.zkoss.zk.ui.event.EventListener<Event>() {
											public void onEvent(Event evt)
													throws InterruptedException {
												if (evt.getName()
														.equals("onOK")) {
													servicioConductor
															.eliminarVarios(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(
															servicioConductor
																	.buscarTodos(),
															true);
												}
											}
										});
						}
						else
						msj.mensajeError(Mensaje.noEliminar);
					}
				} else {
					/* Elimina un solo registro */
					if (!id.equals("")) {
						final Conductor conductor = servicioConductor
								.buscar(id);
						List<Pesaje> pesajes = servicioPesaje
								.buscarPorConductor(conductor);
						if (pesajes.isEmpty()) 
						{
						Messagebox
								.show(Mensaje.deseaEliminar,
										"Alerta",
										Messagebox.OK | Messagebox.CANCEL,
										Messagebox.QUESTION,
										new org.zkoss.zk.ui.event.EventListener<Event>() {
											public void onEvent(Event evt)
													throws InterruptedException {
												if (evt.getName()
														.equals("onOK")) {
													servicioConductor.eliminarUno(id);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(
															servicioConductor
																	.buscarTodos(),
															true);
												}
											}
										});
						}
						else
							msj.mensajeError(Mensaje.noEliminar);
					} else
						msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);
				
				}
			}

			@Override
			public void buscar() {
				abrirCatalogo();
			}

			@Override
			public void ayuda() {

			}

			@Override
			public void annadir() {
				abrirRegistro();
				mostrarBotones(false);
			}
		};
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(3).setVisible(false);
		botonera.getChildren().get(5).setVisible(false);
		botoneraConductor.appendChild(botonera);

	}

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(2).setVisible(bol);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(3).setVisible(!bol);
		botonera.getChildren().get(5).setVisible(!bol);
	}

	public void limpiarCampos() {
		id = "";
		txtNombres.setValue("");
		txtApellidos.setValue("");
		txtCedula.setValue("");
		txtCedula.setDisabled(false);
		txtDireccion.setValue("");
		txtTelefono1.setValue("");
		cmbCiudad.setValue("");
		cmbTransporte.setValue("");
	}

	public boolean validarSeleccion() {
		List<Conductor> seleccionados = catalogo.obtenerSeleccionados();
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
		if (!camposLLenos()) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else {
			if (!Validador.validarTelefono(txtTelefono1.getValue())) {
				msj.mensajeError(Mensaje.telefonoInvalido);
				return false;
			} else if (!Validador.validarNumero(txtCedula.getValue())) {
				msj.mensajeError("La cedula debe contener solo numeros");
				return false;
			} else
				return true;
		}
	}

	public boolean camposLLenos() {
		if (txtApellidos.getText().compareTo("") == 0
				|| txtNombres.getText().compareTo("") == 0
				|| txtCedula.getText().compareTo("") == 0
				|| txtDireccion.getText().compareTo("") == 0
				|| txtTelefono1.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtApellidos.getText().compareTo("") != 0
				|| txtNombres.getText().compareTo("") != 0
				|| txtCedula.getText().compareTo("") != 0
				|| txtDireccion.getText().compareTo("") != 0
				|| txtTelefono1.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
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
			mostrarBotones(true);
		}
	}

	public void mostrarCatalogo() {
		listaGeneral = servicioConductor.buscarTodos();
		catalogo = new Catalogo<Conductor>(divCatalogoConductor,
				"Catalogo de Conductores", listaGeneral, false, false, false,
				"Cedula", "Nombres", "Apellidos", "Direccion", "Telefono") {

			@Override
			protected List<Conductor> buscar(List<String> valores) {

				List<Conductor> lista = new ArrayList<Conductor>();
                                    
				for (Conductor tipo : listaGeneral) {
					String nombre ="";
					String apellido = "";
					String direccion ="";
					String telefono = "";
					if(tipo.getNombres()!=null)
						nombre = tipo.getNombres();
					if(tipo.getApellidos()!=null)
						apellido = tipo.getApellidos();
					if(tipo.getDireccion()!=null)
						direccion = tipo.getDireccion();
					if(tipo.getTelefono()!=null)
						telefono = tipo.getTelefono();
					if (tipo.getCedula().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& nombre.toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& apellido.toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& direccion.toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& telefono.toLowerCase()
									.contains(valores.get(4).toLowerCase())) {
						lista.add(tipo);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(Conductor tipo) {
				String[] registros = new String[5];
				registros[0] = tipo.getCedula();
				registros[1] = tipo.getNombres();
				registros[2] = tipo.getApellidos();
				registros[3] = tipo.getDireccion();
				registros[4] = tipo.getTelefono();
				return registros;
			}
		};
		catalogo.setParent(divCatalogoConductor);
	}

	public boolean idLibre() {
		if (servicioConductor.buscar(txtCedula.getValue()) != null)
			return false;
		else
			return true;
	}

	@Listen("onOpen = #cmbCiudad")
	public void llenarComboCiudad() {
		List<Ciudad> ciudades = servicioCiudad.buscarTodas();
		cmbCiudad.setModel(new ListModelList<Ciudad>(ciudades));
	}

	@Listen("onOpen = #cmbTransporte")
	public void llenarComboTransporte() {
		List<Transporte> transportes = servicioTransporte.buscarTodos();
		cmbTransporte.setModel(new ListModelList<Transporte>(transportes));
	}

	/* Metodo que valida el formmato del telefono ingresado */
	@Listen("onChange = #txtTelefono1")
	public void validarTelefono() throws IOException {
		if (Validador.validarTelefono(txtTelefono1.getValue()) == false) {
			msj.mensajeAlerta(Mensaje.telefonoInvalido);
		}
	}

	/* Metodo que valida el formmato del telefono ingresado */
	@Listen("onChange = #txtCedula")
	public void validarCedula() throws IOException {
		if (!Validador.validarNumero(txtCedula.getValue())) {
			msj.mensajeError("La cedula debe contener solo numeros");
		}
	}

}