/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import imageprocessing.utils.Utils;

/**
 *
 * @author Chien Nguyen
 */
public class Segmentation {
    
   /**
    * 
    * @param img black and white image
    * @return array list of line images
    */
    public static ArrayList<BufferedImage> getLines(BufferedImage img) {
        ArrayList<BufferedImage> lines = new ArrayList<>();
        int height = img.getHeight();
        int width = img.getWidth();
        int row1, row2;
        for (row1 = 0; row1 < height; row1++) {
            if (Utils.haveInRow(img, row1)) {
                for (row2 = row1; row2 < height; row2++) {
                    if (!Utils.haveInRow(img, row2) || row2 == height - 1) {
                        BufferedImage line = img.getSubimage(0, row1, width, row2 - row1 + 1);
                        lines.add(line);
                        row1 = row2;
                        break;
                    }
                }
            }
        }
        return lines;
    }

    /**
     * 
     * @param line line image
     * @return array list of word images
     */
    public static ArrayList<BufferedImage> getWords(BufferedImage line) {
//        line = Utils.fit(line);
        ArrayList<BufferedImage> words = new ArrayList<>();
        int height = line.getHeight();
        int width = line.getWidth();

        int[] histogram = createHistogramInLine(line);

        for (int col1 = 0; col1 < width - 1; col1++) {
            if (histogram[col1] == 1) {
                for(int col2 = col1; col2 < width; col2++){
                    if(histogram[col2] == 0){
                        int spaceLength = computeSpaceLength(histogram, col2);
                        if(spaceLength > height/5){
                            BufferedImage word = 
                                    line.getSubimage(col1, 0, col2 - col1, height);
                            words.add(word);
                            col1 = col2 + spaceLength - 1;
                            break;
                        } else {
                            col2 += spaceLength;
                        }
                    }
                    
                    if(col2 == width - 1){
                        BufferedImage word = line.getSubimage(col1, 0, col2 - col1 + 1, height);
                        words.add(word);
                        col1 = col2;
                        break;
                    }
                }
            }
        }

        return words;
    }

    /**
     * 
     * @param word word image
     * @return array list of character images
     */
    public static ArrayList<BufferedImage> getChars(BufferedImage word) {
        word = Utils.fit(word);
        ArrayList<BufferedImage> chars = new ArrayList<>();
        int height = word.getHeight();
        int width = word.getWidth();

        int[] histogram = createHistogramInLine(word);
        for(int col1 = 0; col1 < width-1; col1++){
            if(histogram[col1] == 1){
                for(int col2 = col1 + 1; col2 < width; col2++){
                    if(histogram[col2] == 0 || col2 == width - 1){
                        BufferedImage t_char = 
                                word.getSubimage(col1, 0, col2 - col1 + 1, height);
                        
                        chars.add(t_char);
                        col1 = col2 + 1;
                        break;
                    }
                }
            }
        }
        return chars;
    }

    private static int[] createHistogramInLine(BufferedImage img) {
        int width = img.getWidth();
        int[] histogram = new int[width];
        for (int col = 0; col < width; col++) {
            if (Utils.haveInCol(img, col)) {
                histogram[col]++;
            }
        }
        return histogram;
    }
    
    private static int computeSpaceLength(int[] histogram, int index){
        int length = 0;
        if(histogram[index] == 0){
            for(int i = index, size = histogram.length; i < size; i++){
                if(histogram[i] == 1){
                    length = i - index;
                    break;
                }
                    
            }
        }
        return length;
    }
    
}
