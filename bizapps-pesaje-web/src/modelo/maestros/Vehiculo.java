package modelo.maestros;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tipo_cliente database table.
 * 
 */
@Entity
@Table(name="vehiculo")
@NamedQuery(name="Vehiculo.findAll", query="SELECT t FROM Vehiculo t")
public class Vehiculo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String placa;

	private String descripcion;

	@Column(name = "placa_chuto")
	private String placaChuto;
	
	@Column(name="usuario_auditoria", length=50)
	private String usuarioAuditoria;
	
	@Column(name="fecha_auditoria")
	private Timestamp fechaAuditoria;

	@Column(name="hora_auditoria", length=10)
	private String horaAuditoria;

	public Vehiculo() {
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getPlacaChuto() {
		return placaChuto;
	}

	public void setPlacaChuto(String placaChuto) {
		this.placaChuto = placaChuto;
	}


	public String getUsuarioAuditoria() {
		return usuarioAuditoria;
	}

	public void setUsuarioAuditoria(String usuarioAuditoria) {
		this.usuarioAuditoria = usuarioAuditoria;
	}

	public Timestamp getFechaAuditoria() {
		return fechaAuditoria;
	}

	public void setFechaAuditoria(Timestamp fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}

	public String getHoraAuditoria() {
		return horaAuditoria;
	}

	public void setHoraAuditoria(String horaAuditoria) {
		this.horaAuditoria = horaAuditoria;
	}




}