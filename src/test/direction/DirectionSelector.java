/* 
 * Copyright (C) 2015 Fabrice Bouyé
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */
package test.direction;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * Contrôle customise qui permet de sélectionner une direction dans une rose des vents.
 * @author Fabriceb Bouyé
 */
public final class DirectionSelector extends Region {

    private static final PseudoClass ACTIVATED = PseudoClass.getPseudoClass("activated");

    private final Region background;
    private final Path north;
    private final Path east;
    private final Path south;
    private final Path west;
    private final Region center;

    /**
     * Constructeur.
     */
    public DirectionSelector() {
        super();
        setId("directionSelector");
        getStyleClass().add("direction-selector");
        setAccessibleRole(AccessibleRole.BUTTON);
        tooltipProperty().addListener(tooltipChangeListener);
        //
        background = new Region();
        background.setId("background");
        background.getStyleClass().add("background");
        // Les 4 flèches.
        north = new Path();
        north.setId("north");
        north.getStyleClass().add("cardinal");
        east = new Path();
        east.setId("east");
        east.getStyleClass().add("cardinal");
        south = new Path();
        south.setId("south");
        south.getStyleClass().add("cardinal");
        west = new Path();
        west.setId("west");
        west.getStyleClass().add("cardinal");
        //
        center = new Region();
        center.setId("center");
        center.getStyleClass().add("center");
        //
        getChildren().addAll(background, north, east, south, west, center);
        // Permet de recevoir les évènements clavier.
        setFocusTraversable(true);
        addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        // Évènements souris.
        north.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        east.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        south.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        west.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        center.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        // Réeagir au changement de valeur de la direction.
        directionProperty().addListener(this::handleDirectionChanged);
    }

    /**
     * Surcharge de la feuille de style par défaut du contrôle (qui sera appliqéee avant la feuille de style de l'app).
     * @return L'emplacement de la feuille de style par default du contrôle.
     */
    @Override
    public String getUserAgentStylesheet() {
        return getClass().getPackage().getName().replaceAll("\\.", "/") + "/DirectionSelector.css";
    }

    // Mise en page.
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final double width = getWidth();
        final double height = getHeight();
        final Insets insets = getInsets();
        final double areaX = insets.getLeft();
        final double areaY = insets.getRight();
        final double areaWidth = Math.max(0, width - (insets.getLeft() + insets.getRight()));
        final double areaHeight = Math.max(0, height - (insets.getTop() + insets.getBottom()));
        final double centerX = areaX + areaWidth / 2d;
        final double centerY = areaY + areaHeight / 2d;
        final double radius = Math.min(areaWidth, areaHeight) / 2d;
        //
        background.resizeRelocate(centerX - radius / 2d, centerY - radius / 2d, radius, radius);
        // Flèche nord.
        north.getElements().clear();
        north.getElements().add(new MoveTo(centerX, centerY));
        north.getElements().add(new LineTo(centerX - 0.08 * radius, centerY));
        north.getElements().add(new LineTo(centerX, centerY - radius));
        north.getElements().add(new LineTo(centerX + 0.08 * radius, centerY));
        north.getElements().add(new ClosePath());
        // Flèche est.
        east.getElements().clear();
        east.getElements().add(new MoveTo(centerX, centerY));
        east.getElements().add(new LineTo(centerX, centerY - 0.08 * radius));
        east.getElements().add(new LineTo(centerX + radius, centerY));
        east.getElements().add(new LineTo(centerX, centerY + 0.08 * radius));
        east.getElements().add(new ClosePath());
        // Flèche sud.
        south.getElements().clear();
        south.getElements().add(new MoveTo(centerX, centerY));
        south.getElements().add(new LineTo(centerX - 0.08 * radius, centerY));
        south.getElements().add(new LineTo(centerX, centerY + radius));
        south.getElements().add(new LineTo(centerX + 0.08 * radius, centerY));
        south.getElements().add(new ClosePath());
        // Flèche ouest.
        west.getElements().clear();
        west.getElements().add(new MoveTo(centerX, centerY));
        west.getElements().add(new LineTo(centerX, centerY - 0.08 * radius));
        west.getElements().add(new LineTo(centerX - radius, centerY));
        west.getElements().add(new LineTo(centerX, centerY + 0.08 * radius));
        west.getElements().add(new ClosePath());
        //
        center.resizeRelocate(centerX - 0.12 * radius, centerY - 0.12 * radius, 0.24 * radius, 0.24 * radius);

    }

    /**
     * Gestion des évènements clavier.
     * @param keyEvent Évènement clavier.
     */
    private void handleKeyPressed(final KeyEvent keyEvent) {
        // Consommer l'évènement est néecessaire ici, car sinon le focus repart sur le composant précédent quand on appuie sur UP par exemple.
        // Note : l'API de gestion du focus doit rendue publique dans le JDK 9, ce qui permettra peut-être de corriger ce soucis.
        switch (keyEvent.getCode()) {
            case UP:
                setDirection(Direction.NORTH);
                keyEvent.consume();
                break;
            case RIGHT:
                setDirection(Direction.EAST);
                keyEvent.consume();
                break;
            case DOWN:
                setDirection(Direction.SOUTH);
                keyEvent.consume();
                break;
            case LEFT:
                setDirection(Direction.WEST);
                keyEvent.consume();
                break;
            default:	
                // Ne rien faire.
        }
    }

    /**
     * Gestion des évènements souris.
     * @param mouseEvent Évènement souris.
     */
    private void handleMousePressed(final MouseEvent mouseEvent) {
        final Node node = (Node) mouseEvent.getSource();
        switch (node.getId()) {
            case "north":
                setDirection(Direction.NORTH);
                break;
            case "east":
                setDirection(Direction.EAST);
                break;
            case "south":
                setDirection(Direction.SOUTH);
                break;
            case "west":
                setDirection(Direction.WEST);
                break;
            default:
                setDirection(Direction.NONE);
        }
        // On tente de récupérer le focus.
        if (!isFocused()) {
            requestFocus();
        }
        mouseEvent.consume();
    }

    /**
     * Réaction aux changements de valeur de la direction.
     * @param observable Valeur observable.
     */
    private void handleDirectionChanged(final Observable observable) {
        final Direction direction = getDirection();
        north.pseudoClassStateChanged(ACTIVATED, direction == Direction.NORTH);
        east.pseudoClassStateChanged(ACTIVATED, direction == Direction.EAST);
        south.pseudoClassStateChanged(ACTIVATED, direction == Direction.SOUTH);
        west.pseudoClassStateChanged(ACTIVATED, direction == Direction.WEST);
    }

    /**
    * Invoqué lorsque l'infobulle change.
    */
    private final ChangeListener<Tooltip> tooltipChangeListener = (observable, oldValue, newValue) -> {
        // Désinstalle l'ancienne infobulle.
        if (oldValue != null) {
            Tooltip.uninstall(this, oldValue);
        }
        // Installe la nouvelle infobulle.
        if (newValue != null) {
            Tooltip.install(this, newValue);
        }
    };

    /**
     * Propriété qui stocke l'infobulle.
     */
    private final ObjectProperty<Tooltip> tooltip = new SimpleObjectProperty<>(this, "tooltip", null);

    public final Tooltip getTooltip() {
        return tooltip.get();
    }

    public final void setTooltip(final Tooltip value) {
        tooltip.set(value);
    }

    public final ObjectProperty<Tooltip> tooltipProperty() {
        return tooltip;
    }

    /**
     * Propriété qui stocke la direction du vent.
     */
    private final ReadOnlyObjectWrapper<Direction> direction = new ReadOnlyObjectWrapper<>(this, "direction", Direction.NONE);

    public final Direction getDirection() {
        return direction.get();
    }

    public final void setDirection(final Direction value) {
        direction.set((value == null) ? Direction.NONE : value);
    }

    public final ReadOnlyObjectProperty<Direction> directionProperty() {
        return direction.getReadOnlyProperty();
    }

    // Support de l'accessiblité.
    @Override
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            // Lorsque l'action FIRE est lancée, on passe à la direction suivante.
            case FIRE: {
                final Direction direction = getDirection();
                final int index = direction.ordinal();
                final int nextIndex = (index + 1) % Direction.values().length;
                final Direction next = Direction.values()[nextIndex];
                setDirection(next);
            }
            break;
            default:
                super.executeAccessibleAction(action, parameters);
        }
    }
}
