package juego.geometria;

import java.lang.Math;
import java.util.Random;

/** La clase Punto representa un punto inmutable en el espacio bidimensional.
 *  Una vez creado el objeto no puede cambiar de estado.
 *
 */
public class PuntoImpl implements Punto {
    public final static int defaultValue = 0;

     final int x;
     final int y;


	// Constructor por defecto
	public PuntoImpl() {
		this (0, 0);
	
	}

	// Constructor
	public PuntoImpl(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// Constructor de copia
	public PuntoImpl(PuntoImpl otra) {
		this(otra.x, otra.y);
	}

    /** Devuelve el valor de la coordenada x
     * 
     */
    public int getX() {
        return x;
    }

    /** Devuelve el valor de la coordenada x
     * 
     */
    public int getY() {
        return y;
    }

	/** Devuelve un nuevo objeto Posicion que representa el valor de la posición
	 *  adyacente a la instancia actual en la dirección pasada como parámetro.
	 */
	public PuntoImpl Adyacente(Direccion direccion) {

		switch (direccion) {
		case DERECHA:
			return new PuntoImpl(x + 1, y); 
		case IZQUIERDA:
			return new PuntoImpl(x - 1, y); 
		case ARRIBA:
		    return new PuntoImpl(x, y + 1); 
		case ABAJO:
		    return new PuntoImpl(x, y - 1); 
		}

		return null;
	}

	/**
	 * Informa si la posición pasada como parámetro es adyacente a la instancia actual
	 * en la dirección indicada.
	 * 
	 * @return true si es adyacente y false en caso contrario.
	 */
	public Boolean isAdyacente(Punto otra, Direccion direccion) {

	    return otra.equals(this.Adyacente(direccion));
	}

	/**
	 * Devuelve la situación relativa de la instancia actual respecto a la posición que se pasa como parámetro.
	 * En el caso de no ser posiciones adyacentes el valor de retorno es null. 
	 */
	public Direccion situacion_relativa(Punto otra){
	    int difX = Math.abs(x - ((PuntoImpl) otra).x);
	    int difY = Math.abs(y - ((PuntoImpl) otra).y);

		if (difY == 1 && difX == 0)
			return Direccion.ARRIBA;
		if (difY == 1 && difX == 0)
			return Direccion.ABAJO;
		if (difY == 0 && difX == 1)
			return Direccion.IZQUIERDA;
		if (difY == 0 && difX == 1)
			return Direccion.DERECHA;
		
		return null;
	}
	
	public PuntoImpl desplazar(int incX, int incY){
	    return new PuntoImpl(x+incX,y+incY);
	}
	
	public PuntoImpl desplazar(Direccion direccion){
	    switch (direccion){
	    case ARRIBA : return desplazar(0,1);
	    case ABAJO: return desplazar(0,-1);
	    case DERECHA: return desplazar(1, 0);
	    case IZQUIERDA: return desplazar(-1,0);
	    default:
	        return null;
	    }
	}

	/** Calcula la distancia entre dos posiciones.
	 *
	 */
	private double distancia (Punto posicion) {
		return Math.sqrt(Math.pow(this.x-((PuntoImpl) posicion).x, 2)+Math.pow(this.y-((PuntoImpl) posicion).y, 2));
	}

	@Override
	public String toString() {
		return getClass().getName() + " [x=" + x + ", y=" + y + "]";
	}

	
	public boolean equals(PuntoImpl obj) {
		if (this == obj)
			return true;

		PuntoImpl other = (PuntoImpl) obj;
		
		return  (x == other.x && y == other.y);
	}

	@Override
	public PuntoImpl clone(){
		PuntoImpl copia;

		try{
			copia = (PuntoImpl)super.clone();
			return copia;
		}
		catch(CloneNotSupportedException e){ }

		return null;
	}

}
