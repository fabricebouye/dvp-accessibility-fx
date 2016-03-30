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
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import test.chart.BulletGraph;
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
        final MenuItem test4Item = new MenuItem(I18N.getString("action.test4"));
        test4Item.setOnAction(actionEvent -> test4());
        final MenuItem test5Item = new MenuItem(I18N.getString("action.test5"));
        test5Item.setOnAction(actionEvent -> test5());
        final MenuItem test6Item = new MenuItem(I18N.getString("action.test6"));
        test6Item.setOnAction(actionEvent -> test6());
        final Menu viewMenu = new Menu(I18N.getString("menu.view"));
        viewMenu.getItems().setAll(test1Item, new SeparatorMenuItem(), test2Item, test3Item, new SeparatorMenuItem(), test4Item, test5Item, test6Item);
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

    // Test 4 - graphe (camembert - API standard).
    // Pour ce test on utilise un graphique en camembert (pie chart).
    // L'idée ici est de savoir :
    // 1 - comment l'assitant décrit l'état initial du graphe.
    // 2 - comment l'assistant notifie l'utilisateur des modifications de valeurs dans le graphe.    
    private void test4() {
        final PieChart pieChart = new PieChart();
        pieChart.getData().add(new PieChart.Data("Windows", 75));
        pieChart.getData().add(new PieChart.Data("Linux", 20));
        pieChart.getData().add(new PieChart.Data("Mac OS X", 5));
        VBox.setVgrow(pieChart, Priority.ALWAYS);
        final Button randomButton = new Button(I18N.getString("chart.action.random.label"));
        randomButton.setTooltip(new Tooltip(I18N.getString("chart.action.random.tip")));
        randomButton.setOnAction(actionEvent -> {
            final int winShare = (int) (100 * Math.random());
            final int linShare = (int) ((100 - winShare) * Math.random());
            final int macShare = 100 - (winShare + linShare);
            pieChart.getData().get(0).setPieValue(winShare);
            pieChart.getData().get(1).setPieValue(linShare);
            pieChart.getData().get(2).setPieValue(macShare);
        });
        final ToolBar bottomBar = new ToolBar();
        bottomBar.getItems().setAll(randomButton);
        final VBox content = new VBox();
        content.getChildren().setAll(pieChart, bottomBar);
        root.setCenter(content);
    }

    // Test 5 - graphe (lignes - API standard).
    // Pour ce test on utilise un graphique en camembert (pie chart).
    // L'idée ici est de savoir :
    // 1 - comment l'assitant décrit l'état initial du graphe.
    // 2 - comment l'assistant notifie l'utilisateur des modifications de valeurs dans le graphe.    
    private void test5() {
        final NumberAxis xAxis = new NumberAxis(0, 10, 2);
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        resetLineChartSeries(lineChart, 2);
        VBox.setVgrow(lineChart, Priority.ALWAYS);
        final Button randomButton = new Button(I18N.getString("chart.action.random.label"));
        randomButton.setTooltip(new Tooltip(I18N.getString("chart.action.random.tip")));
        randomButton.setOnAction(actionEvent -> {
            final int value = (int) (5 * Math.random());
            resetLineChartSeries(lineChart, value);
        });
        final ToolBar bottomBar = new ToolBar();
        bottomBar.getItems().setAll(randomButton);
        final VBox content = new VBox();
        content.getChildren().setAll(lineChart, bottomBar);
        root.setCenter(content);
    }

    private void resetLineChartSeries(final LineChart<Number, Number> lineChart, final int value) {
        LineChart.Series<Number, Number> series = lineChart.getData().isEmpty() ? null : lineChart.getData().get(0);
        if (series == null) {
            series = new LineChart.Series<>();
            lineChart.getData().add(series);
        }
        series.setName(String.format("Puissance de %d", value));
        series.getData().clear();
        final int maxX = (int) ((NumberAxis) lineChart.getXAxis()).getUpperBound();
        final List<LineChart.Data<Number, Number>> data = IntStream.rangeClosed(0, maxX)
                .mapToObj(x -> new LineChart.Data<Number, Number>(x, Math.pow(value, x)))
                .collect(Collectors.toList());
        series.getData().setAll(data);
    }

    // Test 6 - graphe (nouveau contrôle).
    // Ici notre contrôle n'est pas pour le moment accessible.
    private void test6() {
        final BulletGraph bulletChart = new BulletGraph();
        bulletChart.setOrientation(Orientation.VERTICAL);
        bulletChart.setTitle("Revenus 2005");
        bulletChart.setDescription("Milliers d'Euro €");
        bulletChart.getAxis().setUpperBound(400);
        bulletChart.setPerformanceMeasure(350);
        bulletChart.setComparativeMeasure(125);
        VBox.setVgrow(bulletChart, Priority.ALWAYS);
        final Button randomButton = new Button(I18N.getString("chart.action.random.label"));
        randomButton.setTooltip(new Tooltip(I18N.getString("chart.action.random.tip")));
        randomButton.setOnAction(actionEvent -> {
            final double performanceMeasure = bulletChart.getAxis().getUpperBound() * Math.random();
            final double comparativeMeasure = bulletChart.getAxis().getUpperBound() * Math.random();
            bulletChart.setPerformanceMeasure(performanceMeasure);
            bulletChart.setComparativeMeasure(comparativeMeasure);
        });
        final ToolBar bottomBar = new ToolBar();
        bottomBar.getItems().setAll(randomButton);
        bottomBar.getItems().setAll(randomButton);
        final VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.getChildren().setAll(bulletChart, bottomBar);
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
