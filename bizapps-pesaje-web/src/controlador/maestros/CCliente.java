package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Ciudad;
import modelo.maestros.Cliente;

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

public class CCliente extends CGenerico {

	private static final long serialVersionUID = -6868106910332150746L;
	@Wire
	private Textbox txtDescripcion;
	@Wire
	private Textbox txtCodigo;
	@Wire
	private Div divCliente;
	@Wire
	private Div botoneraCliente;
	@Wire
	private Div divCatalogoCliente;
	@Wire
	private Combobox cmbCiudad;
	@Wire
	private Textbox txtDireccion;
	@Wire
	private Textbox txtTelefono1;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;

	Botonera botonera;
	Catalogo<Cliente> catalogo;
	String id = "";
	private List<Cliente> listaGeneral = new ArrayList<Cliente>();

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
						Cliente tipo = catalogo.objetoSeleccionadoDelCatalogo();
						id = tipo.getIdCliente();
						txtDescripcion.setValue(tipo.getDescripcion());
						txtCodigo.setValue(tipo.getIdCliente());
						txtCodigo.setDisabled(true);
						txtDireccion.setValue(tipo.getDireccion());
						txtTelefono1.setValue(tipo.getTelefono());
						if (tipo.getCiudad() != null)
							cmbCiudad.setValue(tipo.getCiudad().getNombre());
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divCliente, cerrar, tabs);

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
						msj.mensajeError(Mensaje.codigoUsado);
					else {
						Ciudad ciudad = null;
						if (cmbCiudad.getSelectedItem() != null)
							ciudad = servicioCiudad.buscar(Long
									.parseLong(cmbCiudad.getSelectedItem()
											.getContext()));
						String descripcion = txtDescripcion.getValue();
						id = txtCodigo.getValue();
						Cliente cliente = new Cliente();
						cliente.setDescripcion(descripcion);
						cliente.setIdCliente(id);
						cliente.setDireccion(txtDireccion.getValue());
						cliente.setTelefono(txtTelefono1.getValue());
						cliente.setCiudad(ciudad);
						cliente.setUsuarioAuditoria(nombreUsuarioSesion());
						cliente.setFechaAuditoria(fechaHora);
						cliente.setHoraAuditoria(horaAuditoria);
						servicioCliente.guardar(cliente);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();
						listaGeneral = servicioCliente.buscarTodos();
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
		botoneraCliente.appendChild(botonera);

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
		txtCodigo.setValue("");
		txtCodigo.setDisabled(false);
		txtDireccion.setValue("");
		txtTelefono1.setValue("");
		cmbCiudad.setValue("");
	}

	public boolean validarSeleccion() {
		List<Cliente> seleccionados = catalogo.obtenerSeleccionados();
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
			} else
				return true;
		}
	}

	public boolean camposLLenos() {
		if (txtDescripcion.getText().compareTo("") == 0
				|| txtCodigo.getText().compareTo("") == 0
				|| txtDireccion.getText().compareTo("") == 0
				|| txtTelefono1.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtDescripcion.getText().compareTo("") != 0
				|| txtCodigo.getText().compareTo("") != 0
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
		listaGeneral = servicioCliente.buscarTodos();
		catalogo = new Catalogo<Cliente>(divCatalogoCliente,
				"Catalogo de Clientes", listaGeneral, false, false, false,
				"Codigo", "Descripcion", "Direccion", "Telefono") {

			@Override
			protected List<Cliente> buscar(List<String> valores) {

				List<Cliente> lista = new ArrayList<Cliente>();

				for (Cliente tipo : listaGeneral) {
					if (tipo.getIdCliente().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& tipo.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& tipo.getDireccion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& tipo.getTelefono().toLowerCase()
									.contains(valores.get(1).toLowerCase())) {
						lista.add(tipo);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(Cliente tipo) {
				String[] registros = new String[4];
				registros[0] = tipo.getIdCliente();
				registros[1] = tipo.getDescripcion();
				registros[2] = tipo.getDireccion();
				registros[3] = tipo.getTelefono();
				return registros;
			}
		};
		catalogo.setParent(divCatalogoCliente);
	}

	public boolean idLibre() {
		if (servicioCliente.buscar(txtCodigo.getValue()) != null)
			return false;
		else
			return true;
	}

	@Listen("onOpen = #cmbCiudad")
	public void llenarComboCiudad() {
		List<Ciudad> ciudades = servicioCiudad.buscarTodas();
		cmbCiudad.setModel(new ListModelList<Ciudad>(ciudades));
	}

	/* Metodo que valida el formmato del telefono ingresado */
	@Listen("onChange = #txtTelefono1")
	public void validarTelefono() throws IOException {
		if (Validador.validarTelefono(txtTelefono1.getValue()) == false) {
			msj.mensajeAlerta(Mensaje.telefonoInvalido);
		}
	}
}