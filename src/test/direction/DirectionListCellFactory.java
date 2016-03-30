/* 
 * Copyright (C) 2015-2016 Fabrice Bouyé
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */
package test.direction;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Usine à cellules de liste pour les directions (utilisée dans le FXML).
 * @author Fabriceb Bouyé
 */
public final class DirectionListCellFactory implements Callback<ListView<Direction>, ListCell<Direction>> {

    @Override
    public ListCell<Direction> call(ListView<Direction> param) {
        return new DirectionListCell();
    }
}
