/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Chien Nguyen
 */
public class Utils {
    public static BufferedImage fit(BufferedImage image){
        int topRow = 0, bottomRow = 0, leftCol = 0, rightCol = 0;
        int height = image.getHeight();
        int width = image.getWidth();
        
        // find topRow
        for (int row = 0; row < height; row++) {
            if (haveInRow(image, row)) {
                topRow = row;
                break;
            }
        }

        // find bottomRow
        for (int row = height - 1; row > 0; row--) {
            if (haveInRow(image, row)) {
                bottomRow = row;
                break;
            }
        }

        // find leftCol
        for (int col = 0; col < width; col++) {
            if (haveInCol(image, col)) {
                leftCol = col;
                break;
            }
        }

        // find rightCol
        for (int col = width - 1; col > 0; col--) {
            if (haveInCol(image, col)) {
                rightCol = col;
                break;
            }
        }
        
        int newHeight = bottomRow - topRow + 1;
        int newWidth = rightCol - leftCol + 1;
        
        return image.getSubimage(leftCol, topRow, newWidth, newHeight);
    }
    
    public static boolean haveInRow(BufferedImage img, int row){
        for(int col = 0, width = img.getWidth(); col < width; col++){
            Color color = new Color(img.getRGB(col, row));
            if(color.getBlue() == 0)
                return true;
        }
        return false;
    }
    
    public static boolean haveInCol(BufferedImage img, int col){
        for(int row = 0, height = img.getHeight(); row < height; row++){
            Color color = new Color(img.getRGB(col, row));
            if(color.getBlue() == 0)
                return true;
        }
        return false;
    }
}
