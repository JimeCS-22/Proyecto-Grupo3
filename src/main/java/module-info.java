    module cr.ac.ucr.sga {
        requires javafx.controls;
        requires javafx.fxml;
        requires javafx.base;
        requires com.google.gson;
        requires itextpdf;
        requires org.apache.poi.ooxml;


        //----- GSON REFLECTION (CLAVE: agrega todos los opens relevantes) -----
        opens cr.ac.ucr.sga.model to com.google.gson, javafx.base;
        opens cr.ac.ucr.sga.model.data to com.google.gson;
        opens cr.ac.ucr.sga.model.entities to com.google.gson, javafx.base;
        opens cr.ac.ucr.sga.model.structures.lists to com.google.gson, javafx.base;
        opens cr.ac.ucr.sga.model.structures.queues to com.google.gson, javafx.base;
        opens cr.ac.ucr.sga.model.structures.stacks to com.google.gson, javafx.base;

        opens cr.ac.ucr.sga to javafx.fxml; // Para FXML y controladores

        opens cr.ac.ucr.sga.controller to javafx.fxml;
        opens util to javafx.fxml;

        exports util;
        exports cr.ac.ucr.sga.controller;
        exports cr.ac.ucr.sga;
        opens cr.ac.ucr.sga.graphics to com.google.gson, javafx.base;
    }