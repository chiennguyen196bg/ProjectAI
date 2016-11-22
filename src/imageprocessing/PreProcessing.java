/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Chien Nguyen
 */
public class PreProcessing {

    // Chuyen doi ve anh den trang
    public static BufferedImage convertToBlackAndWhiteImage(BufferedImage img) {
        BufferedImage blackAndWhiteImage
                = new BufferedImage(
                        img.getWidth(),
                        img.getHeight(),
                        BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = blackAndWhiteImage.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();
        return blackAndWhiteImage;
    }

    
    // làm mảnh (chưa xong)
    public static BufferedImage thin(BufferedImage image) {

        int height = image.getHeight(null);
        int width = image.getWidth(null);

        BufferedImage thinImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = thinImage.createGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, width, height);

        int[][] pixels = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int r = (image.getRGB(i, j) >> 16) & 0xff;
                int g = (image.getRGB(i, j) >> 8) & 0xff;
                int b = image.getRGB(i, j) & 0xff;
                if (r == 255 && g == 255 && b == 255) {
                    pixels[i][j] = 0;
                } else if (r == 0 && g == 0 && b == 0) {
                    pixels[i][j] = 1;
                }
            }
        }

        int v[] = new int[9];
        int nc = 0, nz = 0, start;

        for (int giaidoan = 1; giaidoan <= 2; giaidoan++) {
            for (int i = 1; i < width - 1; i++) {
                for (int j = 1; j < height - 1; j++) {
                    if (pixels[i][j] == 0) //la diem den                          
                    {                  //tìm lân cận                      
                        v[0] = pixels[i][j + 1];
                        v[1] = pixels[i - 1][j + 1];
                        v[2] = pixels[i - 1][j];
                        v[3] = pixels[i - 1][j - 1];
                        v[5] = pixels[i + 1][j - 1];
                        v[6] = pixels[i + 1][j];
                        v[7] = pixels[i + 1][j + 1];
                        nc = -1;
                        start = v[0];
                        nz = 0;
//nz la so diem lan can khac 0                              
//tim so cheo               
                        v[8] = v[0];
                        for (int k = 1; k <= 8; k++) {
                            if (v[k] != start) {
                                nc++;
                            }
                            start = v[k];
                        }
                    }
//tim so phan tu khac 0                             
                    for (int k = 0; k <= 7; k++) {
                        if (v[k] != 0) {
                            nz++;
                        }
                    }
                    if ((giaidoan == 1) && (nc == 1) && (nz >= 2) && (nz <= 6) && (v[0] * v[2] * v[6] == 0) && (v[0] * v[4] * v[6] == 0)) {
                        thinImage.setRGB(i, j, Color.BLACK.getRGB());
                    }
                    if ((giaidoan == 2) && (nc == 1) && (nz >= 2) && (nz <= 6) && (v[0] * v[2] * v[4] == 0) && (v[2] * v[4] * v[6] == 0)) {
                        thinImage.setRGB(i, j, Color.BLACK.getRGB());
                    }
                }
            }
        }
        return thinImage;
    }
}
