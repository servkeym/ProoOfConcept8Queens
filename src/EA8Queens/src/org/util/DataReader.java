package org.util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * @author servkey
 */
public class DataReader {

    public float[] read(){
        return read("semillas.txt");
    }

    private float[] read(String nameFile){
        float[] matrix = new float[30];

        try{
                URL  url = getClass().getResource("./" + nameFile);
                InputStream in = url.openStream();
                Scanner scanner = new Scanner(in);
                int columna = 0;
                
                while (scanner.hasNext()){
                    matrix[columna] = Float.valueOf(scanner.next());
                    columna++;
                }

                
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e1){
            e1.printStackTrace();
        }
        return matrix;
    }
}
