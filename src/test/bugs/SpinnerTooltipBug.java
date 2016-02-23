/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.bugs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * L'infobulle placée sur le spinner n'est pas placée sur son éditeur.
 * Test-case qui a été posté sur http://bugs.java.com/
 * @author Fabrice Bouyé
 */
public final class SpinnerTooltipBug extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        final ComboBox comboBox1 = new ComboBox();
        comboBox1.setMaxWidth(Double.MAX_VALUE);
        comboBox1.setTooltip(new Tooltip("Combo non-editable toolip."));
        final ComboBox comboBox2 = new ComboBox();
        comboBox2.setEditable(true);
        comboBox2.setMaxWidth(Double.MAX_VALUE);
        comboBox2.setTooltip(new Tooltip("Combo editable toolip."));
        final DatePicker datePicker = new DatePicker();
        datePicker.setMaxWidth(Double.MAX_VALUE);
        datePicker.setTooltip(new Tooltip("DatePicker toolip."));
        final Spinner spinner = new Spinner();
        spinner.setMaxWidth(Double.MAX_VALUE);
        spinner.setTooltip(new Tooltip("Spinner toolip."));
        final VBox root = new VBox(comboBox1, comboBox2, datePicker, spinner);
        root.setStyle("-fx-spacing: 6px; -fx-padding: 6px;");
        final Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String... args) {
        launch(args);
    }
}
