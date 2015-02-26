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
@Table(name="balanza")
@NamedQuery(name="Balanza.findAll", query="SELECT t FROM Balanza t")
public class Balanza implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_balanza")
	private long idBalanza;

	private String descripcion;

	public Balanza() {
	}

	public long getIdBalanza() {
		return idBalanza;
	}

	public void setIdBalanza(long idBalanza) {
		this.idBalanza = idBalanza;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
}
