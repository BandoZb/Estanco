package estancosemaforos;

public class Main {

    
    public static void main(String[] args) {
        
        int tiempoProduccion = 1000;
        Estanco estanco = new Estanco(tiempoProduccion);
        Estanquero estanquero = new Estanquero(estanco);
        
        //Los fumadores pueden recibir (o no) por parametros la cantidad de ingredientes iniciales
        Fumador fumador = new Fumador(estanco,"Daniel", 1,1,0);
        Fumador fumador2 = new Fumador(estanco,"Fernando",0,1,0);
        Fumador fumador3 = new Fumador(estanco, "Wanan", 0,0,2);
        
        
        fumador.start();
        fumador2.start();
        fumador3.start();
        estanquero.start();
    }
    
}
