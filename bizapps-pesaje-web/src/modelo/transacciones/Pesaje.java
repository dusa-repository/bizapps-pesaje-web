package modelo.transacciones;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import modelo.maestros.Almacen;
import modelo.maestros.Balanza;
import modelo.maestros.Conductor;
import modelo.maestros.Producto;
import modelo.maestros.Transporte;
import modelo.maestros.Vehiculo;

@Entity
@Table(name="pesaje")
@NamedQuery(name="Pesaje.findAll", query="SELECT t FROM Pesaje t")
public class Pesaje implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "boleto", unique = true, nullable = false)
	private Long boleto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cedula_conductor")
	private Conductor conductor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_transporte")
	private Transporte transporte;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "placa_vehiculo")
	private Vehiculo vehiculo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_producto")
	private Producto producto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_almacen")
	private Almacen almacen;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_balanza")
	private Balanza balanza;
	
	@Column(name = "fecha_pesaje")
	private Timestamp fechaPesaje;
	
	@Column(name = "fecha_pesaje_salida")
	private Timestamp fechaPesajeSalida;

	@Column(name = "hora_pesaje", length = 10)
	private String horaPesaje;

	@Column(name = "entrada")
	private Double entrada;

	@Column(name = "salida")
	private Double salida;
	
	@Column(name = "pt_salida")
	private Double pesoPTSalida;

	@Column(length = 500)
	private String observacion;

	@Column(length = 100)
	private String estatus;

	@Column(name = "fecha_auditoria")
	private Timestamp fechaAuditoria;

	@Column(name = "hora_auditoria", length = 10)
	private String horaAuditoria;

	@Column(name = "usuario_auditoria", length = 50)
	private String usuarioAuditoria;
	
	@Column(length = 100)
	private String nroFactura;

	public Pesaje() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Pesaje(Long boleto, Conductor conductor, Transporte transporte,
			Vehiculo vehiculo, Producto producto, Almacen almacen,
			Timestamp fechaPesaje, String horaPesaje, Double entrada,
			Double salida, String observacion, String estatus,
			Timestamp fechaAuditoria, String horaAuditoria,
			String usuarioAuditoria) {
		super();
		this.boleto = boleto;
		this.conductor = conductor;
		this.transporte = transporte;
		this.vehiculo = vehiculo;
		this.producto = producto;
		this.almacen = almacen;
		this.fechaPesaje = fechaPesaje;
		this.horaPesaje = horaPesaje;
		this.entrada = entrada;
		this.salida = salida;
		this.observacion = observacion;
		this.estatus = estatus;
		this.fechaAuditoria = fechaAuditoria;
		this.horaAuditoria = horaAuditoria;
		this.usuarioAuditoria = usuarioAuditoria;
	}

	public Long getBoleto() {
		return boleto;
	}

	public void setBoleto(Long boleto) {
		this.boleto = boleto;
	}

	public Conductor getConductor() {
		return conductor;
	}

	public void setConductor(Conductor conductor) {
		this.conductor = conductor;
	}

	public Transporte getTransporte() {
		return transporte;
	}

	public void setTransporte(Transporte transporte) {
		this.transporte = transporte;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}

	public Timestamp getFechaPesaje() {
		return fechaPesaje;
	}

	public void setFechaPesaje(Timestamp fechaPesaje) {
		this.fechaPesaje = fechaPesaje;
	}

	public String getHoraPesaje() {
		return horaPesaje;
	}

	public void setHoraPesaje(String horaPesaje) {
		this.horaPesaje = horaPesaje;
	}

	public Double getEntrada() {
		return entrada;
	}

	public void setEntrada(Double entrada) {
		this.entrada = entrada;
	}

	public Double getSalida() {
		return salida;
	}

	public void setSalida(Double salida) {
		this.salida = salida;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
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

	public Balanza getBalanza() {
		return balanza;
	}

	public void setBalanza(Balanza balanza) {
		this.balanza = balanza;
	}

	public Timestamp getFechaPesajeSalida() {
		return fechaPesajeSalida;
	}

	public void setFechaPesajeSalida(Timestamp fechaPesajeSalida) {
		this.fechaPesajeSalida = fechaPesajeSalida;
	}

	public String getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	public Double getPesoPTSalida() {
		return pesoPTSalida;
	}

	public void setPesoPTSalida(Double pesoPTSalida) {
		this.pesoPTSalida = pesoPTSalida;
	}


}
