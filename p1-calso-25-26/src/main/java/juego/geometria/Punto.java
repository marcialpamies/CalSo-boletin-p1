package juego.geometria;

public interface Punto extends Cloneable {
	abstract Punto clone();
	abstract public Punto Adyacente(Direccion direccion);
	abstract public Boolean isAdyacente(Punto otra, Direccion direccion);
	abstract public Direccion situacion_relativa(Punto otra);
	abstract public Punto desplazar(int incX, int incY);
	abstract public Punto desplazar(Direccion direccion);
	
}
