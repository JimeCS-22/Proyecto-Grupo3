package cr.ac.ucr.sga.model.structures.lists;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CircularDoublyLinkedListTest {

    @Test
    void test() {
        CircularDoublyLinkedList<Integer> list = new CircularDoublyLinkedList<>();
        int[] usados = new int[20];
        int usadosCount = 0;
        Random r = new Random();

        // Agregar 20 valores numéricos no repetidos de 0 a 99
        for (int i = 0; i < 20; ) {
            int value = r.nextInt(100);
            boolean repetido = false;
            for (int j = 0; j < usadosCount; j++) {
                if (usados[j] == value) {
                    repetido = true;
                    break;
                }
            }
            if (!repetido) {
                usados[usadosCount++] = value;
                list.add(value);
                System.out.println("add(" + value + ")");
                i++;
            }
        }
        System.out.println("Lista actual: " + list);

        // Buscar 10 valores aleatorios y eliminarlos si existen
        for (int i = 0; i < 10; i++) {
            int value = r.nextInt(100);
            int pos = 0;
            try {
                pos = list.indexOf(value);
            } catch (ListException e) {
                throw new RuntimeException(e);
            }
            try {
                if (list.contains(value)) {
                    System.out.println("El valor " + value + " EXISTE. Pos: " + pos);
                    list.remove(value);
                    System.out.println("Eliminado " + value + " => " + list);
                } else {
                    System.out.println("El valor " + value + " NO existe.");
                }
            } catch (ListException e) {
                throw new RuntimeException(e);
            }
        }

        // Prueba de métodos especiales
        if (!list.isEmpty()) {
            try {
                System.out.println("getFirst: " + list.getFirst());
                System.out.println("getLast: " + list.getLast());
                System.out.println("get(2): " + list.get(2));
                System.out.println("getPrev(2): " + list.getPrev(2));
                System.out.println("getNext(2): " + list.getNext(2));
                System.out.println("removeFirst: " + list.removeFirst());
                System.out.println("Lista después de removeFirst: " + list);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Lista final: " + list);
    }

}