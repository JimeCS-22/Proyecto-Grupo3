package ucr.algoritmos.proyecto.model;

import javafx.beans.property.SimpleStringProperty;

public class MillerRabinResult {

    private final SimpleStringProperty number;
    private final SimpleStringProperty result;

    public MillerRabinResult(String number, String result) {
        this.number = new SimpleStringProperty(number);
        this.result = new SimpleStringProperty(result);
    }

    public String getNumber() {
        return number.get();
    }

    public String getResult() {
        return result.get();
    }
}
