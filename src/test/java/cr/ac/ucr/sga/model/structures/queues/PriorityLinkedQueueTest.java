package cr.ac.ucr.sga.model.structures.queues;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PriorityLinkedQueueTest {

    @Test
    void prioritylinkedQueuetest() {
        PriorityLinkedQueue<Integer> queue = new PriorityLinkedQueue<>();

        try {
            while (queue.size() < 30)  {
                int value = new Random().nextInt(50);
                int priority = new Random().nextInt(1,4);

                if (!queue.contains(value) ) {//no repetidos
                    System.out.println("enQueue( " + value + ", " + priority + ")");
                    queue.enQueue(value, priority); }
            }

            System.out.println("-------------------------");
            System.out.println("Queue size: " + queue.size());
            System.out.println("-------------------------");
            System.out.println("Peek / Top: " + queue.peek());
            System.out.println(queue);
            System.out.println("-------------------------");

            for (int i = 0; i < 20; i++) {
                System.out.println("deQueue: " + queue.deQueue());
                System.out.println(queue);
            }

        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
    }

}