/**
 *
 */
package ejercicio2;

/**
 * Clase con la informacion sobre una comida
 *
 * @author Iker Garcia Ramirez
 * @date 27/4/2015
 *
 */
public class Comida {
    private String nombre; // nombre de la comida
    private float precio; // precio de la comida
    private String descripcion; // descripcion de la comida
    private int calorias; // calorias de la comida

    /**
     * Constructor de la clase Comida
     *
     * @param nombre
     * @param precio
     * @param descripcion
     * @param calorias
     */
    public Comida(String nombre, float precio, String descripcion, int calorias) {
	this.nombre = nombre;
	this.precio = precio;
	this.descripcion = descripcion;
	this.calorias = calorias;
    }

    /**
     * @return el nombre
     */
    public String getNombre() {
	return nombre;
    }

    /**
     * @return el precio
     */
    public float getPrecio() {
	return precio;
    }

    /**
     * @return el descripcion
     */
    public String getDescripcion() {
	return descripcion;
    }

    /**
     * @return las calorias
     */
    public int getCalorias() {
	return calorias;
    }

}

--------------------

package br.com.fiap.tdst.am.advocacia.beans;

public final class Cidade {
	
	private long id;
	private Estado estado;
	private String nome;
	
	public Cidade() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}

--------------------

/*  Tipo :
 * 0 = Con reserva
 * 1 = Sin reserva
 *  Estado
 * 0 = Anulada
 * 1 = Atendida
 */
package entidad;

public class Hospedaje {
	private int codHospedaje,codReserva,codCliente,numHabitacion,codRecepcionista,tipoHospedaje,estadoHospedaje;
	private String fechaRegistroHospedaje;
	public Hospedaje(){}
	public Hospedaje(int codHospedaje, int codReserva, int codCliente,
			int codHabitacion, int codRecepcionista, int tipoHospedaje,
			String fechaRegistroHospedaje, int estadoHospedaje) {
		this.codHospedaje = codHospedaje;
		this.codReserva = codReserva;
		this.codCliente = codCliente;
		this.numHabitacion = codHabitacion;
		this.codRecepcionista = codRecepcionista;
		this.tipoHospedaje = tipoHospedaje;
		this.fechaRegistroHospedaje = fechaRegistroHospedaje;
		this.estadoHospedaje = estadoHospedaje;
	}
	public int getCodHospedaje() {
		return codHospedaje;
	}
	public void setCodHospedaje(int codHospedaje) {
		this.codHospedaje = codHospedaje;
	}
	public int getCodReserva() {
		return codReserva;
	}
	public void setCodReserva(int codReserva) {
		this.codReserva = codReserva;
	}
	public int getCodCliente() {
		return codCliente;
	}
	public void setCodCliente(int codCliente) {
		this.codCliente = codCliente;
	}
	public int getCodHabitacion() {
		return numHabitacion;
	}
	public void setCodHabitacion(int codHabitacion) {
		this.numHabitacion = codHabitacion;
	}
	public int getCodRecepcionista() {
		return codRecepcionista;
	}
	public void setCodRecepcionista(int codRecepcionista) {
		this.codRecepcionista = codRecepcionista;
	}
	public int getTipoHospedaje() {
		return tipoHospedaje;
	}
	public void setTipoHospedaje(int tipoHospedaje) {
		this.tipoHospedaje = tipoHospedaje;
	}
	public int getEstadoHospedaje() {
		return estadoHospedaje;
	}
	public void setEstadoHospedaje(int estadoHospedaje) {
		this.estadoHospedaje = estadoHospedaje;
	}
	public String getFechaRegistroHospedaje() {
		return fechaRegistroHospedaje;
	}
	public void setFechaRegistroHospedaje(String fechaRegistroHospedaje) {
		this.fechaRegistroHospedaje = fechaRegistroHospedaje;
	}
	
	public String getTipo(){
		String tipo="";
		switch (tipoHospedaje) {
		case 0:	tipo="Con Reserva";break;
		case 1: tipo="Sin Reserva";break;
		}
		return tipo;
	}
	
	public String getEstado(){
		String est="";
		switch (estadoHospedaje) {
		case 0:	est="Anulada";break;
		case 1: est="Atendida";break;
		}
		return est;
	}
	
}

--------------------

