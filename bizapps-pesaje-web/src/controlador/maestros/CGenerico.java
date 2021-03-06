package controlador.maestros;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import modelo.seguridad.Usuario;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Tab;

import security.modelo.Grupo;
import security.modelo.UsuarioSeguridad;
import security.servicio.SArbol;
import security.servicio.SGrupo;
import security.servicio.SUsuarioSeguridad;
import servicio.maestros.SAlmacen;
import servicio.maestros.SBalanza;
import servicio.maestros.SCiudad;
import servicio.maestros.SCliente;
import servicio.maestros.SConductor;
import servicio.maestros.SEmpresa;
import servicio.maestros.SEstado;
import servicio.maestros.SF0004;
import servicio.maestros.SF0005;
import servicio.maestros.SFlete;
import servicio.maestros.SPais;
import servicio.maestros.SProducto;
import servicio.maestros.SProveedor;
import servicio.maestros.STransporte;
import servicio.maestros.SVehiculo;
import servicio.seguridad.SUsuario;
import servicio.transacciones.SDevolucion;
import servicio.transacciones.SPesaje;

import componente.Mensaje;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public abstract class CGenerico extends SelectorComposer<Component> {

	private static final long serialVersionUID = -3701148488846104476L;

	@WireVariable("SArbol")
	protected SArbol servicioArbol;
	@WireVariable("SGrupo")
	protected SGrupo servicioGrupo;
	@WireVariable("SUsuario")
	protected SUsuario servicioUsuario;
	@WireVariable("SUsuarioSeguridad")
	protected SUsuarioSeguridad servicioUsuarioSeguridad;
	@WireVariable("SF0004")
	protected SF0004 servicioF0004;
	@WireVariable("SF0005")
	protected SF0005 servicioF0005;
	@WireVariable("SAlmacen")
	protected SAlmacen servicioAlmacen;
	@WireVariable("STransporte")
	protected STransporte servicioTransporte;
	@WireVariable("SFlete")
	protected SFlete servicioFlete;
	@WireVariable("SBalanza")
	protected SBalanza servicioBalanza;
	@WireVariable("SProducto")
	protected SProducto servicioProducto;
	@WireVariable("SCliente")
	protected SCliente servicioCliente;
	@WireVariable("SProveedor")
	protected SProveedor servicioProveedor;
	@WireVariable("SVehiculo")
	protected SVehiculo servicioVehiculo;
	@WireVariable("SConductor")
	protected SConductor servicioConductor;
	@WireVariable("SPesaje")
	protected SPesaje servicioPesaje;
	@WireVariable("SEmpresa")
	protected SEmpresa servicioEmpresa;
	@WireVariable("SDevolucion")
	protected SDevolucion servicioDevolucion;
	@WireVariable("SPais")
	protected SPais servicioPais;
	@WireVariable("SCiudad")
	protected SCiudad servicioCiudad;
	@WireVariable("SEstado")
	protected SEstado servicioEstado;
	
	private static ApplicationContext app = new ClassPathXmlApplicationContext(
			"/META-INF/ConfiguracionAplicacion.xml");

	protected static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	protected static SimpleDateFormat formatoFechaRara = new SimpleDateFormat(
			"yyyyMMdd");
	public List<Tab> tabs = new ArrayList<Tab>();
	public Groupbox grxGraficoGeneral;
	protected DateFormat df = new SimpleDateFormat("HH:mm:ss");
	public Calendar calendario = Calendar.getInstance();
	// Cambio en la hora borrados los :
	public String horaAuditoria = String.valueOf(calendario
			.get(Calendar.HOUR_OF_DAY))
			+ String.valueOf(calendario.get(Calendar.MINUTE))
			+ String.valueOf(calendario.get(Calendar.SECOND));
	public java.util.Date fecha = new Date();
	public Timestamp fechaHora = new Timestamp(fecha.getTime());
	public Mensaje msj = new Mensaje();
	public String cerrar;
	public Time tiempo = new Time(fecha.getTime());
	private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
			"/META-INF/PropiedadesBaseDatos.xml");

	public Timestamp metodoFecha() {
		fecha = new Date();
		return fechaHora = new Timestamp(fecha.getTime());
	}

	public String metodoHora() {
		fecha = new Date();
		calendario.setTime(fecha);
		return String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)) + ":"
				+ String.valueOf(calendario.get(Calendar.MINUTE)) + ":"
				+ String.valueOf(calendario.get(Calendar.SECOND));
	}

	public Usuario usuarioSesion(String valor) {
		return servicioUsuario.buscarUsuarioPorNombre(valor);
	}
	public static SPesaje getServicioPesaje() {
		return app.getBean(SPesaje.class);
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		inicializar();
	}

	public BigDecimal transformarGregorianoAJulia(Date fecha) {
		String valor = "";

		calendario = new GregorianCalendar();
		calendario.setTime(fecha);
		String dia = "";
		if (calendario.get(Calendar.DAY_OF_YEAR) < 10)
			dia = "00";
		else {
			if (calendario.get(Calendar.DAY_OF_YEAR) >= 10
					&& calendario.get(Calendar.DAY_OF_YEAR) < 100)
				dia = "0";
		}
		if ((fecha.getYear() + 1900) < 2000)
			valor = "";
		else
			valor = "1";
		long al = Long.valueOf(valor
				+ String.valueOf(calendario.get(Calendar.YEAR)).substring(2)
				+ dia + String.valueOf(calendario.get(Calendar.DAY_OF_YEAR)));
		BigDecimal a = BigDecimal.valueOf(al);
		return a;
	}

	public Date transformarJulianaAGregoria(BigDecimal valor) {
		String j = valor.toString();
		Date date = new Date();
		String primerValor = "";
		if (j.length() == 5) {
			try {
				date = new SimpleDateFormat("yyD").parse(j);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			primerValor = j.substring(0, 1);
			if (primerValor.equals("1")) {
				String anno = j.substring(1, 3);
				date.setYear(Integer.valueOf("20" + anno) - 1900);
				String s = j.substring(3);
				Date fecha = new Date();
				try {
					fecha = new SimpleDateFormat("D").parse(s);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fecha.setYear(date.getYear());
				return fecha;
			}
		}

		return date;

	}

	public Date transformarJulianaAGregoriadeLong(Long valor) {
		String j = valor.toString();
		Date date = new Date();
		String primerValor = "";
		if (j.length() == 5) {
			try {
				date = new SimpleDateFormat("yyD").parse(j);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			primerValor = j.substring(0, 1);
			if (primerValor.equals("1")) {
				String anno = j.substring(1, 3);
				date.setYear(Integer.valueOf("20" + anno) - 1900);
				String s = j.substring(3);
				Date fecha = new Date();
				try {
					fecha = new SimpleDateFormat("D").parse(s);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fecha.setYear(date.getYear());
				return fecha;
			}
		}

		return date;

	}

	public abstract void inicializar() throws IOException;

	public void cerrarVentana(Div div, String id, List<Tab> tabs2) {
		div.setVisible(false);
		tabs = tabs2;
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getLabel().equals(id)) {
				if (i == (tabs.size() - 1) && tabs.size() > 1) {
					tabs.get(i - 1).setSelected(true);
				}
				tabs.get(i).onClose();
				tabs.remove(i);
			}
		}
	}

	public String nombreUsuarioSesion() {
		Authentication sesion = SecurityContextHolder.getContext()
				.getAuthentication();
		return sesion.getName();
	}

	public boolean enviarEmailNotificacion(String correo, String mensajes) {
		try {

			String cc = "NOTIFICACION DEL SISTEMA DE PESAJE";
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "172.23.20.66");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", "2525");
			props.setProperty("mail.smtp.auth", "true");

			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getInstance(props, auth);
			String remitente = "cdusa@dusa.com.ve";
			String destino = correo;
			String mensaje = mensajes;
			String destinos[] = destino.split(",");
			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(remitente));

			Address[] receptores = new Address[destinos.length];
			int j = 0;
			while (j < destinos.length) {
				receptores[j] = new InternetAddress(destinos[j]);
				j++;
			}

			message.addRecipients(Message.RecipientType.TO, receptores);
			message.setSubject(cc);
			message.setText(mensaje);

			Transport.send(message);

			return true;
		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Date traerFech(String fechaString) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

		Date fecha = null;
		try {
			fecha = formatter.parse(fechaString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fecha;
	}

	public String damePath() {
		return Executions.getCurrent().getContextPath() + "/";
	}

	public List<String> obtenerPropiedades() {
		List<String> arreglo = new ArrayList<String>();
		DriverManagerDataSource ds = (DriverManagerDataSource) applicationContext
				.getBean("dataSource");
		arreglo.add(ds.getUsername());
		arreglo.add(ds.getPassword());
		arreglo.add(ds.getUrl());
		return arreglo;
	}

	class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication("cdusa", "cartucho");
		}
	}

	public int obtenerDiasHabiles(Date fecha, Date fecha2) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		calendario.setTime(fecha2);
		calendario.add(Calendar.DAY_OF_YEAR, +1);
		calendario.set(Calendar.HOUR, 0);
		calendario.set(Calendar.HOUR_OF_DAY, 0);
		calendario.set(Calendar.SECOND, 0);
		calendario.set(Calendar.MILLISECOND, 0);
		calendario.set(Calendar.MINUTE, 0);
		fecha2 = calendario.getTime();
		calendario.setTime(fecha);
		String fija = formatoFecha.format(fecha2);
		String hoy = "";
		int contador = 0;
		do {
			calendario.setTime(fecha);
			if (calendario.get(Calendar.DAY_OF_WEEK) != 1
					&& calendario.get(Calendar.DAY_OF_WEEK) != 7)
				contador++;
			calendario.add(Calendar.DAY_OF_YEAR, +1);
			fecha = calendario.getTime();
			hoy = formatoFecha.format(fecha);
		} while (!hoy.equals(fija));
		return contador;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		Double valor = value;
		BigDecimal bd = new BigDecimal(value);
		if (!valor.isNaN() && !valor.isInfinite()) {
			bd = new BigDecimal(value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
		}
		return bd.doubleValue();
	}
	

	public Date agregarDia(Date fecha) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fecha);
		calendario.add(Calendar.DAY_OF_YEAR, +1);
		return fecha = calendario.getTime();
	}
	

	public byte[] generarReporteGenerico(Map<String, Object> p, List<?> lista,
			String nombreReporte, String tipo) {
		Mensaje mensaje = new Mensaje();
		byte[] fichero = null;
		JasperReport repor = null;
		try {
			repor = (JasperReport) JRLoader.loadObject(getClass().getResource(
					nombreReporte));
		} catch (JRException e1) {
			e1.printStackTrace();
		}

		if (tipo.equals("EXCEL")) {
			JasperPrint jasperPrint = null;
			try {
				jasperPrint = JasperFillManager.fillReport(repor, p,
						new JRBeanCollectionDataSource(lista));
			} catch (JRException e) {
				e.printStackTrace();
			}
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			try {
				exporter.exportReport();
			} catch (JRException e) {
				e.printStackTrace();
			}
			return xlsReport.toByteArray();
		} else {
			try {
				fichero = JasperRunManager.runReportToPdf(repor, p,
						new JRBeanCollectionDataSource(lista));
			} catch (JRException e) {
				e.printStackTrace();
				mensaje.mensajeError(e.getMessage());
			}
			return fichero;
		}
	}


	public void guardarDatosSeguridad(Usuario usuarioLogica,
			Set<Grupo> gruposUsuario) {
		UsuarioSeguridad usuario = new UsuarioSeguridad(
				usuarioLogica.getLogin(), usuarioLogica.getEmail(),
				usuarioLogica.getPassword(), usuarioLogica.getImagen(), true,
				usuarioLogica.getPrimerNombre(),
				usuarioLogica.getPrimerApellido(), fechaHora, horaAuditoria,
				nombreUsuarioSesion(), gruposUsuario);
		servicioUsuarioSeguridad.guardar(usuario);
	}

	public void inhabilitarSeguridad(List<Usuario> list) {
		for (int i = 0; i < list.size(); i++) {
			UsuarioSeguridad usuario = servicioUsuarioSeguridad
					.buscarPorLogin(list.get(i).getLogin());
			usuario.setEstado(false);
			servicioUsuarioSeguridad.guardar(usuario);
			list.get(i).setEstado(false);
			servicioUsuario.guardar(list.get(i));
		}
	}

}