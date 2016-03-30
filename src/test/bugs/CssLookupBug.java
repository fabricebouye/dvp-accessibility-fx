/* 
 * Copyright (C) 2015-2016 Fabrice Bouyé
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */
package test.bugs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Il y a un bug sur la résolution des lookups de couleur quand aucun contrôle n'est présent dans la scène.
 * Test-case qui a été posté sur http://bugs.java.com/
 * @author Fabrice Bouyé
 */
public final class CssLookupBug extends Application {

    private Button button = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Region node = new Region();
        // Initially -fx-focus-color fails to resolve.
        // A warning is shown because the lookup resolution for -fx-focus-color fails.
        // The region in the scene is either transparent or the wrong color.
        node.setStyle("-fx-background-color: -fx-focus-color;");
        VBox.setVgrow(node, Priority.ALWAYS);
        final VBox root = new VBox();
        node.setOnMouseClicked(mouseEvent -> {
            if (button == null) {
                button = new Button();
                root.getChildren().add(button);
                // -fx-focus-color is only resolved after the button has been added to the scene.
                // The lookup resolution happens immediatly without further modifications to the scene.
            }
        });
        root.getChildren().add(node);
        final Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String... args) {
        launch(args);
    }
}
