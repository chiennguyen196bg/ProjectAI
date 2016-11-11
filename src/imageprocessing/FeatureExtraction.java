/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Chien Nguyen
 */
public class FeatureExtraction {
    
    // chuyển ảnh thành về vector đặc trung có độ dài height * width
     public static double[] chuanHoa(BufferedImage originalImage, int height, int width) throws IOException {
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        ImageIO.write(resizedImage, "png", new File("C:\\Users\\Chien Nguyen\\Desktop\\test.png"));
        double[] featureArray = new double[height*width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color color = new Color(resizedImage.getRGB(col, row));
                if(color.getBlue() == 0)
                    featureArray[row * width + col] = 1.0;
                else
                    featureArray[row * width + col] = 0.0;
            }
        }
        return featureArray;
    }
}
