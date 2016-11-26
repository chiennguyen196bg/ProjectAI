/*
 *     mlp-java, Copyright (C) 2012 Davide Gessa
 *
 * 	This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Model.*;


/** Pattern recognition of characters (OCR) */
public class _PatternRecognitionTest
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        TestPrecision(50);

		/*int i;
		for(i=0; i <= 200;)
		{
			if(i < 20)
				i++;
			else if(i < 50)
				i+=2;
			else if(i >= 50)
				i+=5;

			System.out.println(i+","+TestPrecision(i));
		}*/
    }

    public static double TestPrecision(int maxit)
    {
        int size = 32;
        double error = 0.0;
        double accouracy = 0.01;
        int nimagesxpatt = 89;
        int npatt = 36;
        int dir = 1;
        int cpatt = 1;
        int numHidden = 100;


        int[] layers = new int[]{ size*size, numHidden/*size*/, npatt };

        String pattern=null;
        try {
            pattern = new File( "." ).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.6, new SigmoidFunction());

        pattern = "/media/hamhochoi/586E4FEE6E4FC40A/Ubuntu/IdeaProjects/NeuralNetWork";

        MultiLayerPerceptron net=null;

        if (net.loadNetwork(pattern + "//network.txt") == null) {
            new MultiLayerPerceptron(layers, 0.6, new SigmoidFunction());
        }
        else{
            net = net.loadNetwork(pattern + "//network.txt");
            System.out.println("Da load du lieu");
        }
		/* Learning */
        for(int i = 0; i < maxit; i++)
        {
            for(int k = 1; k < nimagesxpatt; k++)
            {
                for(int j = 1; j < npatt+1; j++)
                {
                    try {
                        pattern = new File( "." ).getCanonicalPath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    pattern = pattern + "/img/patterns/"+j+"/"+k+".png";
                    double[] inputs = ImageProcessingBW.loadImage(pattern, size, size);

                    if(inputs == null)
                    {
                        System.out.println("Cant find "+pattern);
                        continue;

                    }
                    double[] output = new double[npatt];

                    for(int l = 0; l < npatt; l++)
                        output[l] = 0.0;

                    output[j-1] = 1.0;


                    // Training
                    error = net.backPropagate(inputs, output);
                    System.out.println("Error is "+error+" ("+i+" "+j+" "+k+" )");
                }
            }
        }

        // Save the network
        pattern = "/media/hamhochoi/586E4FEE6E4FC40A/Ubuntu/IdeaProjects/NeuralNetWork";

        net.saveNetwork(pattern + "/network.txt");

        System.out.println("Learning completed!");

		/* Test */
        int correct = 0;

        try {
            pattern = new File( "." ).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[] inputs = ImageProcessingBW.loadImage(pattern + "/img/patterns/7/1.png", size, size);
        double[] output = net.execute(inputs);

        int max = 0;
        for(int i = 0; i < npatt; i++)
            if(output[i] > output[max])
            {
                max = i;
            }

        System.out.println("Maximum accuracy is "+output[max]+"recognized pattern "+(max+1));

		/*
		for(int j = 0; j < npatt; j++)
		{
			for(int k = 1; k < nimagesxpatt; k++)
			{
				double[] inputs = ImageProcessingBW.LoadImage("/home/dak/workspace/MultiLayersPerceptronLib/img/patterns/"+(j+1)+"/"+k+".png", size, size);
				double[] output = net.Execute(inputs);

				for(int i = 0; i < npatt; i++)
					if(output[i] > 0.6)
					{
						System.out.println("Il pattern e' il "+(i+1)+" mi attendevo il "+(j+1)+" ("+output[i]+")");
						if(i == j)
							correct++;
					}
			}
		}
		System.out.println(correct+" results correct of "+(npatt*nimagesxpatt));
		*/
        return (double) ((double) (npatt*nimagesxpatt) - (double) correct) / (double) (npatt*nimagesxpatt);
    }
}
