package fiuba.taller.actividad;

import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis2.AxisFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fiuba.taller.actividad.excepcion.FechaErroneaExcepcion;
import fiuba.taller.actividad.excepcion.XmlErroneoExcepcion;

//import com.ws.services.*;

@SuppressWarnings("unused")
public class Actividad implements Serializable {

	protected long id;
	protected long idAmbitoSuperior;
	protected long idActividadSuperior;
	protected String nombre;
	protected String tipo;
	protected String descripcion;
	protected long fechaInicio;
	protected long fechaFin;
	protected List<Long> coordinadores;
	protected long idMuro;
	protected long idCartelera;
	protected long idForo;
	protected List<Long> actividadesInternas;
	protected long idChat;

	/*
	 * Mapa donde se van a guardar provisoriamente las actividades hasta que lo
	 * de Integracion ande.
	 */
	protected static HashMap<Long, String> AuxHastaQIntegracionAnde = new HashMap<Long, String>();

	public Actividad() {
		id = -1;
		idAmbitoSuperior = -1;
		idActividadSuperior = -1;
		nombre = "";
		tipo = "";
		descripcion = "";
		fechaFin = -1;
		fechaInicio = -1;
	}

	public long getId() {
		return id;
	}

	public long getIdAmbitoSuperior() {
		return idAmbitoSuperior;
	}

	public void setIdAmbitoSuperior(long idAmbitoSuperior) {
		this.idAmbitoSuperior = idAmbitoSuperior;
	}

	public long getIdActividadSuperior() {
		return idActividadSuperior;
	}

	public void setIdActividadSuperior(long idActividadSuperior) {
		this.idActividadSuperior = idActividadSuperior;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public long getFechaInicio() {
		return fechaInicio;
	}

	public long getFechaFin() {
		return fechaFin;
	}

	public void setFecha(long fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setFecha(long fechaInicio, long fechaFin)
			throws FechaErroneaExcepcion {
		if (fechaInicio > fechaFin) {
			String mensaje = "La fecha de inicio no puede ser posterior a la "
					+ "fecha de fin";
			throw new FechaErroneaExcepcion(mensaje);
		}
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

	@Deprecated
	// FIXME De esto no se encarga Participacion???
	public List<String> getCoordinadores() {
		// TODO(Pampa) Implementar
		return new ArrayList<>();
	}

	@Deprecated
	// FIXME De esto no se encarga Participacion???
	public void agregarCoordinador(long idCoordinador) {
		/*
		 * TODO(Pampa) Implementar. Si el id ya existe, debe lanzar una
		 * excepcion.
		 */
	}

	@Deprecated
	// FIXME De esto no se encarga Participacion???
	public void eliminarCoordinador(long idCoordinador) {
		/*
		 * TODO(Pampa) Implementar. Si el id no existe, debe lanzar una
		 * excepcion. Si es el único coordinador, no se puede borrar. Lanzar una
		 * excepcion.
		 */
	}

	public long getIdMuro() {
		return idMuro;
	}

	public long getIdCartelera() {
		return idCartelera;
	}

	public long getIdForo() {
		return idForo;
	}

	public List<Actividad> getActividades() {
		// TODO(Pampa) Implementar
		return new ArrayList<>();
	}

	public long getIdChat() {
		return idChat;
	}

	// serializa a la actividad
	// devuelve xml
	// este metodo intenta ser util tanto para cuando:
	// -> se envian las propiedades de la actividad a presentacion
	// -> se guardan los datos a integracion y se envian los datos de consulta a
	// integracion
	@Override
	public String serializar() {
		return "<?xml version=\"1.0\"?><WS><Actividad>" + serializarInterno()
				+ "</Actividad></WS>";
	}

	@Override
	public void descerializar(String xml) throws XmlErroneoExcepcion {
		Document doc = getDocumentElement(xml);
		descerializar(doc);
	}

	/**
	 * Guarda el estado actual del objeto a la base de datos.
	 */
	public void guardarEstado() {
	/*	GuardarDatosResponse response = new GuardarDatosResponse();
		GuardarDatos envio = new GuardarDatos();
		envio.setXml(serializar());
		IntegracionStub servicio;
		try {
			servicio = new IntegracionStub();
			response = servicio.guardarDatos(envio);
		} catch (RemoteException e) { System.out.print("Ocurrio un Error en el metodo setNombre\n");
		 e.printStackTrace(); } System.out.print(response.get_return());*/
		Actividad.AuxHastaQIntegracionAnde.put(this.id, serializar());
	}

	/**
	 * A partir de los valores de los atributos internos de la instancia, genera
	 * un XML que es enviado a Integracion. Integracion devuelve un XML con
	 * todos los datos y este es devuelto. NOTA: integracion puede devolver mas
	 * de 1 XML según los valores de los parametros internos de la instancia.
	 */
	public String realizarConsulta() {
		// "No implementado todavia :)";
		// devuelve siempre lo mismo
		
		 String xml = serializar(); // TODO llamar a integrar y conseguir el
		/* xml completo String xmlDevuelto =
		 * "<?xml version=\"1.0\"?><WS><Actividad>" + "<Id>" + 45 + "</Id>" +
		 * "<IdAmbitoSuperior>" + 88 + "</IdAmbitoSuperior>" +
		 * "<IdActividadSuperior>" + 90 + "</IdActividadSuperior>" + "<Nombre>"
		 * + "pepe" + "</Nombre>" + "<Tipo>" + "ActividadIndividual" + "</Tipo>"
		 * + "<Descripcion>" + "esto es una descripcion" + "</Descripcion>" +
		 * "<FechaInicio>" + fechaInicio + "</FechaInicio>" + "<FechaFin>" +
		 * fechaFin + "</FechaFin>" + "</Actividad></WS>";
		 */
		return Actividad.AuxHastaQIntegracionAnde.get(this.id);
	}

	public void actualizar(String xml) throws XmlErroneoExcepcion {
		Actividad actividadTemporal = new Actividad();
		actividadTemporal.descerializar(xml);
		actualizar(actividadTemporal);
	}

	/* METODOS DE CLASE (ESTATICOS) */

	public static Actividad getActividad(long idActividad)
			throws XmlErroneoExcepcion {
		/*
		 * FIXME Si no existe la actividad con el ID especificado, se debe
		 * lanzar la excepcion ActividadInexistenteExcepcion
		 */
		Actividad actividad = new Actividad();
		actividad.levantarEstado(idActividad);
		return actividad;
	}

	/* METODOS PROTEGIDOS AUXILIARES */

	protected void setId(long id) {
		this.id = id;
	}

	protected void guardarNuevoElemento() {
/*		GuardarDatosResponse response = new GuardarDatosResponse();
		GuardarDatos envio = new GuardarDatos();
		envio.setXml(serializar());
		IntegracionStub servicio;
		try {
			servicio = new IntegracionStub();
			response = servicio.guardarDatos(envio);
		} catch (RemoteException e) { 
			System.out.print("Ocurrio un Error en el metodo setNombre\n");
			e.printStackTrace();
		} 
		System.out.print(response.get_return()); */
		Actividad.AuxHastaQIntegracionAnde.put(this.id, serializar());
	}

	protected void levantarEstado(long idActividad) throws XmlErroneoExcepcion {
		id = idActividad;
		descerializar(realizarConsulta());
	}

	protected String serializarInterno() {
		String identifStr = "";
		String idAmbSupStr = "";
		String idActSupStr = "";
		String fehcaFinStr = "";
		String fehcaIniStr = "";
		if (id >= 0) {
			identifStr = String.valueOf(id);
		}
		if (idAmbitoSuperior >= 0) {
			idAmbSupStr = String.valueOf(idAmbitoSuperior);
		}
		if (idActividadSuperior >= 0) {
			idActSupStr = String.valueOf(idActividadSuperior);
		}
		if (fechaFin >= 0) {
			fehcaFinStr = String.valueOf(fechaFin);
		}
		if (fechaInicio >= 0) {
			fehcaIniStr = String.valueOf(fechaInicio);
		}

		String xml = "<Id>" + identifStr + "</Id>"
				+ "<Nombre>" + nombre + "</Nombre>"
				+ "<Tipo>" + tipo + "</Tipo>"
				+ "<IdAmbitoSuperior>" + idAmbSupStr + "</IdAmbitoSuperior>"
				+ "<IdActividadSuperior>" + idActSupStr + "</IdActividadSuperior>"
				+ "<Descripcion>" + descripcion + "</Descripcion>"
				+ "<FechaInicio>" + fehcaIniStr + "</FechaInicio>"
				+ "<FechaFin>" + fehcaFinStr + "</FechaFin>";
		return xml;
	}

	protected void descerializar(Document doc) throws XmlErroneoExcepcion {
		NodeList nodes = doc.getElementsByTagName("Actividad");
		if (nodes.getLength() != 1) {
			throw new XmlErroneoExcepcion(
					"Debe haber solamente un nodo Actividad");
		}
		Element element = (Element) nodes.item(0);
		String idStr = getValue("Id", element);
		if (idStr.length() > 0)
			id = Long.valueOf(getValue("Id", element));
		String idAmbSupStr = getValue("IdAmbitoSuperior", element);
		if(idAmbSupStr.length() > 0)
			idAmbitoSuperior = Long.valueOf(idAmbSupStr);
		String idActSupStr = getValue("IdActividadSuperior", element);
		if(idActSupStr.length() > 0)
			idActividadSuperior = Long.valueOf(idActSupStr);
		nombre = getValue("Nombre", element);
		tipo = getValue("Tipo", element);
		descripcion = getValue("Descripcion", element);
		String fechaInicioStr = getValue("FechaInicio", element);
		if(fechaInicioStr.length() > 0)
			fechaInicio = Long.valueOf(fechaInicioStr);
		String fechaFinStr = getValue("FechaFin", element);
		if(fechaFinStr.length() > 0)
			fechaFin = Long.valueOf(fechaFinStr);
	}

	protected void actualizar(Actividad actividadConDatosNuevos)
			throws XmlErroneoExcepcion {
		if (actividadConDatosNuevos.getNombre().length() > 0) {
			setNombre(actividadConDatosNuevos.getNombre());
		}
		if (actividadConDatosNuevos.getDescripcion().length() > 0) {
			setDescripcion(actividadConDatosNuevos.getDescripcion());
		}
		if (actividadConDatosNuevos.getFechaInicio() > 0) {
			if (actividadConDatosNuevos.getFechaFin() > 0) {
				try {
					setFecha(actividadConDatosNuevos.getFechaInicio(),
							actividadConDatosNuevos.getFechaFin());
				} catch (FechaErroneaExcepcion e) {
					String mensaje = "La fecha de inicio no puede ser posterior "
							+ "a la fecha de fin.";
					throw new XmlErroneoExcepcion(mensaje);
				}
			} else {
				setFecha(actividadConDatosNuevos.getFechaInicio());
			}
		}
	}

	protected static Document getDocumentElement(String xml)
			throws XmlErroneoExcepcion {
		Document doc = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			doc = builder.parse(is);
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new XmlErroneoExcepcion("Error al parsear el XML.");
		}
		return doc;
	}

	// metodo interno de ayuda para el parseo
	protected static String getValue(String tag, Element element)
			throws XmlErroneoExcepcion {
		NodeList nodes = element.getElementsByTagName(tag);
		if (nodes.getLength() != 1) {
			String mensaje = "Debe existir un nodo " + tag + ".";
			throw new XmlErroneoExcepcion(mensaje);
		}
		Element elemento = (Element) nodes.item(0);
		return elemento.getTextContent();
	}

	/* METODOS PUBLICOS DE TESTING */

	public String toString() {
		return "ID: " + id + "\n" + "ID AMBITO SUP: " + idAmbitoSuperior + "\n"
				+ "NOMBRE: " + nombre + "\n" + "FECHA INI: " + fechaInicio
				+ "\n" + "FECHA FIN: " + fechaFin + "\n" + "DESCRIPCION: "
				+ descripcion + "\n";
	}
}
