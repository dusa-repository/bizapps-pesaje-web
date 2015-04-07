package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Ciudad;
import modelo.maestros.Estado;

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

public class CCiudad extends CGenerico {

	private static final long serialVersionUID = -6868106910332150746L;

	@Wire
	private Textbox txtNombreCiudad;
	@Wire
	private Div botoneraCiudad;
	@Wire
	private Div catalogoCiudad;
	@Wire
	private Div divCiudad;
	@Wire
	private Combobox cmbEstado;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;

	Botonera botonera;
	Catalogo<Ciudad> catalogo;
	long id = 0;
	private List<Ciudad> listaGeneral = new ArrayList<Ciudad>();

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
		txtNombreCiudad.setFocus(true);
		mostrarCatalogo();
		llenarCombo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Ciudad tipo = catalogo.objetoSeleccionadoDelCatalogo();
						id = tipo.getIdCiudad();
						txtNombreCiudad.setValue(tipo.getNombre());
						Estado estado = null;
						if (tipo.getEstado() != null)
							cmbEstado.setValue(tipo.getEstado().getNombre());
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divCiudad, cerrar, tabs);

			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
			}

			@Override
			public void guardar() {
				if (validar()) {
					String nombre = txtNombreCiudad.getValue();
					long idEstado=0;
					if(cmbEstado.getSelectedItem()
							.getContext()!=null)
					idEstado = Long.valueOf(cmbEstado.getSelectedItem()
							.getContext());
					Estado estado = servicioEstado.buscar(idEstado);
					Ciudad ciudad = new Ciudad(id, fechaHora, horaAuditoria,
							nombre, nombreUsuarioSesion(), estado);
					servicioCiudad.guardar(ciudad);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					listaGeneral = servicioCiudad.buscarTodas();
					catalogo.actualizarLista(listaGeneral, true);
					abrirCatalogo();
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
		botoneraCiudad.appendChild(botonera);

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
		txtNombreCiudad.setText("");
		cmbEstado.setValue("");
		cmbEstado.setPlaceholder("Seleccione un Estado");
		id = 0;
	}

	public boolean validarSeleccion() {
		List<Ciudad> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtNombreCiudad.getText().compareTo("") == 0 || cmbEstado.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtNombreCiudad.getText().compareTo("") != 0 || cmbEstado.getText().compareTo("") != 0) {
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
		listaGeneral = servicioCiudad.buscarTodas();
		catalogo = new Catalogo<Ciudad>(catalogoCiudad,
				"Catalogo de Ciudades", listaGeneral, false, false, false,
				"Nombre") {

			@Override
			protected List<Ciudad> buscar(List<String> valores) {

				List<Ciudad> listaa = new ArrayList<Ciudad>();

				for (Ciudad tipo : listaGeneral) {
					if (tipo.getNombre().toLowerCase()
							.contains(valores.get(0).toLowerCase())) {
						listaa.add(tipo);
					}
				}
				return listaa;
			}

			@Override
			protected String[] crearRegistros(Ciudad tipo) {
				String[] registros = new String[1];
				registros[0] = tipo.getNombre();
				return registros;
			}
		};
		catalogo.setParent(catalogoCiudad);
	}
	@Listen("onOpen = #cmbEstado")
	public void llenarCombo() {
		List<Estado> estados = servicioEstado.buscarTodos();
		cmbEstado.setModel(new ListModelList<Estado>(estados));
	}
}