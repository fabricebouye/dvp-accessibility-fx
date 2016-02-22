/* 
 * Copyright (C) 2015 Fabrice Bouyé
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */
package test;

import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import test.direction.Direction;
import test.direction.DirectionSelector;

/**
 * Formulaire créé par code (attention le montage de l'UI est long).
 * @author Fabriceb Bouyé
 */
public final class CodeForm extends StackPane {

    private final ResourceBundle bundle = Main.I18N;

    public CodeForm() {
        super();
        setId("form");
        createUI();
    }

    private void createUI() {
        final Tab formTab = new Tab(bundle.getString("tab.form.label"));
        createFormTab(formTab);
        final Tab listTab = new Tab(bundle.getString("tab.list.label"));
        createListTab(listTab);
        final Tab tableTab = new Tab(bundle.getString("tab.table.label"));
        createTableTab(tableTab);
        final Tab treeTab = new Tab(bundle.getString("tab.tree.label"));
        createTreeTab(treeTab);
        final Tab treeTableTab = new Tab(bundle.getString("tab.treetable.label"));
        createTreeTableTab(treeTableTab);
        final Tab customTab = new Tab(bundle.getString("tab.custom.label"));
        createCustomTab(customTab);
        final TabPane tabPane = new TabPane();
        tabPane.getTabs().setAll(formTab, listTab, tableTab, treeTab, treeTableTab, customTab);
        getChildren().add(tabPane);
    }

    private void createFormTab(final Tab tab) {
        // Nom.
        final Label nameLabel = new Label();
        nameLabel.setId("nameLabel");
        nameLabel.setText(bundle.getString("form.name.label"));
        GridPane.setConstraints(nameLabel, 0, 0);
        final TextField nameField = new TextField();
        nameField.setId("nameField");
        nameField.setPromptText(bundle.getString("form.name.prompt"));
        nameField.setTooltip(new Tooltip(bundle.getString("form.name.tip")));
        GridPane.setConstraints(nameField, 1, 0);
        nameLabel.setLabelFor(nameField);
        // Prénom.
        final Label surnameLabel = new Label();
        surnameLabel.setId("surnameLabel");
        surnameLabel.setText(bundle.getString("form.surname.label"));
        GridPane.setConstraints(surnameLabel, 0, 1);
        final TextField surnameField = new TextField();
        surnameField.setId("surnameField");
        surnameField.setPromptText(bundle.getString("form.surname.prompt"));
        surnameField.setTooltip(new Tooltip(bundle.getString("form.surname.tip")));
        GridPane.setConstraints(surnameField, 1, 1);
        surnameLabel.setLabelFor(surnameField);
        // Date de naissance.
        final Label birthdayLabel = new Label();
        birthdayLabel.setId("birthdayLabel");
        birthdayLabel.setText(bundle.getString("form.birthday.label"));
        GridPane.setConstraints(birthdayLabel, 0, 2);
        final DatePicker birthdayPicker = new DatePicker();
        birthdayPicker.setId("birthdayPicker");
        birthdayPicker.setPromptText(bundle.getString("form.birthday.prompt"));
        birthdayPicker.setTooltip(new Tooltip(bundle.getString("form.birthday.tip")));
        birthdayPicker.setMaxWidth(Double.MAX_VALUE);
        GridPane.setConstraints(birthdayPicker, 1, 2);
        birthdayLabel.setLabelFor(birthdayPicker);
        // Sex.
        final Label sexLabel = new Label();
        sexLabel.setId("sexLabel");
        sexLabel.setText(bundle.getString("form.sex.label"));
        GridPane.setConstraints(sexLabel, 0, 3);
        final ToggleGroup sexToggleGroup = new ToggleGroup();
        final RadioButton sexFemaleRadio = new RadioButton();
        sexFemaleRadio.setId("sexFemaleRadio");
        sexFemaleRadio.setText(bundle.getString("form.sex.female.label"));
        sexFemaleRadio.setTooltip(new Tooltip(bundle.getString("form.sex.female.tip")));
        sexFemaleRadio.setToggleGroup(sexToggleGroup);
        sexFemaleRadio.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(sexFemaleRadio, Priority.ALWAYS);
        final RadioButton sexMaleRadio = new RadioButton();
        sexMaleRadio.setId("sexMaleRadio");
        sexMaleRadio.setText(bundle.getString("form.sex.male.label"));
        sexMaleRadio.setTooltip(new Tooltip(bundle.getString("form.sex.male.tip")));
        sexMaleRadio.setToggleGroup(sexToggleGroup);
        sexMaleRadio.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(sexMaleRadio, Priority.ALWAYS);
        final HBox sexHBox = new HBox();
        sexHBox.setId("sexHBox");
        sexHBox.getChildren().setAll(sexFemaleRadio, sexMaleRadio);
        GridPane.setConstraints(sexHBox, 1, 3);
        sexLabel.setLabelFor((Node) sexToggleGroup.getToggles().get(0));
        // Enfants.
        final Label kidsLabel = new Label();
        kidsLabel.setId("kidsLabel");
        kidsLabel.setText(bundle.getString("form.kids.label"));
        GridPane.setConstraints(kidsLabel, 0, 4);
        final Spinner<Integer> kidsSpinner = new Spinner();
        kidsSpinner.setId("kidsField");
        kidsSpinner.setMaxWidth(Double.MAX_VALUE);
        kidsSpinner.setTooltip(new Tooltip(bundle.getString("form.kids.tip")));
        // Actuellement, l'API ne permet pas de modifier le prompt text sur le spinner.
        // Il faut donc accéder à son éditeur.
        kidsSpinner.getEditor().setPromptText(bundle.getString("form.kids.prompt"));
        // Bug dans le spinner : l'infobulle n'est pas reportée sur l'éditeur.
        // Voir : test.bugs.SpinnerTooltipBug
        kidsSpinner.getEditor().setTooltip(new Tooltip(bundle.getString("form.kids.tip")));
        // La factory entière va cacher le prompt text avec la valeur par défaut 0.
        // Le prompt text est surtout utile quand on à un spinner d'objets.        
        kidsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        GridPane.setConstraints(kidsSpinner, 1, 4);
        kidsLabel.setLabelFor(kidsSpinner);
        // Couleur.
        final Label colorLabel = new Label();
        colorLabel.setId("colorLabel");
        colorLabel.setText(bundle.getString("form.color.label"));
        GridPane.setConstraints(kidsLabel, 0, 5);
        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setId("colorPicker");
        colorPicker.setTooltip(new Tooltip(bundle.getString("form.color.tip")));
        colorPicker.setMaxWidth(Double.MAX_VALUE);
        GridPane.setConstraints(colorPicker, 1, 5);
        colorLabel.setLabelFor(colorPicker);
        // Création de la grille.
        final GridPane formGridPane = new GridPane();
        formGridPane.setId("formGridPane");
        // Contraintes des colonnes.
        final List<ColumnConstraints> columnsContraints = IntStream.range(0, 2)
                .mapToObj(index -> {
                    final ColumnConstraints columnConstraint = new ColumnConstraints();
                    columnConstraint.setHgrow(index == 0 ? Priority.NEVER : Priority.ALWAYS);
                    columnConstraint.setMinWidth(index == 0 ? Region.USE_COMPUTED_SIZE : 10);
                    columnConstraint.setPrefWidth(index == 0 ? Region.USE_COMPUTED_SIZE : 100);
                    return columnConstraint;
                })
                .collect(Collectors.toList());
        formGridPane.getColumnConstraints().setAll(columnsContraints);
        // Contraintes des lignes.
        final List<RowConstraints> rowConstraints = IntStream.range(0, 6)
                .mapToObj(index -> {
                    final RowConstraints rowConstraint = new RowConstraints();
                    rowConstraint.setMinHeight(20);
                    rowConstraint.setPrefHeight(30);
                    rowConstraint.setVgrow(Priority.NEVER);
                    return rowConstraint;
                })
                .collect(Collectors.toList());
        formGridPane.getRowConstraints().setAll(rowConstraints);
        formGridPane.getChildren().setAll(nameLabel, nameField,
                surnameLabel, surnameField,
                birthdayLabel, birthdayPicker,
                sexLabel, sexHBox,
                kidsLabel, kidsSpinner);
        final StackPane container = new StackPane();
        container.getStyleClass().add("container-pane");
        container.getChildren().add(formGridPane);
        tab.setContent(container);
    }

    private void createListTab(final Tab tab) {
        final ListView<String> dataListView = new ListView();
        dataListView.setItems(createListModel());
        final StackPane container = new StackPane();
        container.getStyleClass().add("container-pane");
        container.getChildren().add(dataListView);
        tab.setContent(container);
    }

    private void createTableTab(final Tab tab) {
        final TableColumn<String, String> dataTableViewCol0 = new TableColumn();
        dataTableViewCol0.setText(bundle.getString("form.data.key.column"));
        dataTableViewCol0.setMinWidth(75);
        dataTableViewCol0.setMaxWidth(75);
        dataTableViewCol0.setCellValueFactory(feature -> {
            final String value = feature.getValue();
            return new SimpleObjectProperty(value);
        });
        final TableColumn<String, Number> dataTableViewCol1 = new TableColumn();
        dataTableViewCol1.setText(bundle.getString("form.data.value.column"));
        dataTableViewCol1.setCellValueFactory(feature -> {
            final String value = feature.getValue();
            final Number number = parseToNumber(value);
            return new SimpleObjectProperty(number);
        });
        final TableView<String> dataTableView = new TableView();
        dataTableView.setItems(createListModel());
        dataTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        dataTableView.getColumns().setAll(dataTableViewCol0, dataTableViewCol1);
        final StackPane container = new StackPane();
        container.getStyleClass().add("container-pane");
        container.getChildren().add(dataTableView);
        tab.setContent(container);
    }

    private void createTreeTab(final Tab tab) {
        final TreeView<String> dataTreeView = new TreeView();
        dataTreeView.setRoot(createTreeModel());
        final StackPane container = new StackPane();
        container.getStyleClass().add("container-pane");
        container.getChildren().add(dataTreeView);
        tab.setContent(container);
    }

    private void createTreeTableTab(final Tab tab) {
        final TreeTableColumn<String, String> dataTreeTableViewCol0 = new TreeTableColumn();
        dataTreeTableViewCol0.setText(bundle.getString("form.data.key.column"));
        dataTreeTableViewCol0.setMinWidth(200);
        dataTreeTableViewCol0.setMaxWidth(200);
        dataTreeTableViewCol0.setCellValueFactory(feature -> {
            final TreeItem<String> item = feature.getValue();
            final String value = item.getValue();
            return new SimpleObjectProperty(value);
        });
        final TreeTableColumn<String, Number> dataTreeTableViewCol1 = new TreeTableColumn();
        dataTreeTableViewCol1.setText(bundle.getString("form.data.value.column"));
        dataTreeTableViewCol1.setCellValueFactory(feature -> {
            final TreeItem<String> item = feature.getValue();
            final String value = item.getValue();
            final Number number = parseToNumber(value);
            return new SimpleObjectProperty(number);
        });
        final TreeTableView<String> dataTreeTableView = new TreeTableView();
        dataTreeTableView.setRoot(createTreeModel());
        dataTreeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        dataTreeTableView.getColumns().setAll(dataTreeTableViewCol0, dataTreeTableViewCol1);
        final StackPane container = new StackPane();
        container.getStyleClass().add("container-pane");
        container.getChildren().add(dataTreeTableView);
        tab.setContent(container);
    }

    private boolean isDirectionEditing = false;

    private void createCustomTab(final Tab tab) {
        // Direction.
        final Label directionLabel = new Label();
        directionLabel.setId("directionLabel");
        directionLabel.setText(bundle.getString("form.direction.label"));
        GridPane.setConstraints(directionLabel, 0, 0);
        final ComboBox<Direction> directionCombo = new ComboBox();
        directionCombo.setId("directionCombo");
        directionCombo.setPromptText(bundle.getString("form.direction.prompt"));
        directionCombo.setTooltip(new Tooltip(bundle.getString("form.direction.selector.tip")));
        directionCombo.setMaxWidth(Double.MAX_VALUE);
        directionCombo.getItems().setAll(Direction.values());
        GridPane.setConstraints(directionCombo, 1, 0);
        directionLabel.setLabelFor(directionCombo);
        // Direction selector.
        final DirectionSelector directionSelector = new DirectionSelector();
        directionSelector.setId("directionSelector");
        directionSelector.setTooltip(new Tooltip(bundle.getString("form.direction.selector.tip")));
        GridPane.setConstraints(directionSelector, 1, 1);
        // Event.
        directionCombo.setValue(directionSelector.getDirection());
        directionCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!isDirectionEditing) {
                isDirectionEditing = true;
                directionSelector.setDirection(newValue);
                isDirectionEditing = false;
            }
        });
        directionSelector.directionProperty().addListener((observable, oldValue, newValue) -> {
            if (!isDirectionEditing) {
                isDirectionEditing = true;
                directionCombo.setValue(newValue);
                isDirectionEditing = false;
            }
        });
        // Grille
        final GridPane customGridPane = new GridPane();
        customGridPane.setId("customGridPane");
        // Contraintes des colonnes.
        final List<ColumnConstraints> columnsContraints = IntStream.range(0, 2)
                .mapToObj(index -> {
                    final ColumnConstraints columnConstraint = new ColumnConstraints();
                    columnConstraint.setHgrow(index == 0 ? Priority.NEVER : Priority.ALWAYS);
                    columnConstraint.setMinWidth(index == 0 ? Region.USE_COMPUTED_SIZE : 10);
                    columnConstraint.setPrefWidth(index == 0 ? Region.USE_COMPUTED_SIZE : 100);
                    return columnConstraint;
                })
                .collect(Collectors.toList());
        customGridPane.getColumnConstraints().setAll(columnsContraints);
        // Contraintes des lignes.
        final List<RowConstraints> rowConstraints = IntStream.range(0, 5)
                .mapToObj(index -> {
                    final RowConstraints rowConstraint = new RowConstraints();
                    rowConstraint.setMinHeight(20);
                    rowConstraint.setPrefHeight(index == 1 ? Region.USE_COMPUTED_SIZE : 30);
                    rowConstraint.setVgrow(index == 1 ? Priority.ALWAYS : Priority.NEVER);
                    return rowConstraint;
                })
                .collect(Collectors.toList());
        customGridPane.getRowConstraints().setAll(rowConstraints);
        customGridPane.getChildren().setAll(directionLabel, directionCombo,
                directionSelector);
        final StackPane container = new StackPane();
        container.getStyleClass().add("container-pane");
        container.getChildren().add(customGridPane);
        tab.setContent(container);
    }

    private ObservableList<String> createListModel() {
        return FXCollections.observableArrayList("One", "Two", "Three", "Four", "Five");
    }

    private TreeItem<String> createTreeModel() {
        final TreeItem<String> item1 = new TreeItem("One");
        item1.setExpanded(true);
        final TreeItem<String> item2 = new TreeItem("Two");
        item2.setExpanded(true);
        final TreeItem<String> item3 = new TreeItem("Three");
        item3.setExpanded(true);
        final TreeItem<String> item4 = new TreeItem("Four");
        item4.setExpanded(true);
        final TreeItem<String> item5 = new TreeItem("Five");
        item5.setExpanded(true);
        item1.getChildren().setAll(item2);
        item2.getChildren().setAll(item3);
        item3.getChildren().setAll(item4);
        item4.getChildren().setAll(item5);
        return item1;
    }

    private int parseToNumber(final String value) {
        Objects.requireNonNull(value);
        switch (value) {
            case "One":
                return 1;
            case "Two":
                return 2;
            case "Three":
                return 3;
            case "Four":
                return 4;
            case "Five":
                return 5;
        }
        return -1;
    }
}
