/* 
 * Copyright (C) 2015-2016 Fabrice Bouyé
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
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
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

    private BorderPane root;

    @Override
    public void start(Stage primaryStage) throws IOException {
        root = new BorderPane();
        createMenuBar();
        final Scene scene = new Scene(root, 500, 800);
        final URL cssURL = getClass().getResource("app.css");
        scene.getStylesheets().add(cssURL.toExternalForm());
        primaryStage.setTitle(I18N.getString("app.title"));
        primaryStage.setScene(scene);
        primaryStage.show();
        //
        Platform.runLater(() -> test1());
        // ScenicView est un outils qui permet d'inspecter le contenu de la scène.
        // J'ignore tout du degré d'accessiblité de cet outil.
        // http://fxexperience.com/scenic-view/
//        ScenicView.show(scene);
    }
    
    /**
    * Crée par la barre de menu.
    */
    private void createMenuBar() {
        final MenuItem exitItem = new MenuItem(I18N.getString("action.exit"));
        exitItem.setOnAction(actionEvent -> Platform.exit());
        final Menu fileMenu = new Menu(I18N.getString("menu.file"));
        fileMenu.getItems().add(exitItem);
        final MenuItem test1Item = new MenuItem(I18N.getString("action.test1"));
        test1Item.setOnAction(actionEvent -> test1());
        final MenuItem test2Item = new MenuItem(I18N.getString("action.test2"));
        test2Item.setOnAction(actionEvent -> test2());
        final MenuItem test3Item = new MenuItem(I18N.getString("action.test3"));
        test3Item.setOnAction(actionEvent -> test3());
        final Menu viewMenu = new Menu(I18N.getString("menu.view"));
        viewMenu.getItems().setAll(test1Item, test2Item, test3Item);
        final MenuBar menuBar = new MenuBar();
        menuBar.getMenus().setAll(fileMenu, viewMenu);
        // Sur Mac OS afficher la barre de l'application dans la barre de menu systeme.
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            menuBar.setUseSystemMenuBar(true);
        }
        root.setTop(menuBar);
    }

    // Test 1 - le controle customisé seul.
    private void test1() {
        final Parent content = createCustomForm();
        root.setCenter(content);
    }

    // Test 2 - formulaire créé par code.
    private void test2() {
        final Parent content = new CodeForm();
        root.setCenter(content);
    }
    
    // Test 3 - formulaire créé par FXML.
    private void test3() {
        final Parent content = new FXMLForm();
        root.setCenter(content);
    }

    private Parent createCustomForm() {
        final DirectionSelector directionSelector = new DirectionSelector();
        VBox.setVgrow(directionSelector, Priority.ALWAYS);
        final VBox root = new VBox();
        root.getChildren().add(directionSelector);
        final Button button = new Button();
        // Hack : l'ajout + suppression d'un bouton corrige un bug dans la résolution des lookups CSS.
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
