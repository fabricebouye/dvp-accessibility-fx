/* 
 * Copyright (C) 2015 Fabrice Bouyé
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */
package test.fxml;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import test.direction.Direction;
import test.direction.DirectionSelector;

/**
 * Contrôleur du FXML.
 * @author Fabriceb Bouyé
 */
public final class MyFormController implements Initializable {

    @FXML
    private Label nameLabel;
    @FXML
    private TextField nameField;
    @FXML
    private Label surnameLabel;
    @FXML
    private TextField surnameField;
    @FXML
    private Label birthdayLabel;
    @FXML
    private DatePicker birthdayPicker;
    @FXML
    private Label sexLabel;
    @FXML
    private ToggleGroup sexToggleGroup;
    @FXML
    private RadioButton sexFemaleRadio;
    @FXML
    private RadioButton sexMaleRadio;
    @FXML
    private Label kidsLabel;
    @FXML
    private Spinner<Integer> kidsSpinner;
    @FXML
    private Label colorLabel;
    @FXML
    private ColorPicker colorPicker;    
    @FXML
    private Label avatarLabel;
    @FXML
    private Pagination avatarPagination;    
    @FXML
    private ListView<String> dataListView;
    @FXML
    private TreeView<String> dataTreeView;
    @FXML
    private TableView<String> dataTableView;
    @FXML
    private TableColumn<String, String> dataTableViewCol0;
    @FXML
    private TableColumn<String, String> dataTableViewCol1;
    @FXML
    private TreeTableView<String> dataTreeTableView;
    @FXML
    private TreeTableColumn<String, String> dataTreeTableViewCol0;
    @FXML
    private TreeTableColumn<String, Number> dataTreeTableViewCol1;
    @FXML
    private Label directionLabel;
    @FXML
    private ComboBox<Direction> directionCombo;
    @FXML
    private DirectionSelector directionSelector;

    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        // Suite à un défaut dans le FXMLLoader, on doit attacher manuellement les labels à leur controles respectifs.
        nameLabel.setLabelFor(nameField);
        surnameLabel.setLabelFor(surnameField);
        birthdayLabel.setLabelFor(birthdayPicker);
        sexLabel.setLabelFor((Node) sexToggleGroup.getToggles().get(0));
        kidsLabel.setLabelFor(kidsSpinner);
        colorLabel.setLabelFor(colorPicker);
        directionLabel.setLabelFor(directionCombo);
        directionLabel.setLabelFor(directionCombo);
        // Actuellement, l'API ne permet pas de modifier le prompt text sur le spinner.
        // Et le FXML ne permet pas de specifier un éditeur aisément.
        // Il faut donc accéder à son éditeur.
        kidsSpinner.getEditor().setPromptText(bundle.getString("form.kids.prompt"));
        // Bug dans le spinner : l'infobulle n'est pas reportée sur l'éditeur.
        // Voir : test.bugs.SpinnerTooltipBug
        kidsSpinner.getEditor().setTooltip(new Tooltip(bundle.getString("form.kids.tip")));
        // Il semble que la définition de la factory ne soit pas possible depuis le FXML.
        // La factory entière va cacher le prompt text avec la valeur par défaut 0.
        // Le prompt text est surtout utile quand on à un spinner d'objets.
        kidsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        // Liste.
        // Table.
        dataTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        dataTableViewCol0.setCellValueFactory(feature -> {
            final String value = feature.getValue();
            return new SimpleObjectProperty(value);
        });
        dataTableViewCol1.setCellValueFactory(feature -> {
            final String value = feature.getValue();
            final Number number = parseToNumber(value);
            return new SimpleObjectProperty(number);
        });
        // Arbre.
        // Arbre-table.
        dataTreeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        dataTreeTableViewCol0.setCellValueFactory(feature -> {
            final TreeItem<String> item = feature.getValue();
            final String value = item.getValue();
            return new SimpleObjectProperty(value);
        });
        dataTreeTableViewCol1.setCellValueFactory(feature -> {
            final TreeItem<String> item = feature.getValue();
            final String value = item.getValue();
            final Number number = parseToNumber(value);
            return new SimpleObjectProperty(number);
        });
        //
        directionCombo.getItems().setAll(Direction.values());
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

    private boolean isDirectionEditing = false;
}
