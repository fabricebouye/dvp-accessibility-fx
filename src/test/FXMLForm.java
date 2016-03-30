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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Formulaire créé par chargement d'un FXML externe.
 * @author Fabriceb Bouyé
 * @see test.fxml.MyFormController
 */
public final class FXMLForm extends StackPane {

    public FXMLForm() {
        super();
        setId("form");
        try {
            final ResourceBundle bundle = Main.I18N;
            final URL fxmlURL = getClass().getResource("fxml/MyForm.fxml");
            final FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL, bundle);
            final Node form = fxmlLoader.load();
            getChildren().add(form);
        } catch (IOException ex) {
            Logger.getLogger(FXMLForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
