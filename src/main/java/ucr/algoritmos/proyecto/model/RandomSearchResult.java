package ucr.algoritmos.proyecto.model;

import javafx.beans.property.SimpleIntegerProperty;

public class RandomSearchResult {
    private final SimpleIntegerProperty value = new SimpleIntegerProperty();
    private final SimpleIntegerProperty index = new SimpleIntegerProperty();
    private final SimpleIntegerProperty attempts = new SimpleIntegerProperty();
    private final SimpleIntegerProperty maxAttempts = new SimpleIntegerProperty();

    public RandomSearchResult(int value, int index, int attempts, int maxAttempts) {
        this.value.set(value);
        this.index.set(index);
        this.attempts.set(attempts);
        this.maxAttempts.set(maxAttempts);
    }

    public int getValue() { return value.get(); }
    public int getIndex() { return index.get(); }
    public int getAttempts() { return attempts.get(); }
    public int getMaxAttempts() { return maxAttempts.get(); }
}