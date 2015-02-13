package modelo.maestros;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import javax.persistence.Table;


/**
 * The persistent class for the tipo_cliente database table.
 * 
 */
@Entity
@Table(name="conductor")
@NamedQuery(name="Conductor.findAll", query="SELECT t FROM Conductor t")
public class Conductor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String cedula;

	private String nombres;
	
	private String apellidos;

	public Conductor() {
	}


	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}


	public String getCedula() {
		return cedula;
	}


	public void setCedula(String cedula) {
		this.cedula = cedula;
	}




}
