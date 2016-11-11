/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing.segmentationobject;

import imageprocessing.utils.Utils;
import java.awt.image.BufferedImage;

/**
 *
 * @author Chien Nguyen
 */
public class MyCharacter {

    private final BufferedImage top;
    private final BufferedImage center;
    private final boolean bottom;

    public MyCharacter(BufferedImage _char) {
        _char = Utils.fit(_char);
        int height = _char.getHeight();
        int width = _char.getWidth();
        int topCenter = 0, bottomCenter = 0;
        for (int row = height / 2; row >= 0; row--) {
            if (!Utils.haveInRow(_char, row)) {
                topCenter = row + 1;
                break;
            }
            if (row == 0) {
                topCenter = row;
            }
        }

        for (int row = height / 2; row < height; row++) {
            if (!Utils.haveInRow(_char, row)) {
                bottomCenter = row - 1;
                break;
            }
            if (row == height - 1) {
                bottomCenter = row;
            }
        }

        center = _char.getSubimage(0, topCenter, width, bottomCenter - topCenter + 1);

        top = Utils.fit(_char.getSubimage(0, 0, width, topCenter));

        bottom = bottomCenter != height - 1;
    }

    public BufferedImage getTop() {
        return top;
    }

    public BufferedImage getCenter() {
        return center;
    }

    public boolean isBottom() {
        return bottom;
    }

}
