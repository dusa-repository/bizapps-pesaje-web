package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Vehiculo;

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

public class CVehiculo extends CGenerico {

	private static final long serialVersionUID = -6868106910332150746L;
	@Wire
	private Textbox txtDescripcion;
	@Wire
	private Textbox txtPlaca;
	@Wire
	private Div divVehiculo;
	@Wire
	private Div botoneraVehiculo;
	@Wire
	private Div divCatalogoVehiculo;
	@Wire
	private Textbox txtPlacaChuto;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;

	Botonera botonera;
	Catalogo<Vehiculo> catalogo;
	String id = "";
	private List<Vehiculo> listaGeneral = new ArrayList<Vehiculo>();

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
		txtDescripcion.setFocus(true);
		mostrarCatalogo();

		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Vehiculo tipo = catalogo
								.objetoSeleccionadoDelCatalogo();
						id = tipo.getPlaca();
						txtDescripcion.setValue(tipo.getDescripcion());
						txtPlacaChuto.setValue(tipo.getPlacaChuto());
						txtPlaca.setValue(tipo.getPlaca());
						txtPlaca.setDisabled(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVehiculo, cerrar, tabs);

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
						msj.mensajeError(Mensaje.placaUsada);
					else {
						String descripcion = txtDescripcion.getValue();
						id = txtPlaca.getValue();
						Vehiculo vehiculo = new Vehiculo();
						vehiculo.setDescripcion(descripcion);
						vehiculo.setPlaca(id);
						vehiculo.setPlacaChuto(txtPlacaChuto.getValue());
						vehiculo.setUsuarioAuditoria(nombreUsuarioSesion());
						vehiculo.setFechaAuditoria(fechaHora);
						vehiculo.setHoraAuditoria(horaAuditoria);
						servicioVehiculo.guardar(vehiculo);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();
						listaGeneral = servicioVehiculo.buscarTodos();
						catalogo.actualizarLista(listaGeneral, true);
						abrirCatalogo();
					}
				}
			}

			@Override
			public void eliminar() {

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
		botoneraVehiculo.appendChild(botonera);

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
		txtDescripcion.setValue("");
		txtPlaca.setValue("");
		txtPlaca.setDisabled(false);
		txtPlacaChuto.setValue("");
	}

	public boolean validarSeleccion() {
		List<Vehiculo> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtDescripcion.getText().compareTo("") == 0
				|| txtPlaca.getText().compareTo("") == 0
				|| txtPlacaChuto.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtDescripcion.getText().compareTo("") != 0
				|| txtPlaca.getText().compareTo("") != 0
				|| txtPlacaChuto.getText().compareTo("") != 0) {
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
		listaGeneral = servicioVehiculo.buscarTodos();
		catalogo = new Catalogo<Vehiculo>(divCatalogoVehiculo,
				"Catalogo de Vehiculos", listaGeneral, false, false, false,
				"Placa", "Descripcion",  "Placa Chuto") {

			@Override
			protected List<Vehiculo> buscar(List<String> valores) {

				List<Vehiculo> lista = new ArrayList<Vehiculo>();

				for (Vehiculo tipo : listaGeneral) {
					if (tipo.getPlaca().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& tipo.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& tipo.getPlacaChuto().toLowerCase()
									.contains(valores.get(2).toLowerCase()))
						lista.add(tipo);
					}
				
				return lista;
			}

			@Override
			protected String[] crearRegistros(Vehiculo tipo) {
				String[] registros = new String[3];
				registros[0] = tipo.getPlaca();
				registros[1] = tipo.getDescripcion();
				registros[2] = tipo.getPlacaChuto();
				return registros;
			}
		};
		catalogo.setParent(divCatalogoVehiculo);
	}

	public boolean idLibre() {
		if (servicioVehiculo.buscar(txtPlaca.getValue()) != null)
			return false;
		else
			return true;

	}

}