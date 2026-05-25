package cr.ac.ucr.sga.model.structures.stacks;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LinkedStackTest {

    @Test
    void linkedStackTest() {
        LinkedStack<Integer> stack = new LinkedStack<>();

        try{
            while (stack.size() < 50) {
                int value = new Random().nextInt(100);
                if (!stack.contains(value)) {//no repetidos
                    System.out.println("push(" + value + ")");
                    stack.push(value);
                }

            }

            System.out.println("-------------------------");
            System.out.println("Stack size: " + stack.size());
            System.out.println("-------------------------");
            System.out.println("Peek / Top: " + stack.peek());
            System.out.println(stack);
            System.out.println("-------------------------");

            for (int i = 0; i < 20; i++) {

                System.out.println("pop(): " + stack.pop());
                System.out.println(stack);

            }
        }catch (StackException e){
            throw  new RuntimeException(e);
        }
    }

}