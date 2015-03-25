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
	
	@Column(length = 500)
	private String equipo;
	
	@Column(length = 500)
	private String ip;
	
	@Column(length = 500)
	private String puerto;
	
	@Column(length = 500)
	private String modelo;
	
	@Column(length = 500)
	private String baudrate;
	
	@Column(length = 500)
	private String databits;
	
	@Column(length = 500)
	private String stopbits;
	
	@Column(length = 500)
	private String paritynone;
	
	@Column(length = 500)
	private String clasificacion;
	

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

	public String getEquipo() {
		return equipo;
	}

	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPuerto() {
		return puerto;
	}

	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getBaudrate() {
		return baudrate;
	}

	public void setBaudrate(String baudrate) {
		this.baudrate = baudrate;
	}

	public String getDatabits() {
		return databits;
	}

	public void setDatabits(String databits) {
		this.databits = databits;
	}

	public String getStopbits() {
		return stopbits;
	}

	public void setStopbits(String stopbits) {
		this.stopbits = stopbits;
	}

	public String getParitynone() {
		return paritynone;
	}

	public void setParitynone(String paritynone) {
		this.paritynone = paritynone;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	
}
