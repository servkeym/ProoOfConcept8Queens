package prooofconcept8reinas;
import org.reinas.core.Evolution;
import org.util.DataReader;

/**
 *
 * @author servkey
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        

        //Leer 30 semillas del archivo semillas.txt del paquete org.util.semillas para iniciar la ejecución de la evolución
        float semillas[] = new DataReader().read();
        int evaluaciones[] = new int[semillas.length];

        for (int i = 0; i < semillas.length; i++){
            Evolution e = new Evolution(semillas[i]);
            evaluaciones[i] = e.iniciar();
        }
        System.out.println("\n\n*****************************************************");
        System.out.println("Evaluaciones con las semillas cargadas desde archivo");
        for (int i = 0; i < evaluaciones.length; i++)
            System.out.println(evaluaciones[i]);


        System.out.println("\n\n***************************************************************************");
        System.out.println("Calculando el promedio de la función aptitud en la mediana, por cada generación");
        //Realizar y obtener el promedio de la mediana
        
        Evolution e = new Evolution(0.18f);
        e.setImprimirPromedios(true);        
        e.iniciar();


        System.out.println("\n\n***************************************************************************");
        System.out.println("Calculando los mejores valores de la función aptitud en la mediana, por cada generación");
        //Realizar y obtener el promedio de la mediana

        e = new Evolution(0.18f);
        e.setImprimirMejorPorGeneracion(true);
        e.iniciar();
    }
}
