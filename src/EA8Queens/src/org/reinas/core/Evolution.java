package org.reinas.core;
import org.util.Random;

/**
 *
 * @author servkey
 *
 */
public class Evolution {
    //21, semilla .1 proba .8
    //OK .99, 50
    //OK .90, 50
    //OK .90, 100

    //Default .6
    private float semilla = 0.6f;

    //Probabilidad de mutacion
    private float probabilidad = 0.9f;
    private int[][] poblacion = new int[100][8];
    private int[] aptitudpoblacion = new int[poblacion.length];

    private int[][] cincoaleatorios = new int[5][8];
    private boolean imprimirPromedios = false;
    private boolean imprimirMejorPorGeneracion = false;

    public Evolution(float semilla){
        this.semilla = semilla;
    }

    //iniciar generaciones
    public int iniciar(){
        Random.setRseed(semilla);
        int evaluaciones = 0;

        //Generar evaluaciones
        evaluaciones = this.generatePopulate();        
        boolean encontrado = false;
        
        int generacion = 0;
        if (this.imprimirPromedios)
            System.out.println("Promedio Por generación");
        else if (this.imprimirMejorPorGeneracion)
            System.out.println("Mejor por generación");

       //Iniciar Generaciones
        while (evaluaciones <= 10000 && encontrado == false)
        {
            encontrado=false;
            //Seleccionar aleatoriamente 5 soluciones de la población y escoger las dos mejores
            //con base en su No. de ataques.
            int[] ataques = this.seleccionar5Random();
            this.ordernarMejores(this.cincoaleatorios, ataques);
    
            int[] padre1 = this.cincoaleatorios[0];
            int[] padre2 = this.cincoaleatorios[1];

            int[] hijo1 = new int[padre1.length];
            int[] hijo2 = new int[padre2.length];
            System.arraycopy(padre1, 0, hijo1, 0, padre1.length);
            System.arraycopy(padre2, 0, hijo2, 0, padre2.length);

            //Cruzar
            if (Random.flip(1) == 1){
                int index1 = Random.rnd(1,8) - 1;
                
                if (index1 == 7){
                    //Se queda igual
                    //hijo1 = padre1;
                    //hijo2 = padre2;
                }else
                {
                    //Cruzar
                    int iteraciones = padre1.length - (index1 + 1);
                    int anteriores = padre1.length - iteraciones;


                    int[] hijo1tmp = new int[padre1.length];
                    int[] hijo2tmp = new int[padre1.length];

                    for (int i0 = 0; i0 < anteriores; i0++){
                        hijo1tmp[i0] = hijo1[i0];
                        hijo2tmp[i0] = hijo2[i0];
                    }

                    //hijo 1
                    int index0 = index1;
                    int index2 = index1;
                    hijo1 = cruzar(index1, index2, iteraciones,  hijo1tmp, padre2);

                    //hijo 2
                    index2 = index0;
                    index1 = index2;
                    hijo2 = cruzar(index1, index2, iteraciones,  hijo2tmp, padre1);
                }

            }

            //Mutar hijo 1
            if (Random.flip(this.probabilidad) == 1)
                hijo1 = mutar(hijo1);

            //Mutar hijo 2
            if (Random.flip(this.probabilidad) == 1)
                hijo2 = mutar(hijo2);

            boolean hijo1Band = false;
            boolean hijo2Band = false;
            evaluaciones++;
            
            int evaluacionHijo1 = this.aptitud(hijo1);
            int evaluacionHijo2 = this.aptitud(hijo2);

            if (evaluacionHijo1 == 0)
                hijo1Band = true;

            evaluaciones++;
            if (evaluacionHijo2 == 0)
                 hijo2Band = true;
            
            this.ordernarMejores(this.poblacion,this.aptitudpoblacion);
            this.poblacion[poblacion.length-1] = hijo2;
            this.poblacion[poblacion.length-2] = hijo1;

            this.aptitudpoblacion[poblacion.length - 1]= evaluacionHijo2;
            this.aptitudpoblacion[poblacion.length - 2]= evaluacionHijo1;

            if (this.imprimirPromedios){
                System.out.println(this.calculateAverage());
            }else if(this.imprimirMejorPorGeneracion)
                System.out.println(this.mejorNumeroAtaques());

            generacion++;
            if (hijo1Band || hijo2Band){
                encontrado = true;
                System.out.println("Solución Encontrada");
                System.out.println("\nNúmero de generaciones -> " + generacion);
                System.out.println("\nSolución->");

                int auxhijo[] = hijo1Band?hijo1:hijo2;

                for (int j = 0; j < auxhijo.length; j++)
                        System.out.print(auxhijo[j] + ",");
                System.out.println();
                System.out.println("\nNúmero de evaluaciones -> " + evaluaciones);
                return evaluaciones;
            }            
            
        }
        if (!encontrado)
              evaluaciones = -1;
        return evaluaciones;

    }

    //Verifica si el vector enviado como parametro existe en la población
    private boolean equalPoblacion(int[] comparate,int index){
        boolean result = false;

        if (index != 0){
            int i = 0;
            while (i < index){

                for (int column = 0; column < poblacion[0].length; column++){
                    if (comparate[column] == poblacion[i][column])
                        result = true;
                    else{
                        result = false;
                        break;
                    }

                }
                if (result)
                   break;
                i++;
            }
        }

        return result;
    }


    //Generar población apartir de un vector (se intercambia)
    public int generatePopulate(){
        int i = 0;
        Random.randomize();
        int[] inicial = new int[]{2,4,6,5,8,1,3,7};
        int[] tmp = inicial;
        int[] copiaTmp = tmp;
        int ataques[] = new int[poblacion.length];
        int numAtaques = this.aptitud(tmp);


        while ( i < poblacion.length){
            poblacion[i] = copiaTmp;
            ataques[i] = numAtaques;
            do{
                int intercambio1 = getIntRnd(0);
                int intercambio2 = getIntRnd(intercambio1);

                copiaTmp = new int[copiaTmp.length];
                System.arraycopy(poblacion[i], 0, copiaTmp, 0, poblacion[i].length);

                int aux1 = copiaTmp[intercambio1-1];
                int aux2 = copiaTmp[intercambio2-1];
                copiaTmp[intercambio1-1] = aux2;
                copiaTmp[intercambio2-1] = aux1;
                numAtaques = this.aptitud(copiaTmp);
                
            }
            while ((equalPoblacion(copiaTmp,i)) && (numAtaques == 0));
            i++;
        }
        this.aptitudpoblacion = ataques;
        return poblacion.length;
    }


    //Función de aptitud para obtener número de ataques de un tablero
    public int aptitud(int[] genotipo)
    {
        int ataques = 0;
        int index = 1;
        int column = 1;
        boolean filas = true;
        int[][] fenotipo = getFenotipo(genotipo);
        //Recorrer primer sentido
        int count = 1;
        int countControl = 1;

        while (count <=  15){
            int numIterate = 0;
            numIterate = filas ?(index + column) - 1 : Math.abs(index -column) + 1  ;
            Reina reinaEvaluar = null;

            int columntmp = column;
            int indextmp = index;

            for (int i = 0; i < numIterate; i++){
                if (fenotipo[indextmp-1][columntmp-1] == 1){
                    ataques = reinaEvaluar != null? ataques + 1: ataques;
                    reinaEvaluar = reinaEvaluar == null? new Reina(indextmp, columntmp): reinaEvaluar;
                }
                indextmp--;
                columntmp++;
            }            

            if (filas){
                index = index + 1;
                countControl = countControl + 1;
            }
            else{
                column = column + 1;
                countControl = countControl -1;
            }

            if (index == 9){
                index = 8;
                column = 2;
                filas = false;
            }
            count++;
        }

        index = 8;
        column = 1;
        boolean columnas = true;
        count = 1;
        countControl = 1;
        //Recorrer segundo sentido
        while (count <= 15){
            /*int numIterate = 0;
            numIterate = columnas ?(index + column) + 1 : Math.abs(index -column) + 1  ;*/
            Reina reinaEvaluar = null;

            int columntmp = column;
            int indextmp = index;

            for (int i = 0; i < countControl; i++){
                if (fenotipo[indextmp-1][columntmp-1] == 1){
                    ataques = reinaEvaluar != null? ataques + 1: ataques;
                    reinaEvaluar = reinaEvaluar == null? new Reina(indextmp, columntmp): reinaEvaluar;
                }
                indextmp--;
                columntmp--;
            }

            if (columnas){
                countControl = countControl + 1;
                column = column + 1;
            }else{
                countControl = countControl -1;
                index = index - 1;
            }
            

            if (column == 9){
                column = 8;
                index = 7;
                countControl = 7;
                columnas = false;
            }

            count++;
        }
        return ataques;
    }

    //Obtener fenotipo apartir de un genotipo
    public int[][] getFenotipo(int[] genotipo){
        int[][] fenotipo = new int[8][8];
        for (int j = 0; j < genotipo.length; j++)
           fenotipo[genotipo[j]-1][j] = 1;

        return fenotipo;
    }

    //Imprimir detalle en cada evaluación, imprimir media de número de ataques
    public void setImprimirPromedios(boolean imprimirPromedios){
        this.imprimirPromedios = imprimirPromedios;
    }

    //Imprimir detalle en cada evaluación, imprimir media de número de ataques
    public void setImprimirMejorPorGeneracion(boolean imprimirMejorPorGeneracion){
        this.imprimirMejorPorGeneracion = imprimirMejorPorGeneracion;
    }

    //obtener un numero aleatorio pero que sea distinto al parametro enviado
    private int getIntRnd(int diff){
        int result = 0;

        result = Random.rnd(1,8);
	//System.out.println("Aleatorio entero= " + result;
        while (result == diff)
            result = Random.rnd(1,8);
        return result;
    }

    //mutar hijo
    private int[] mutar(int[] hijo){
          int index1 = this.getIntRnd(0) - 1;
          int index2 = this.getIntRnd(index1 + 1) - 1;
          int aux0hijo1 = hijo[index1];
          int aux1hijo1 = hijo[index2];
          hijo[index2] = aux0hijo1;
          hijo[index1] = aux1hijo1;
          return hijo;
    }


    //Cruzar padre para generar hijo
    private int[] cruzar(int index1, int index2, int iteraciones, int[] hijo, int[] padre){

        int i = 0;
        while (i < iteraciones){

            //int aux1 = padre1[1 + index2];
            int aux2 = padre[1 + index2];

            if (!this.isOn(hijo, aux2)){
                hijo[++index1] = aux2;
            }
            index2++;
            i++;
        }
        poblarRestante(hijo);
        return hijo;
    }


    //Elegir cinco aleatoreos de toda la poblacion
    private int[] seleccionar5Random(){
        int index = 0;
        int result = 0;
        int ataques[] = new int[5];
        int cincoindex[] = new int[5];
        boolean band = true;

        while (index < 5){
            band = true;
            //System.out.println("Aleatorio entero= " + result;
            while (band){
                result = Random.rnd(1,poblacion.length);
                band = false;
                for (int i = 0; i < index; i++)
                {
                    if (cincoindex[i] == (result -1)){
                        band = true;
                        break;
                    }
                }
                if (!band){                    
                    cincoindex[index] = result-1;
                    this.cincoaleatorios[index] = this.poblacion[cincoindex[index]];
                    ataques[index] = this.aptitudpoblacion[cincoindex[index]];
                    index++;
                }
            }
        }
        return ataques;
    }

    //Mejor número de ataques
    private int mejorNumeroAtaques(){
        int numAtaque = this.aptitudpoblacion[0];
        for (int i = 1; i < aptitudpoblacion.length; i++){
            if (aptitudpoblacion[i] < numAtaque)
                numAtaque = aptitudpoblacion[i];
        }
        return numAtaque;
    }

    //Ordenar arreglo de acuero a los numeros de ataque
    private void ordernarMejores(int [][] arreglo, int[] ataques){
        boolean termino = false;
        int size = arreglo.length;
        int comparations = size;
        //int ataques[] = new int[arreglo.length] ;

        for (int i = 0; i<(size-1) ; i++)
        {
           if (termino)
                break;

            termino = true;
            for (int j = 0; j < comparations-1 ; j++)
                if(ataques[j] >  ataques[j+1])
                {
                    int auxataque0 = ataques[j];
                    int auxataque1 = ataques[j+1];
                    
                    ataques[j] = auxataque1 ;
                    ataques[j + 1] = auxataque0;
                    //intercambiar(arreglo[j],arreglo[j+1]);

                    int auxtmp0[] = arreglo[j];
                    int auxtmp1[] = arreglo[j+1];

                    arreglo[j] = auxtmp1;
                    arreglo[j+1] = auxtmp0;
                    termino = false;
                }
                comparations--;
        }
    }

    //preguntar si el número ya esta en el vector
    private boolean isOn(int [] arreglo, int number){
        boolean result = false;
        for (int i = 0; i < arreglo.length; i++){
            if (arreglo[i] == number)
                return true;

        }
        return result;
    }

    //agregar valor a donde no haya
    private void agregar(int[] arreglo, int number){
        for (int i = 0; i < arreglo.length; i++){
            if (arreglo[i] == 0){
                arreglo[i] = number;
                break;
            }
        }
    }

    private void poblarRestante(int[] arreglo){
        for (int i = 1; i < 9; i++){
            if (!isOn(arreglo, i)){
                agregar(arreglo,i);
            }
        }
    }

    private double calculateAverage(){
        double sum = 0;
        for (int i = 0; i < this.aptitudpoblacion.length; i++){
            sum += aptitudpoblacion[i];
        }

        sum = sum / aptitudpoblacion.length;
        return sum;
    }
}
