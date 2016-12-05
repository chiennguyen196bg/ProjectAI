/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;

import imageprocessing.utils.Utils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Chien Nguyen
 */
public class FeatureExtraction {

//    // chuyển ảnh thành về vector đặc trung có độ dài height * width
//    public static double[] chuanHoa(BufferedImage originalImage, int height, int width) {
//        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
//        Graphics2D g = resizedImage.createGraphics();
//        g.drawImage(originalImage, 0, 0, width, height, null);
//        g.dispose();
////        ImageIO.write(resizedImage, "png", new File("C:\\Users\\Chien Nguyen\\Desktop\\test.png"));
////        BufferedImage resizedImage = scaleImage(originalImage, height, width);
//        double[] featureArray = new double[height * width];
//        for (int row = 0; row < height; row++) {
//            for (int col = 0; col < width; col++) {
//                Color color = new Color(resizedImage.getRGB(col, row));
//                if (color.equals(Color.BLACK)) {
//                    featureArray[row * width + col] = 1.0;
//                } else {
//                    featureArray[row * width + col] = 0.0;
//                }
//            }
//        }
//        return featureArray;
//    }

    public static double[] getFeatureByProjectionHistograms(BufferedImage originalImage, int h, int w) {
        return null;
    }

    public static double[] getFeatureByProfiling(BufferedImage originalImage, int h, int w) {
        return null;
    }

    public static double[] getFeatureByResizingSquare(BufferedImage img, int side) {
        BufferedImage fittedImg = Utils.fit(img);
        int height = fittedImg.getHeight();
        int width = fittedImg.getWidth();
//        int ratio = height / width;
        BufferedImage squareImage = new BufferedImage(side, side, img.getType());
        Graphics2D g = squareImage.createGraphics();
//        if(ratio > 3){
//            g.drawImage(fittedImg.getScaledInstance(side, side, 0), 0,0, null);
//        } else {
        BufferedImage bg = new BufferedImage(height, height, img.getType());
        Graphics2D bgg = bg.createGraphics();
        bgg.setPaint(new Color(255, 255, 255));
        bgg.fillRect(0, 0, bg.getWidth(), bg.getHeight());
        bgg.drawImage(fittedImg, (height - width) / 2, 0, null);
        g.drawImage(bg.getScaledInstance(side, side, 0), 0, 0, null);
//        }
        return convertImageToArray(squareImage);
    }

//    public static BufferedImage scaleImage(BufferedImage src, int width, int height) {
//        Image scaled = src.getScaledInstance(width, height, 0);
//        BufferedImage ret = new BufferedImage(width, height, src.getType());
//        Graphics2D g = ret.createGraphics();
//        g.drawImage(scaled, 0, 0, null);
//        return ret;
//    }

    public static double[] convertImageToArray(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();
        double[] arr = new double[height * width];
        for (int row = 0; row < img.getHeight(); row++) {
            for (int col = 0; col < img.getWidth(); col++) {
                Color color = new Color(img.getRGB(col, row));
                if (color.equals(Color.WHITE)) {
                    arr[row * width + col] = 0.0;
                } else {
                    arr[row * width + col] = 1.0;
                }
            }
        }
        return arr;
    }
}
