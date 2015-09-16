package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Ciudad;
import modelo.maestros.Estado;
import modelo.maestros.Pais;

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

public class CEstado extends CGenerico {

	private static final long serialVersionUID = -6868106910332150746L;
	@Wire
	private Div catalogoEstado;
	@Wire
	private Div divEstado;
	@Wire
	private Div botoneraEstado;
	@Wire
	private Textbox txtNombreEstado;
	@Wire
	private Combobox cmbPais;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;

	Botonera botonera;
	Catalogo<Estado> catalogo;
	long id = 0;
	private List<Estado> listaGeneral = new ArrayList<Estado>();

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
		llenarCombo();
		txtNombreEstado.setFocus(true);
		mostrarCatalogo();

		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Estado tipo = catalogo.objetoSeleccionadoDelCatalogo();
						id = tipo.getIdEstado();
						txtNombreEstado.setValue(tipo.getNombre());
						if (tipo.getPais() != null)
							cmbPais.setValue(tipo.getPais().getNombre());
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divEstado, cerrar, tabs);

			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
				id = 0;
			}

			@Override
			public void guardar() {
				if (validar()) {
					String nombre = txtNombreEstado.getValue();
					Pais pais = null;
					if (cmbPais.getSelectedItem().getContext() != null)
						pais = servicioPais.buscar(Long.parseLong(cmbPais
								.getSelectedItem().getContext()));
					Estado estado = new Estado(id, fechaHora, horaAuditoria,
							nombre, nombreUsuarioSesion(), pais);
					servicioEstado.guardar(estado);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					listaGeneral = servicioEstado.buscarTodos();
					catalogo.actualizarLista(listaGeneral, true);
					abrirCatalogo();
				}
			}

			@Override
			public void eliminar() {

				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Estado> eliminarLista = catalogo
								.obtenerSeleccionados();
						List<Ciudad> ciudades = servicioCiudad
								.buscarPorEstado(eliminarLista);
						if (ciudades.isEmpty()) {
						Messagebox
								.show("¿Desea Eliminar los "
										+ eliminarLista.size() + " Registros?",
										"Alerta",
										Messagebox.OK | Messagebox.CANCEL,
										Messagebox.QUESTION,
										new org.zkoss.zk.ui.event.EventListener<Event>() {
											public void onEvent(Event evt)
													throws InterruptedException {
												if (evt.getName()
														.equals("onOK")) {
													servicioEstado
															.eliminarVarios(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(
															servicioEstado
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
					if (id != 0 && txtNombreEstado.getText().compareTo("") != 0) {
						final Estado estado = servicioEstado
								.buscar(id);
						List<Ciudad> ciudades = servicioCiudad
								.buscarPorEstado(estado);			
						if (ciudades.isEmpty())
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
													servicioEstado.eliminar(estado);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(
															servicioEstado
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
		botoneraEstado.appendChild(botonera);

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
		id = 0;
		txtNombreEstado.setValue("");
		cmbPais.setValue("");
	}

	public boolean validarSeleccion() {
		List<Estado> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtNombreEstado.getText().compareTo("") == 0 ||cmbPais.getText().compareTo("") == 0  ) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtNombreEstado.getText().compareTo("") != 0 ||cmbPais.getText().compareTo("") != 0  ) {
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
		listaGeneral = servicioEstado.buscarTodos();
		catalogo = new Catalogo<Estado>(catalogoEstado,
				"Catalogo de Estados", listaGeneral, false, false, false,
				"Nombre") {

			@Override
			protected List<Estado> buscar(List<String> valores) {

				List<Estado> listaa = new ArrayList<Estado>();

				for (Estado tipo : listaGeneral) {
					if (tipo.getNombre().toLowerCase()
							.contains(valores.get(0).toLowerCase())) {
						listaa.add(tipo);
					}
				}
				return listaa;
			}

			@Override
			protected String[] crearRegistros(Estado tipo) {
				String[] registros = new String[1];
				registros[0] = tipo.getNombre();
				return registros;
			}
		};
		catalogo.setParent(catalogoEstado);
	}

	@Listen("onOpen = #cmbPais")
	public void llenarCombo() {
		List<Pais> paises = servicioPais.buscarTodos();
		cmbPais.setModel(new ListModelList<Pais>(paises));
	}
}