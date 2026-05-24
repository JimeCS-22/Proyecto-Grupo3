
module cr.ac.ucr.sga{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;
    requires com.google.gson;

    opens cr.ac.ucr.sga.model.data to com.google.gson;
    opens cr.ac.ucr.sga.model to javafx.base;
    opens cr.ac.ucr.sga to javafx.fxml;
    opens cr.ac.ucr.sga.model.entities to com.google.gson,javafx.base;
    //opens cr.ac.ucr.sga.model to javafx.base;
    exports cr.ac.ucr.sga;

    opens cr.ac.ucr.sga.controller to javafx.fxml;
    exports util;
    opens util to javafx.fxml;
    opens cr.ac.ucr.sga.model.structures.lists to javafx.base;
    opens cr.ac.ucr.sga.model.structures.stacks to javafx.base;
    opens cr.ac.ucr.sga.model.structures.queues to javafx.base;

    exports cr.ac.ucr.sga.controller;

}