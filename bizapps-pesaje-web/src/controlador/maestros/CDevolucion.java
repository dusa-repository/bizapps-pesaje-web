package controlador.maestros;

import java.io.IOException;
import java.util.HashMap;

import modelo.transacciones.Devolucion;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componente.Botonera;
import componente.Mensaje;

public class CDevolucion extends CGenerico {

	@Wire
	private Textbox txtDescripcion;
	@Wire
	private Textbox txtBoleto;
	@Wire
	private Div divDevolucion;
	@Wire
	private Div botoneraDevolucion;
	@Wire
	private Window wdwDevolucion;

	CPesaje cp = new CPesaje();

	@Override
	public void inicializar() throws IOException {

		HashMap<String, Object> mapa = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("devolucion");
		if (mapa != null) {
			if (mapa.get("boleto") != null) {

				String boleto = (String) mapa.get("boleto");
				txtBoleto.setValue(boleto);

				if (mapa.get("c") != null)
					cp = (CPesaje) mapa.get("c");
				mapa.clear();
				mapa = null;
			}
		}

		// cp.limpiarCampos();
		txtBoleto.setReadonly(true);

		Botonera botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void salir() {
				wdwDevolucion.onClose();

			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub

			}

			@Override
			public void limpiar() {
				txtBoleto.setValue("");
				txtDescripcion.setValue("");
				// txtBoleto.setDisabled(true);

			}

			@Override
			public void guardar() {
				if (validar()) {
					Devolucion devolucion = new Devolucion();
					devolucion.setBoleto(txtBoleto.getValue());
					devolucion.setDescripcion(txtDescripcion.getValue());
					devolucion.setIdDevolucion((long) 0);
					servicioDevolucion.guardar(devolucion);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					salir();
					if (cp != null) {
						cp.limpiarCampos();
						cp.editable(false);
					}
				}

			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void buscar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void ayuda() {
				// TODO Auto-generated method stub

			}

			@Override
			public void annadir() {
				// TODO Auto-generated method stub

			}
		};

		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(5).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botoneraDevolucion.appendChild(botonera);

	}

	protected boolean validar() {
		if (txtDescripcion.getText().compareTo("") == 0) {
			msj.mensajeError(componente.Mensaje.camposVacios);
			return false;
		} else
			return true;
	}

}
