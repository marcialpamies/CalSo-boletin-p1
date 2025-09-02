package juego.geometria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PuntoImplTest {

    @Nested
    @DisplayName("Constructores")
    class Constructores {

        @Test
        @DisplayName("Constructor por defecto crea (0,0)")
        void ctorDefecto() {
            PuntoImpl p = new PuntoImpl();
            assertEquals(0, p.getX());
            assertEquals(0, p.getY());
        }

        @Test
        @DisplayName("Constructor (x,y) asigna coordenadas")
        void ctorXY() {
            PuntoImpl p = new PuntoImpl(3, -2);
            assertEquals(3, p.getX());
            assertEquals(-2, p.getY());
        }

        @Test
        @DisplayName("Constructor de copia duplica coordenadas")
        void ctorCopia() {
            PuntoImpl original = new PuntoImpl(5, 7);
            PuntoImpl copia = new PuntoImpl(original);
            assertEquals(5, copia.getX());
            assertEquals(7, copia.getY());
            assertTrue(original.equals(copia), "equals(PuntoImpl) debería devolver true para mismos valores");
        }
    }

    @Nested
    @DisplayName("Adyacencia y desplazamientos")
    class Movimiento {

        @Test
        @DisplayName("Adyacente(DERECHA) => (x+1, y)")
        void adyacenteDerecha() {
            PuntoImpl p = new PuntoImpl(0, 0);
            PuntoImpl q = p.Adyacente(Direccion.DERECHA);
            assertEquals(1, q.getX());
            assertEquals(0, q.getY());
        }

        @Test
        @DisplayName("Adyacente(IZQUIERDA) => (x-1, y)")
        void adyacenteIzquierda() {
            PuntoImpl p = new PuntoImpl(0, 0);
            PuntoImpl q = p.Adyacente(Direccion.IZQUIERDA);
            assertEquals(-1, q.getX());
            assertEquals(0, q.getY());
        }

        @Test
        @DisplayName("Adyacente(ARRIBA) => (x, y+1)")
        void adyacenteArriba() {
            PuntoImpl p = new PuntoImpl(3, 4);
            PuntoImpl q = p.Adyacente(Direccion.ARRIBA);
            assertEquals(3, q.getX());
            assertEquals(5, q.getY());
        }

        @Test
        @DisplayName("Adyacente(ABAJO) => (x, y-1)")
        void adyacenteAbajo() {
            PuntoImpl p = new PuntoImpl(3, 4);
            PuntoImpl q = p.Adyacente(Direccion.ABAJO);
            assertEquals(3, q.getX());
            assertEquals(3, q.getY());
        }

        @Test
        @DisplayName("desplazar(incX,incY) traslada correctamente")
        void desplazarInc() {
            PuntoImpl p = new PuntoImpl(10, -2);
            PuntoImpl q = p.desplazar(-3, 5);
            assertEquals(7, q.getX());
            assertEquals(3, q.getY());
        }

        @Test
        @DisplayName("desplazar(Direccion) delega en incX/incY")
        void desplazarDireccion() {
            PuntoImpl p = new PuntoImpl(1, 1);
            assertEquals(new PuntoImpl(1, 2), p.desplazar(Direccion.ARRIBA));
            assertEquals(new PuntoImpl(1, 0), p.desplazar(Direccion.ABAJO));
            assertEquals(new PuntoImpl(2, 1), p.desplazar(Direccion.DERECHA));
            assertEquals(new PuntoImpl(0, 1), p.desplazar(Direccion.IZQUIERDA));
        }
    }

    @Nested
    @DisplayName("isAdyacente y situacion_relativa (tests de intención)")
    class Relacion {

        @Test
        @DisplayName("isAdyacente debe ser true para puntos adyacentes en la dirección dada (falla con equals actual)")
        void isAdyacente_true_para_adyacentes() {
            Punto otra = new PuntoImpl(2, 3);
            PuntoImpl p = new PuntoImpl(1, 3);

            assertTrue(p.isAdyacente(otra, Direccion.DERECHA),
                "Debería ser true si otra == p.Adyacente(DERECHA)");

        }

        @Test
        @DisplayName("situacion_relativa devuelve la dirección correcta (falla con lógica actual)")
        void situacionRelativa_correcta() {
            PuntoImpl base = new PuntoImpl(0, 0);

            assertEquals(Direccion.ARRIBA,  base.situacion_relativa(new PuntoImpl(0, 1)));
            assertEquals(Direccion.ABAJO,   base.situacion_relativa(new PuntoImpl(0, -1)));
            assertEquals(Direccion.DERECHA, base.situacion_relativa(new PuntoImpl(1, 0)));
            assertEquals(Direccion.IZQUIERDA, base.situacion_relativa(new PuntoImpl(-1, 0)));

            assertNull(base.situacion_relativa(new PuntoImpl(2, 0)), "No adyacente → null");
            assertNull(base.situacion_relativa(new PuntoImpl(1, 1)), "Diagonal no es adyacente → null");
        }
    }

    @Nested
    @DisplayName("equals y toString")
    class IgualdadYToString {

        @Test
        @DisplayName("equals(PuntoImpl) funciona con mismo tipo, pero falla al upcast a Punto (bug de contrato)")
        void equalsSobrecargado_noSOBREescrito() {
            PuntoImpl a = new PuntoImpl(2, 2);
            PuntoImpl b = new PuntoImpl(2, 2);

            assertTrue(a.equals(b), "equals(PuntoImpl) devuelve true para mismos valores (mismo tipo declarado)");

            Punto pa = a; 
            assertFalse(pa.equals(b), "Upcast a Punto rompe la simetría/contrato de equals (bug didáctico)");
        }

        @Test
        @DisplayName("toString contiene nombre de clase y coordenadas")
        void toStringFormato() {
            PuntoImpl p = new PuntoImpl(3, 4);
            String s = p.toString();
            assertTrue(s.contains("PuntoImpl"));
            assertTrue(s.contains("x=3"));
            assertTrue(s.contains("y=4"));
        }
    }

    @Nested
    @DisplayName("clone")
    class Clonado {

        @Test
        @DisplayName("clone debería devolver una copia; implementación actual retorna null (bug)")
        void clone_devuelveNull_bug() {
            PuntoImpl p = new PuntoImpl(7, 8);
            PuntoImpl c = p.clone();
            assertNull(c, "Con la implementación actual (sin Cloneable), clone() retorna null tras capturar la excepción");
        }

        @Disabled("Habilitar cuando PuntoImpl implemente Cloneable y sobreescriba clone() correctamente")
        @Test
        @DisplayName("clone devuelve copia con mismos valores y distinta referencia")
        void clone_ok_cuando_se_arregle() {
            PuntoImpl p = new PuntoImpl(7, 8);
            PuntoImpl c = p.clone();
            assertNotSame(p, c);
            assertEquals(p.getX(), c.getX());
            assertEquals(p.getY(), c.getY());
        }
    }
}
