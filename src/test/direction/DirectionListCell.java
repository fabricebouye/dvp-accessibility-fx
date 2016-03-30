/* 
 * Copyright (C) 2015-2016 Fabrice Bouyé
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */
package test.direction;

import java.util.ResourceBundle;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import test.Main;

/**
 * Cellule de liste pour les directions.
 * @author Fabriceb Bouyé
 */
public final class DirectionListCell extends ListCell<Direction> {

    private final ResourceBundle bundle = Main.I18N;

    public DirectionListCell() {
    }

    @Override
    protected void updateItem(Direction item, boolean empty) {
        super.updateItem(item, empty);
        String text = null;
        Tooltip tooltip = null;
        if (!empty && item != null) {
            final String textKey = String.format("form.direction.%s.label", item.name().toLowerCase());
            text = bundle.getString(textKey);
            final String tooltipKey = String.format("form.direction.%s.tip", item.name().toLowerCase());
            tooltip = new Tooltip(bundle.getString(tooltipKey));
        }
        setText(text);
        setTooltip(tooltip);
    }
}
