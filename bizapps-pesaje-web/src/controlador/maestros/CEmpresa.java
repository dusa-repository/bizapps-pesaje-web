package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Empresa;

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
import componente.Validador;

public class CEmpresa extends CGenerico {

	private static final long serialVersionUID = -6868106910332150746L;
	@Wire
	private Textbox txtRif;
	@Wire
	private Div divEmpresa;
	@Wire
	private Div botoneraEmpresa;
	@Wire
	private Div divCatalogoEmpresa;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	@Wire
	private Textbox txtNombre;
	@Wire
	private Textbox txtTelefono;
	@Wire
	private Textbox txtDireccion;

	Botonera botonera;
	Catalogo<Empresa> catalogo;
	String id = "";
	private List<Empresa> listaGeneral = new ArrayList<Empresa>();

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
		txtRif.setFocus(true);
		mostrarCatalogo();

		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Empresa tipo = catalogo.objetoSeleccionadoDelCatalogo();
						id = tipo.getRif();
						txtRif.setValue(tipo.getRif());
						txtNombre.setValue(tipo.getNombre());
						txtDireccion.setValue(tipo.getDireccion());
						txtTelefono.setValue(tipo.getTelefono());
						txtRif.setDisabled(true);
						
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divEmpresa, cerrar, tabs);

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
					if (buscarPorLogin()) {
						Empresa empresa = new Empresa();
						empresa.setRif(txtRif.getValue());
						empresa.setNombre(txtNombre.getValue());
						empresa.setTelefono(txtTelefono.getValue());
						empresa.setDireccion(txtDireccion.getValue());
						empresa.setUsuarioAuditoria(nombreUsuarioSesion());
						empresa.setHoraAuditoria(metodoHora());
						empresa.setFechaAuditoria(metodoFecha());
						servicioEmpresa.guardar(empresa);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();
						listaGeneral = servicioEmpresa.buscarTodos();
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
		botoneraEmpresa.appendChild(botonera);

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
		txtRif.setValue("");
		txtNombre.setValue("");
		txtDireccion.setValue("");
		txtTelefono.setValue("");
		txtRif.setDisabled(false);
	}

	public boolean validarSeleccion() {
		List<Empresa> seleccionados = catalogo.obtenerSeleccionados();
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
			if (txtTelefono.getText().compareTo("") != 0
					&& !Validador.validarTelefono(txtTelefono.getValue())) {
				msj.mensajeAlerta(Mensaje.telefonoInvalido);
				return false;
			} else
				return true;
		}
	}

	public boolean camposLLenos() {
		if (txtRif.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtRif.getText().compareTo("") != 0) {
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
		listaGeneral = servicioEmpresa.buscarTodos();
		catalogo = new Catalogo<Empresa>(divCatalogoEmpresa,
				"Catalogo de Empresas", listaGeneral, false, false, false,
				"Rif", "Nombre", "Direccion") {

			@Override
			protected List<Empresa> buscar(List<String> valores) {

				List<Empresa> lista = new ArrayList<Empresa>();

				for (Empresa tipo : listaGeneral) {
					if (tipo.getRif().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& tipo.getNombre().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& tipo.getDireccion().toLowerCase()
									.contains(valores.get(2).toLowerCase())) {
						lista.add(tipo);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(Empresa tipo) {
				String[] registros = new String[3];
				registros[0] = tipo.getRif();
				registros[1] = tipo.getNombre();
				registros[2] = tipo.getDireccion();
				return registros;
			}
		};
		catalogo.setParent(divCatalogoEmpresa);
	}

	@Listen("onChange = #txtTelefono")
	public void validarTelefono() {
		if (txtTelefono.getText().compareTo("") != 0
				&& !Validador.validarTelefono(txtTelefono.getValue())) {
			msj.mensajeAlerta(Mensaje.telefonoInvalido);
		}
	}

	@Listen("onChange = #txtRif")
	public boolean buscarPorLogin() {
		Empresa usuario = servicioEmpresa.buscar(txtRif.getValue());
		if (usuario == null)
			return true;
		else {
			if (usuario.getRif() == id)
				return true;
			else {
				msj.mensajeAlerta("El rif fue usado por otro registro");
				txtRif.setValue("");
				txtRif.setFocus(true);
				return false;
			}
		}
	}
}
