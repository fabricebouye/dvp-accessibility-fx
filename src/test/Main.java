/* 
 * Copyright (C) 2015 Fabrice Bouyé
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */
package test;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import org.scenicview.ScenicView;
import test.direction.DirectionSelector;

/**
 * Application.
 * @author Fabriceb Bouyé
 */
public final class Main extends Application {

    public static final ResourceBundle I18N = ResourceBundle.getBundle(Main.class.getPackage().getName().replaceAll("\\.", "/") + "/strings");

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Test 1 - le controle customisé seul.
//        final Parent root = createCustomForm();
        // Test 2 - formulaire créé par code.
//        final Parent root = new CodeForm();
        // Test 3 - formulaire créé par FXML.
        final Parent root = new FXMLForm();
        final Scene scene = new Scene(root, 500, 800);
        final URL cssURL = getClass().getResource("app.css");
        scene.getStylesheets().add(cssURL.toExternalForm());
        primaryStage.setTitle(I18N.getString("app.title"));
        primaryStage.setScene(scene);
        primaryStage.show();
        // ScenicView est un outils qui permet d'inspecter le contenu de la scène.
        // J'ignore tout du degré d'accessiblité de cet outil.
        // http://fxexperience.com/scenic-view/
//        ScenicView.show(scene);
    }

    private Parent createCustomForm() {
        final DirectionSelector directionSelector = new DirectionSelector();
        VBox.setVgrow(directionSelector, Priority.ALWAYS);
        final VBox root = new VBox();
        root.getChildren().add(directionSelector);
        final Button button = new Button();
        // Hack : l'ajout + suppression d'un bouton corrige un bug dans la résolution des lookup CSS.
        // Voir test.bugs.CssLookupBug
        root.getChildren().add(button);
        root.getChildren().remove(button);
        return root;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
