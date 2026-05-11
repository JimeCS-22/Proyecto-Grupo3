module ucr.algoritmos.proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;


    opens ucr.algoritmos.proyecto to javafx.fxml;
    opens ucr.algoritmos.proyecto.model to javafx.base;
    exports ucr.algoritmos.proyecto;
    exports ucr.algoritmos.proyecto.controller;
    opens ucr.algoritmos.proyecto.controller to javafx.fxml;
    exports util;
    opens util to javafx.fxml;
    opens ucr.algoritmos.proyecto.model.linkedList to javafx.base;
    opens ucr.algoritmos.proyecto.model.stack to javafx.base;
    opens ucr.algoritmos.proyecto.model.Queue to javafx.base;
}