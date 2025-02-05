package estancosemaforos;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Estanco {
    // Controla las peticiones
    private final Semaphore semaforoPeticiones = new Semaphore(0);
    
    // Controla que el fumador espere para comprar 
    private final Semaphore semaforoIngredienteListo = new Semaphore(0);
    
    // Semaforo para que cada fumador espere a poder pedir un ingrediente
    private final Semaphore semaforoOrdenPedido = new Semaphore(1);

    // Semaforo lo utilizo para controlar a los fumadores cuando notifian al estanquero que se van
    private final Semaphore semaforoFumadores = new Semaphore(1);

    int numTabaco = 0, numPapel = 0, numCerillas = 0;
    final int tiempoProduccion, tiempoFumando = 1000;
    private String ingredienteNecesitado;
    private int contadorFumadores ;

    public Estanco(int tiempoProduccion) {
        this.tiempoProduccion = tiempoProduccion;
    }

    /**
     * Metodo que produce un ingrediente aleatorio entre tabaco, papel y cerillas.
     * 
     * 
     */
    public void producirIngrediente() {
        try {
            
            semaforoPeticiones.acquire();

            System.out.println("El estanquero produce " + ingredienteNecesitado);
            Thread.sleep(tiempoProduccion);

            switch (ingredienteNecesitado) {
                case "tabaco":
                    numTabaco++;
                    break;
                case "papel":
                    numPapel++;
                    break;
                case "cerillas":
                    numCerillas++;
                    break;
            }

            System.out.println(ingredienteNecesitado + " listo");
            
            semaforoIngredienteListo.release();

        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    /**
     * Metodo que utiliza el fumador, pidiendo el ingrediente que necesita en ese momento y comprandolo cuando esté disponible
     * 
     * @param ingrediente
     * @param nombre 
     */
    public void pedirIngrediente(String ingrediente, String nombre) {
        try {
            semaforoOrdenPedido.acquire();
            ingredienteNecesitado = ingrediente;
            System.out.println("-----------------------------------\n" + nombre + " pide " + ingrediente + "...\n-----------------------------------");
            semaforoPeticiones.release();
            semaforoIngredienteListo.acquire();

            switch (ingrediente) {
                case "tabaco":
                    if (numTabaco > 0) {
                        numTabaco--;
                    }
                    break;
                case "papel":
                    if (numPapel > 0) {
                        numPapel--;
                    }
                    break;
                case "cerillas":
                    if (numCerillas > 0) {
                        numCerillas--;
                    }
                    break;
            }
            System.out.println("-------------------------\n" + nombre + " compra  " + ingrediente + "...\n-------------------------");
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        semaforoOrdenPedido.release();
    }

    /**
     * Este metodo lo utilizan los fumadores para consumir sus ingredientes y fumar el cigarro
     * Se pasara por parametros un nombre
     * 
     * @param nombre 
     */
    public void fumar(String nombre) {
        try {
            System.out.println(nombre + " fumando...");
            Thread.sleep(tiempoFumando);
        } catch (InterruptedException ex) {
            Logger.getLogger(Estanco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Metodo que devuelve un @return booleano para saber si hay fumadores activos o no
     * 
     * @return 
     */
    public boolean hayFumadores() {
        return contadorFumadores > 0;
    }

    /**
     * Método que notifica si un fumador ha terminado
     * 
     */
    public void notificarFumadorTerminado() {
        try {
            semaforoFumadores.acquire();
            contadorFumadores--;
            if (contadorFumadores == 0) {
                System.out.println("\n+++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++\nYa no hay fumadores. El estanco cierra por hoy\n+++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++");
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        } finally {
            semaforoFumadores.release();
        }
    }

    public int getContadorFumadores() {
        return contadorFumadores;
    }

    public void setContadorFumadores(int contadorFumadores) {
        this.contadorFumadores = contadorFumadores;
    }
    
    

}
