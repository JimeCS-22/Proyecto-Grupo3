package ucr.algoritmos.proyecto.model;

import java.math.BigInteger;
import java.util.Random;

public class Probabilistic {

    /**
     * Busqueda aleatoria en un arreglo por medio de induces aleatorios
     * Se utiliza algoritmos probabilisticos
     * Devuelve un arreglo con el indice del valor buscado y el número de intentos realizadps
     * Si el indice es -1, el valor no se encontró el elemento total  de intentos realizados
     * */

    public int [] randomSearch (int [] arr, int value, int attempts){
        int[] result = new int[2];
        int count = 0;
        //Random random = new Random();

        if (arr == null || arr.length == 0 || attempts <= 0) {
            result[0] = -1;
            result[1] = 0;
            return result;
        }

        Random random = new Random();

        while (true) {
            int index = random.nextInt(arr.length);
            count++;
            result[0] = index;
            result[1] = count;

            if (arr[index] == value) return result; //Si el indice del array es igual a la variable

            if (count>= attempts){
                result[0] = -1;
                return result;
            }
        }

    }

    public String millerRabin(String bigNumber) {
        String result="";
        bigNumber = bigNumber.replaceAll("\\s+", "");
        //BigInteger number = new BigInteger("104729"); // Ejemplo de número primo
        BigInteger number = new BigInteger(bigNumber);
        int k = 5; // Número de repeticiones del test de Miller-Rabin
        if (isProbablePrime(number, k))
            result += "The big number: " + number + " is probably prime.";
        else
            result += "The big number: " + number + " is not prime.";
        return result;
    }

    private boolean isProbablePrime(BigInteger n, int k) {

        // Casos base
        if (n.compareTo(BigInteger.ONE) <= 0) return false;
        if (n.equals(BigInteger.TWO) || n.equals(BigInteger.valueOf(3))) return true;
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) return false;

        // n - 1 = d * 2^r
        BigInteger d = n.subtract(BigInteger.ONE);
        int r = 0;

        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.TWO);
            r++;
        }

        Random rand = new Random();

        for (int i = 0; i < k; i++) {

            // a aleatorio en [2, n-2]
            BigInteger a;
            do {
                a = new BigInteger(n.bitLength(), rand);
            } while (a.compareTo(BigInteger.TWO) < 0 || a.compareTo(n.subtract(BigInteger.TWO)) > 0);

            // x = a^d mod n
            BigInteger x = a.modPow(d, n);

            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE)))
                continue;

            boolean continueOuter = false;

            for (int j = 0; j < r - 1; j++) {
                x = x.modPow(BigInteger.TWO, n);

                if (x.equals(n.subtract(BigInteger.ONE))) {
                    continueOuter = true;
                    break;
                }
            }

            if (continueOuter)
                continue;

            return false; // compuesto
        }

        return true; // probablemente primo
    }
}



