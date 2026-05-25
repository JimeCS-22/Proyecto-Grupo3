package cr.ac.ucr.sga.model.structures.lists;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CircularLinkedListTest {

    @Test
    public void CircularLinkedListTest() {
        CircularLinkedList<Integer> CircularLinkedList = new CircularLinkedList<>();

        for (int i = 0; i < 30; i++) {
            try {
                if (!CircularLinkedList.contains(i)) {
                    CircularLinkedList.add(new Random().nextInt(50));
                }
            } catch (ListException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println(CircularLinkedList);

        //quiero ver la data del primero y ult nodo de la lista
        System.out.println("_".repeat(50));
        System.out.println("getHead: " + CircularLinkedList.getHead().data);
        System.out.println("getTail: " + CircularLinkedList.getTail().data);

        System.out.println("addFirst(100)"); CircularLinkedList.addFirst(100);
        System.out.println("addFirst(200)"); CircularLinkedList.addFirst(200);
        System.out.println(CircularLinkedList);
        try {
            System.out.println("Linklist size: "+CircularLinkedList.size());

            //probamos contains
            System.out.println("_".repeat(50));
            for (int i=0; i<10;i++) {
                int value = new Random().nextInt(50);
                System.out.println(
                        CircularLinkedList.contains(value)
                                ? "value [" + value + "] exists. Position: "
                                + CircularLinkedList.indexOf(value)
                                : "value [" + value + "] does not exist"
                );
            }

            //Probamos removeFirst, removeLast
            System.out.println("\nremoveFirst: " + CircularLinkedList.removeFirst());
            System.out.println("removeLast: " + CircularLinkedList.removeLast());
            System.out.println("removeLast: " + CircularLinkedList.removeLast());

            //probamos get
            System.out.println("_".repeat(50));
            int n =  CircularLinkedList.size();
            for (int i = 1; i <= n; i++) {
                System.out.println("get(" + i + ") = " + CircularLinkedList.get(i));
            }

            System.out.println("_".repeat(50));
            System.out.println(CircularLinkedList);
            for (int i = 1; i <= n; i++) {
                System.out.println(
                        "get(" + i + ") = " + CircularLinkedList.get(i)
                                + ", getPrev(" + CircularLinkedList.get(i) + ") = "
                                + CircularLinkedList.getPrev(CircularLinkedList.get(i))
                                + ", getNext(" + CircularLinkedList.get(i) + ") = "
                                + CircularLinkedList.getNext(CircularLinkedList.get(i))
                );
            }

            //al final volvemos a mostrar la lista
            System.out.println("_".repeat(50));
            System.out.println(CircularLinkedList);

            //probamos los removes
            for (int i = 0; i < 20; i++) {
                int value =  new Random().nextInt(50);
                if(CircularLinkedList.contains(value)) {
                    System.out.println("remove("+value+") deleted !!!");
                    CircularLinkedList.remove(value);
                }
            }
            //al final volvemos a mostrar la lista
            System.out.println(CircularLinkedList);

            //probamos los removeFirst, removeLast
            System.out.println("_".repeat(50));
            for (int i = 0; i < 20; i++) {
                int value =  new Random().nextInt(50);
                if(CircularLinkedList.contains(value)) {
                    System.out.println("removeFirst(): "+CircularLinkedList.removeFirst());
                    System.out.println("removeLast(): "+CircularLinkedList.removeLast());
                    System.out.println(CircularLinkedList);
                }
            }
            //al final volvemos a mostrar la lista
            System.out.println(CircularLinkedList);

        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }

}