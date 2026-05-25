package cr.ac.ucr.sga.model.structures.lists;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DoublyLinkedListTest {

    @Test
    void add() throws ListException {
        //a. Agregue 50 valores numéricos no repetidos a la lista y muestre la lista por consola
        DoublyLinkedList<Integer> listNumbers = new DoublyLinkedList<>();
        for (int i = 0; i < 50; i++) {
            listNumbers.add(i);
        }
        System.out.println(listNumbers);
        //b. Pruebe los métodos: getFirst y getLast
        System.out.println("getFirst(" + listNumbers.getFirst() + ")");
        System.out.println("getLast(" + listNumbers.getLast() + ")");

    }

    @Test
    public void doublyDoublyLinkedListTest() {
        DoublyLinkedList<Integer> doublyLinkedList = new DoublyLinkedList<>();
        doublyLinkedList.add(20);
        doublyLinkedList.add(10);
        for (int i = 0; i < 20; i++) {
            doublyLinkedList.add(new Random().nextInt(50));
        }
        System.out.println(doublyLinkedList);

        //quiero ver la data del primero y ult nodo de la lista
        System.out.println("_".repeat(50));
        System.out.println("getHead: " + doublyLinkedList.getHead().data);
        System.out.println("getTail: " + doublyLinkedList.getTail().data);

        System.out.println("addFirst(100)"); doublyLinkedList.addFirst(100);
        System.out.println("addFirst(200)"); doublyLinkedList.addFirst(200);
        System.out.println(doublyLinkedList);
        try {
            System.out.println("Linklist size: "+doublyLinkedList.size());

            //probamos contains
            System.out.println("_".repeat(50));
            for (int i=0; i<10;i++) {
                int value = new Random().nextInt(50);
                System.out.println(
                        doublyLinkedList.contains(value)
                                ? "value [" + value + "] exists. Position: "
                                + doublyLinkedList.indexOf(value)
                                : "value [" + value + "] does not exist"
                );
            }

            // ========= AGREGADO (punto 4.c de la guía): 20 búsquedas aleatorias + pos/prev/next =========
            System.out.println("_".repeat(50));
            System.out.println("20 random searches (show index, prev, next if found):");
            for (int i = 0; i < 20; i++) {
                int value = new Random().nextInt(50);

                if (doublyLinkedList.contains(value)) {
                    int pos = doublyLinkedList.indexOf(value);
                    Integer prev = doublyLinkedList.getPrev(value);
                    Integer next = doublyLinkedList.getNext(value);

                    System.out.println(
                            "value [" + value + "] FOUND. Position: " + pos
                                    + ", prev: " + (prev != null ? prev : "NULL")
                                    + ", next: " + (next != null ? next : "NULL")
                    );
                } else {
                    System.out.println("value [" + value + "] NOT FOUND.");
                }
            }
            // ========= FIN AGREGADO =========

            //Probamos removeFirst, removeLast
            System.out.println("\nremoveFirst: " + doublyLinkedList.removeFirst());
            System.out.println("removeLast: " + doublyLinkedList.removeLast());
            System.out.println("removeLast: " + doublyLinkedList.removeLast());

            //probamos get
            System.out.println("_".repeat(50));
            int n =  doublyLinkedList.size();
            for (int i = 1; i <= n; i++) {
                System.out.println("get(" + i + ") = " + doublyLinkedList.get(i));
            }

            System.out.println("_".repeat(50));
            System.out.println(doublyLinkedList);
            for (int i = 1; i <= n; i++) {
                System.out.println(
                        "get(" + i + ") = " + doublyLinkedList.get(i)
                                + ", getPrev(" + doublyLinkedList.get(i) + ") = "
                                + doublyLinkedList.getPrev(doublyLinkedList.get(i))
                                + ", getNext(" + doublyLinkedList.get(i) + ") = "
                                + doublyLinkedList.getNext(doublyLinkedList.get(i))
                );
            }

            //al final volvemos a mostrar la lista
            System.out.println("_".repeat(50));
            System.out.println(doublyLinkedList);

            //probamos los removes
            for (int i = 0; i < 20; i++) {
                int value =  new Random().nextInt(50);
                if(doublyLinkedList.contains(value)) {
                    System.out.println("remove("+value+") deleted !!!");
                    doublyLinkedList.remove(value);
                }
            }
            //al final volvemos a mostrar la lista
            System.out.println(doublyLinkedList);

            //probamos los removeFirst, removeLast
            System.out.println("_".repeat(50));
            for (int i = 0; i < 20; i++) {
                int value =  new Random().nextInt(50);
                if(doublyLinkedList.contains(value)) {
                    System.out.println("removeFirst(): "+doublyLinkedList.removeFirst());
                    System.out.println("removeLast(): "+doublyLinkedList.removeLast());
                    System.out.println(doublyLinkedList);
                }
            }
            //al final volvemos a mostrar la lista
            System.out.println(doublyLinkedList);

        } catch (ListException e) {
            throw new RuntimeException(e);
        }

}}