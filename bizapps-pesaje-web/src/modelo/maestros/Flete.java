package modelo.maestros;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import javax.persistence.Table;


/**
 * The persistent class for the tipo_cliente database table.
 * 
 */
@Entity
@Table(name="flete")
@NamedQuery(name="Flete.findAll", query="SELECT t FROM Flete t")
public class Flete implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_flete")
	private long idFlete;

	private String descripcion;

	public Flete() {
	}

	public long getIdFlete() {
		return idFlete;
	}

	public void setIdFlete(long idFlete) {
		this.idFlete = idFlete;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



}
