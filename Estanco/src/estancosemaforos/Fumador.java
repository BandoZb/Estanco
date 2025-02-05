package estancosemaforos;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Fumador extends Thread {

    private Estanco estanco;
    private String nombre;
    private int numTabacoPropio = 0, numPapelPropio = 0, numCerillasPropias = 0, cigarrosFumados = 0;

    public Fumador(Estanco estanco, String nombre) {
        this.estanco = estanco;
        this.nombre = nombre;
    }
    
    /**
     * Constructor que recibe por parametros los objetos iniciales en el caso que se le indique
     * 
     * @param estanco
     * @param nombre
     * @param tabacoInicial
     * @param papelInicial
     * @param cerillasIniciales 
     */
    public Fumador(Estanco estanco, String nombre, int tabacoInicial, int papelInicial, int cerillasIniciales) {
        this.estanco = estanco;
        this.nombre = nombre;
        this.numTabacoPropio = tabacoInicial;
        this.numPapelPropio = papelInicial;
        this.numCerillasPropias = cerillasIniciales;
    }

    @Override
    public void run() {
        estanco.setContadorFumadores(estanco.getContadorFumadores()+1);
        while (cigarrosFumados < 2) {
            System.out.println("Materiales de " + nombre + " son: tabaco: " + numTabacoPropio+ ", papel:  " + numPapelPropio + ", cerillas: " + numCerillasPropias );
            try {
                if(numTabacoPropio == 0){
                    estanco.pedirIngrediente("tabaco", nombre);
                    numTabacoPropio++;
                }
                else if(numPapelPropio == 0){
                    estanco.pedirIngrediente("papel", nombre);
                    numPapelPropio++;
                }
                else if(numCerillasPropias == 0){
                    estanco.pedirIngrediente("cerillas", nombre);
                    numCerillasPropias++;
                }
                else{
                    estanco.fumar(nombre);
                    numTabacoPropio--;
                    numPapelPropio--;
                    numCerillasPropias--;
                    cigarrosFumados++;

                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        System.out.println("*************** "+nombre + " ya fumo " + cigarrosFumados + " cigarros, se va a su casa ***************");
        estanco.notificarFumadorTerminado();
    }
}
