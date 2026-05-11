package ucr.algoritmos.proyecto.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search {

    public static List<String> steps = new ArrayList<>();

    /**
     * int value = 10
     * int pos =
     * binarySearch(sortedArray, value,0,sortedArray.length-1)
     *
     */

    public static int binarySearch(int[] sortedArray, int value, int low, int high) {

        //Caso base
        if (low > high) {
            steps.add("No se encontró el valor " + value);
            return -1;
        }

        int mid = (low + high) / 2;

        steps.add("Rango [" + low + " , " + high + " ] ----> mitad  = " + mid + " (sortedArray[mid] = " +
                sortedArray[mid] + " )");

        if (value == sortedArray[mid]) {
            steps.add("Valor encontrado en el indice " + mid);
            return mid;
        } else if (value < sortedArray[mid]) {
            return binarySearch(sortedArray, value, low, mid - 1);
        } else return binarySearch(sortedArray, value, mid + 1, high);

    }

    /**
     * Metodo iteractivo de la busqueda binaria
     **/

    public static int binarySearchIterative(int[] sortedArray, int value) {

        int low = 0;
        int high = sortedArray.length - 1;

        while (low <= high) {

            int mid = (low + high) / 2;

            steps.add("Rango [" + low + " , " + high + " ] ----> mitad  = " + mid + " (sortedArray[mid] = " +
                    sortedArray[mid] + " )");

            if (value == sortedArray[mid]) {
                steps.add("Valor encontrado en el indice " + mid);
                return mid;
            } else if (value < sortedArray[mid]) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        steps.add("No se encontró el valor " + value);
        return -1;
    }

    /**
     * Metodo de busqueda binaria Secuencial
     **/
    public static int linearSearch(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            steps.add("Comparar array[" + i + "]=" + array[i] + " con " + value);
            if (array[i] == value) {
                steps.add("Valor encontrado en el indice " + i);
                return i;
            }
        }
        steps.add("No se encontró el valor " + value);
        return -1;
    }

    /**
     * Metodo de busqueda binaria Secuencial con centinela
     */
    public static int sentinelLinearSearch(int[] array, int value) {
        if (array.length == 0) {
            steps.add("Arreglo vacío, No se encontró el valor " + value);
            return -1;
        }

        int n = array.length;
        int last = array[n - 1];

        int[] copy = Arrays.copyOf(array, n + 1);
        copy[n] = value; // centinela

        int i = 0;
        while (copy[i] != value) {
            steps.add("Comparar array[" + i + "]=" + copy[i] + " con " + value);
            i++;
        }

        // Si cayó en el centinela, no estaba
        if (i == n) {
            steps.add("No se encontró el valor " + value + " (se llegó al centinela)");
            return -1;
        }

        steps.add("Valor encontrado en el indice " + i);
        return i;
    }

    /**
     * Metodo de búsqueda por Interpolación (arreglo ordenado)
     */
    public static int interpolationSearch(int[] sortedArray, int value) {
        int low = 0;
        int high = sortedArray.length - 1;

        while (low <= high && value >= sortedArray[low] && value <= sortedArray[high]) {

            if (sortedArray[high] == sortedArray[low]) {
                steps.add("Rango [" + low + "," + high + "] tiene valores iguales: " + sortedArray[low]);
                if (sortedArray[low] == value) {
                    steps.add("Valor encontrado en el indice " + low);
                    return low;
                }
                break;
            }

            // Fórmula de interpolación
            int pos = low + (int) (((long) (high - low) * (value - sortedArray[low]))
                    / (sortedArray[high] - sortedArray[low]));

            steps.add("Rango [" + low + "," + high + "] -> pos=" + pos +
                    " (A[low]=" + sortedArray[low] + ", A[high]=" + sortedArray[high] + ")");

            if (pos < low || pos > high) {
                steps.add("pos fuera del rango. Terminar.");
                break;
            }

            if (sortedArray[pos] == value) {
                steps.add("Valor encontrado en el indice " + pos);
                return pos;
            } else if (sortedArray[pos] < value) {
                low = pos + 1;
            } else {
                high = pos - 1;
            }
        }

        steps.add("No se encontró el valor " + value);
        return -1;
    }


    //Practica
    public static class MinMax {
        private int min;
        private int max;

        public MinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

    }

    public static MinMax findMinMax(int[] arr, int low, int high) {
        //Case base: solo hay un elemento
        if(low==high){
            steps.add("Caso base 1 elemento: min=="+arr[low]+" max=="+arr[high]);
            return new MinMax(arr[low], arr[low]);
        }
        //Caso base: dos elementos
        if(high == low + 1){
            steps.add("Caso base 2 elementos: min=="+Math.min(arr[low], arr[high])+
                    ", max=="+Math.max(arr[low], arr[high]));
            return new MinMax(Math.min(arr[low], arr[high]), Math.max(arr[low], arr[high]));
        }

        //En otro caso, debemos dividir el arreglo en dos mitades
        int mid = (low+high)/2;
        steps.add("Rango ["+low+","+high+"] -->mid="+mid+", arr[mid]=="+arr[mid]);
        steps.add("leftResult: findMinMax(arr,"+low+","+mid+") -->low=="+low
                +", arr[low]=="+arr[low]+", high=="+mid+", arr[mid]=="+arr[mid]);
        MinMax leftResult = findMinMax(arr, low, mid); //la mitad a la izq
        steps.add("rightResult: findMinMax(arr," + (mid+1) + "," + high + ") -->low==" + (mid+1)
                + ", arr[mid+1]==" + arr[mid+1] + ", high==" + high + ", arr[high]==" + arr[high]);
        MinMax rightResult = findMinMax(arr, mid+1, high); //la mitad a la der

        //Podemos combinar los resultados
        int min = Math.min(leftResult.getMin(), rightResult.getMin());
        steps.add("valor de la variable min =="+min);
        int max = Math.max(leftResult.getMax(), rightResult.getMax());
        steps.add("valor de la variable max =="+max);

        return new MinMax(min, max);
    }


}