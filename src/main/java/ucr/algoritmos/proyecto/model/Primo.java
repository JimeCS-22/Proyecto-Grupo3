package ucr.algoritmos.proyecto.model;

public class Primo {

    public static boolean esPrimo(int numero) {
        if (numero <= 1) {
            return false;
        }
        if (numero == 2) {
            return true;
        }
        if (numero % 2 == 0) {
            return false;
        }

        int limite = (int) Math.sqrt(numero);
        for (int divisor = 3; divisor <= limite; divisor += 2) {
            if (numero % divisor == 0) {
                return false;
            }
        }
        return true;
    }

}