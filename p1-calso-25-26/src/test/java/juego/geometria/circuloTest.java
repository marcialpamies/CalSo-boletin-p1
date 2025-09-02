package juego.geometria;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas para la clase 'circulo' con errores intencionados.
 * NO invoca getCentro() para evitar el problema del clone().
 */
class circuloTest {

    // ==== STUBS MÍNIMOS PARA COMPILAR LAS PRUEBAS ====

    

    /** Stub mínimo de Punto: inmutable y con desplazamiento. */
    static final class StubPunto implements Punto {
        final int x, y;

        StubPunto() { this(0, 0); }
        StubPunto(int x, int y) { this.x = x; this.y = y; }

        @Override 
        public Punto Adyacente(Direccion direccion) { return this; }
        @Override 
        public Boolean isAdyacente(Punto otra, Direccion direccion) { return false; }
        @Override 
        public Direccion situacion_relativa(Punto otra) { return null; }
        @Override 
        public Punto desplazar(int incX, int incY) { return new StubPunto(x + incX, y + incY); }
        @Override 
        public Punto desplazar(Direccion direccion) { return this; }
        
        @Override
        public Punto clone() {
            return new StubPunto(this.x, this.y);
        }

        @Override 
        public boolean equals(Object o) {
            if (!(o instanceof StubPunto)) return false;
            StubPunto other = (StubPunto) o;
            return x == other.x && y == other.y;
        }
        @Override 
        public int hashCode() { return 31 * x + y; }
        @Override 
        public String toString() { return "StubPunto(" + x + "," + y + ")"; }
    }

    // ==== HELPERS ====

    /** Lee el campo privado 'centro' por reflexión. */
    private static Punto getCentroPrivado(circulo c) {
        try {
            Field f = circulo.class.getDeclaredField("centro");
            f.setAccessible(true);
            return (Punto) f.get(c);
        } catch (Exception e) {
            fail("No se pudo leer 'centro' por reflexión: " + e.getMessage());
            return null;
        }
    }

    /** Escribe el campo privado 'centro' por reflexión. */
    private static void setCentroPrivado(circulo c, Punto p) {
        try {
            Field f = circulo.class.getDeclaredField("centro");
            f.setAccessible(true);
            f.set(c, p);
        } catch (Exception e) {
            fail("No se pudo asignar 'centro' por reflexión: " + e.getMessage());
        }
    }

    // ==== TESTS ====

    @Test
    @DisplayName("Constructor principal: BUG -> no asigna 'centro'; desplazar provoca NPE")
    void constructorPrincipal_noAsignaCentro_desplazarDaNPE() {
        Punto p = new StubPunto(10, 20);
        circulo c = new circulo(p, 7);

        assertEquals(7, c.getRadio(), "El radio sí se asigna");
        assertThrows(NullPointerException.class, () -> c.desplazar(1, 1),
                "Como 'centro' no se asigna, desplazar debe lanzar NPE (bug documentado)");
    }

    @Test
    @DisplayName("Constructor con Supplier: sigue llamando al principal -> 'centro' sigue null; radio=DEFAULT")
    void constructorSupplier_sigueSinAsignarCentro() {
        Supplier<Punto> proveedor = () -> new StubPunto(1, 2);
        circulo c = new circulo(proveedor);

        assertEquals(circulo.DEFAULT_RADIO, c.getRadio());
        assertThrows(NullPointerException.class, () -> c.desplazar(0, 0),
                "El centro no se inicializa porque el ctor principal lo ignora");
    }

    @Test
    @DisplayName("getPerimetro calcula 2πr independientemente del centro")
    void perimetro_ok() {
        circulo c = new circulo(new StubPunto(0, 0), 5);
        double esperado = 2 * Math.PI * 5;
        assertEquals(esperado, c.getPerimetro(), 1e-9);
    }

    @Test
    @DisplayName("escalar trunca a int (comportamiento actual)")
    void escalar_trunca() {
        circulo c = new circulo(new StubPunto(0, 0), 10);

        c.escalar(49.0); // 10 * 0.49 = 4.9 -> (int) = 4
        assertEquals(4, c.getRadio());

        c.escalar(200.0); // 4 * 2.0 = 8
        assertEquals(8, c.getRadio());

        c.escalar(0.0); // 8 * 0 = 0
        assertEquals(0, c.getRadio());

        c = new circulo(new StubPunto(0, 0), 10);
        c.escalar(-50.0); // permite negativos (documenta bug/diseño)
        assertEquals(-5, c.getRadio());
    }

    @Test
    @DisplayName("desplazar actualiza 'centro' cuando está inicializado (inyectado por reflexión)")
    void desplazar_actualizaCentro_cuandoExiste() {
        circulo c = new circulo(new StubPunto(3, 4), 5);
        setCentroPrivado(c, new StubPunto(3, 4)); // simulamos centro asignado

        c.desplazar(2, -3);

        Punto centro = getCentroPrivado(c);
        assertEquals(new StubPunto(5, 1), centro,
                "desplazar debe guardar un nuevo Punto trasladado");
    }

    @Disabled("BUG intencionado: getCentro() llama a clone() sobre la interfaz Punto; activar cuando se corrija")
    @Test
    @DisplayName("getCentro debería devolver el centro (o copia defensiva) cuando se corrija clone()")
    void getCentro_bugDocumentado() {
        circulo c = new circulo(new StubPunto(2, 2), 4);
        setCentroPrivado(c, new StubPunto(2, 2));

        // Cuando Punto declare/impl clone() o se cambie a retorno directo/copia, habilitar:
        // assertEquals(new StubPunto(2, 2), c.getCentro());
    }
}