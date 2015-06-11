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
@Table(name="empresa")
@NamedQuery(name="Empresa.findAll", query="SELECT t FROM Empresa t")
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "rif")
	private String rif;

	@Column(length = 500)
	private String nombre;
	
	@Column(length = 500)
	private String direccion;
	
	@Column(length = 50)
	private String telefono;

	@Column(name = "fecha_auditoria")
	private Timestamp fechaAuditoria;

	@Column(name = "hora_auditoria", length = 10)
	private String horaAuditoria;

	@Column(name = "usuario_auditoria", length = 50)
	private String usuarioAuditoria;

	public Empresa() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Empresa(String rif, String nombre, String direccion, String telefono,
			Timestamp fechaAuditoria, String horaAuditoria,
			String usuarioAuditoria) {
		super();
		this.rif = rif;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.fechaAuditoria = fechaAuditoria;
		this.horaAuditoria = horaAuditoria;
		this.usuarioAuditoria = usuarioAuditoria;
	}

	public String getRif() {
		return rif;
	}

	public void setRif(String rif) {
		this.rif = rif;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
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

	public String getUsuarioAuditoria() {
		return usuarioAuditoria;
	}

	public void setUsuarioAuditoria(String usuarioAuditoria) {
		this.usuarioAuditoria = usuarioAuditoria;
	}
	
	

}
