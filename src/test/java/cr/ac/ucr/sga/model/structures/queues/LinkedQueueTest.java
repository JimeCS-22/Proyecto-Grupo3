package cr.ac.ucr.sga.model.structures.queues;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LinkedQueueTest {

    @Test
    void linkedQueuetest() {
        LinkedQueue<Integer> queue = new LinkedQueue<>();
        try {
            for (int i = 0; i < 10; i++) {
                int value = new Random().nextInt(50);
                System.out.println("enQueue( " + value + ")");
                queue.enQueue(value);
            }

            System.out.println("-------------------------");
            System.out.println("Queue size: " + queue.size());
            System.out.println("-------------------------");
            System.out.println("Peek / Top: " + queue.peek());
            System.out.println(queue);
            System.out.println("-------------------------");

            for (int i = 0; i < 5; i++) {
                System.out.println("deQueue: " + queue.deQueue());
                System.out.println(queue);
            }

        } catch (QueueException e) {
            throw new RuntimeException(e);
        }


    }
}