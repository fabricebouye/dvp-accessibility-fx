/* 
 * Copyright (C) 2015 Fabrice Bouyé
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */
package test.avatar;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import test.Main;

/**
 * Fabrique generatrice de contenu pour le contrôle Pagination.
 * @author Fabrice Bouyé
 */
public final class AvatarPageFactory implements Callback<Integer, Node> {

    private ResourceBundle bundle = Main.I18N;

    @Override
    public Node call(Integer param) {
        ImageView result = null;
        if (param < 4) {
            final String imageName = String.format("avatar_%d.png", param);
            final URL imageURL = getClass().getResource(imageName);
            final Image image = new Image(imageURL.toExternalForm());
            // L'infobulle du contrôle ne s'affiche pas lors du test s'il y a déjà une infobulle sur le Pagination.
            final String tooltipKey = String.format("form.avatar.%d.tip", param);
            final Tooltip tooltip = new Tooltip(bundle.getString(tooltipKey));
            result = new ImageView(image);
            Tooltip.install(result, tooltip);
            result.setId(String.format("avatarImageView%d", param));
        }
        return result;
    }
}
