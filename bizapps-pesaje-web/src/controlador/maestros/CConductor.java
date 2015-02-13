package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Conductor;
import modelo.maestros.Producto;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;

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
						String nombres = txtNombres.getValue();
						String apellidos = txtApellidos.getValue();
						id = txtCedula.getValue();
						Conductor conductor = new Conductor();
						conductor.setApellidos(apellidos);
						conductor.setNombres(nombres);
						conductor.setCedula(id);
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
				// if (gpxDatos.isOpen()) {
				// /* Elimina Varios Registros */
				// if (validarSeleccion()) {
				// final List<Balanza> eliminarLista = catalogo
				// .obtenerSeleccionados();
				// List<Pesaje> pesajes = servicioBalanza
				// .buscarPorIds(eliminarLista);
				// if (pesajes.isEmpty()) {
				// Messagebox
				// .show("¿Desea Eliminar los "
				// + eliminarLista.size()
				// + " Registros?",
				// "Alerta",
				// Messagebox.OK | Messagebox.CANCEL,
				// Messagebox.QUESTION,
				// new org.zkoss.zk.ui.event.EventListener<Event>() {
				// public void onEvent(Event evt)
				// throws InterruptedException {
				// if (evt.getName().equals(
				// "onOK")) {
				// servicioBalanza
				// .eliminarVarios(eliminarLista);
				// msj.mensajeInformacion(Mensaje.eliminado);
				// listaGeneral = servicioBalanza
				// .buscarTodos();
				// catalogo.actualizarLista(
				// listaGeneral,
				// true);
				// }
				// }
				// });
				//
				// } else
				// msj.mensajeError(Mensaje.noEliminar);
				// }
				// } else {
				// /* Elimina un solo registro */
				// if (id != 0) {
				// List<Pesaje> pesajes = servicioBalanza
				// .buscarPorBalanza(id);
				//
				// if (pesajes.isEmpty()) {
				// Messagebox
				// .show(Mensaje.deseaEliminar,
				// "Alerta",
				// Messagebox.OK | Messagebox.CANCEL,
				// Messagebox.QUESTION,
				// new org.zkoss.zk.ui.event.EventListener<Event>() {
				// public void onEvent(Event evt)
				// throws InterruptedException {
				// if (evt.getName().equals(
				// "onOK")) {
				//
				// servicioBalanza
				// .eliminarUno(id);
				// msj.mensajeInformacion(Mensaje.eliminado);
				// limpiar();
				// listaGeneral = servicioBalanza
				// .buscarTodos();
				// catalogo.actualizarLista(
				// listaGeneral,
				// true);
				// }
				// }
				// });
				//
				// } else
				// msj.mensajeError(Mensaje.noEliminar);
				// } else
				// msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);
				// }

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
		} else
			return true;
	}

	public boolean camposLLenos() {
		if (txtApellidos.getText().compareTo("") == 0
				||txtNombres.getText().compareTo("") == 0
				|| txtCedula.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtApellidos.getText().compareTo("") != 0
				||txtNombres.getText().compareTo("") != 0
				|| txtCedula.getText().compareTo("") != 0) {
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
				"Cedula", "Nombres","Apellidos") {

			@Override
			protected List<Conductor> buscar(List<String> valores) {

				List<Conductor> lista = new ArrayList<Conductor>();

				for (Conductor tipo : listaGeneral) {
					if (tipo.getCedula().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& tipo.getNombres().toLowerCase()
									.contains(valores.get(1).toLowerCase())
									&& tipo.getApellidos().toLowerCase()
									.contains(valores.get(2).toLowerCase())) {
						lista.add(tipo);
					}
				}
				return lista;
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
		catalogo.setParent(divCatalogoConductor);
	}

	public boolean idLibre() {
		if (servicioConductor.buscar(txtCedula.getValue()) != null)
			return false;
		else
			return true;

	}

}