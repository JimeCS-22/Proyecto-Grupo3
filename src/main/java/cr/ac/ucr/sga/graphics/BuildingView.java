package cr.ac.ucr.sga.graphics;

import cr.ac.ucr.sga.model.entities.Building;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.control.Tooltip;

public class BuildingView extends StackPane{

    private final Building building;

    private Rectangle background;

    private Label icon;

    private Label title;

    public BuildingView(Building building){

        this.building = building;

        createComponents();

        configureLayout();

        configureEffects();

        configureEvents();

        playAppearAnimation();

    }

    private void createComponents() {

        background = new Rectangle(170, 90);

        background.setArcWidth(25);
        background.setArcHeight(25);

        background.setFill(Color.web("#3498DB"));

        background.setStroke(Color.WHITE);
        background.setStrokeWidth(2);

        icon = new Label(building.getIcon());

        icon.setStyle("""
            -fx-font-size:28;
            """);

        title = new Label(building.getName());

        title.setStyle("""
            -fx-font-size:15;
            -fx-font-weight:bold;
            -fx-text-fill:white;
            """);

        Tooltip tooltip = new Tooltip(
                building.getIcon() + " " + building.getName() +
                        "\n\n" +
                        building.getDescription() +
                        "\n\nClick para seleccionar"
        );

        tooltip.setShowDelay(Duration.millis(200));

        Tooltip.install(this, tooltip);

    }

    private void configureLayout() {

        VBox content = new VBox(6);

        content.setAlignment(Pos.CENTER);

        content.getChildren().addAll(icon, title);

        setAlignment(Pos.CENTER);

        getChildren().addAll(background, content);

        setLayoutX(building.getX());

        setLayoutY(building.getY());

    }

    private void configureEffects() {

        DropShadow shadow = new DropShadow();

        shadow.setRadius(15);

        shadow.setOffsetY(4);

        shadow.setColor(Color.rgb(0,0,0,.25));

        setEffect(shadow);

    }

    private void configureEvents() {

        setOnMouseEntered(e -> {
            background.setFill(Color.web("#2980B9"));
            playScaleAnimation(1.08);
        });

        setOnMouseExited(e -> {
            background.setFill(Color.web("#3498DB"));
            playScaleAnimation(1.0);
        });

        setOnMouseClicked(e -> {
            background.setFill(Color.web("#27AE60"));
        });

    }

    private void playAppearAnimation() {

        setOpacity(0);

        FadeTransition transition =
                new FadeTransition(Duration.millis(500), this);

        transition.setFromValue(0);

        transition.setToValue(1);

        transition.play();

    }

    private void playScaleAnimation(double scale) {

        ScaleTransition transition = new ScaleTransition(Duration.millis(180), this);
        transition.setToX(scale);
        transition.setToY(scale);
        transition.play();

    }

    public void select() {

        background.setFill(Color.web("#27AE60"));
        playScaleAnimation(1.12);

    }

    public void unselect() {

        background.setFill(Color.web("#3498DB"));
        playScaleAnimation(1.0);

    }

    public Building getBuilding() {
        return building;
    }


    public void visit() {

        background.setFill(Color.ORANGE);

        ScaleTransition transition =
                new ScaleTransition(Duration.millis(300), this);

        transition.setToX(1.2);
        transition.setToY(1.2);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);

        transition.play();
    }
    public void finish() {

        background.setFill(Color.web("#27AE60"));

    }
}
