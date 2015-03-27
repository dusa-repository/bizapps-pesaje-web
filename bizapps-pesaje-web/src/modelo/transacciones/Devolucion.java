package modelo.transacciones;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="devolucion")
@NamedQuery(name="Devolucion.findAll", query="SELECT t FROM Devolucion t")
public class Devolucion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_devolucion")
	private Long idDevolucion;
	
	private String boleto;
	
	@Column(length = 500)
	private String descripcion;
	
	public Devolucion() {
	}

	public Long getIdDevolucion() {
		return idDevolucion;
	}

	public void setIdDevolucion(Long idDevolucion) {
		this.idDevolucion = idDevolucion;
	}

	public String getBoleto() {
		return boleto;
	}

	public void setBoleto(String boleto) {
		this.boleto = boleto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

}
