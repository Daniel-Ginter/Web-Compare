package web.compare.util;

import org.junit.Test;
import web.compare.util.comparer.PixelComparer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ImageTest {
    public ImageTest(BufferedImage image, String name) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(name);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                var icon = new ImageIcon((image));
                var label = new JLabel();
                label.setIcon(icon);
                frame.getContentPane().add(label, BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        BufferedImage bImage = null;
        try {
            bImage = ImageIO.read(Path.of("C:\\Users\\Speick\\Desktop\\Result\\ls5vs013.cs.tu-dortmund.de\\home\\Chrome_test\\Chrome_test-a-5.png").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage cImage = null;
        try {
            cImage = ImageIO.read(Path.of("C:\\Users\\Speick\\Desktop\\1618863920840.jpg").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new ImageTest(cImage, "vor");
        var greenImg=ImageUtil.shadeImage(cImage,0,25,25);
        try {
            ImageIO.write(greenImg, "png", new File("C:\\Users\\Speick\\Desktop\\Neuer Ordner (2)\\green.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new ImageTest(greenImg, "greenImg");
    //    new ImageTest(bImage, "img1 nach");
        new ImageTest(cImage, "img2");
   //     new ImageTest(pixelbasedimg(bImage, cImage), "pixelDiff");
        // new ImageTest(test(bImage),"test img1");
        // new ImageTest(test(cImage),"test img2");


    }

    public static BufferedImage test(BufferedImage bImage) {
        int bheight = bImage.getHeight();
        int bwidth = bImage.getWidth();
        BufferedImage rImage = new BufferedImage(bwidth, bheight, BufferedImage.TYPE_4BYTE_ABGR);
        for (int y = 0; y < bheight; y++) {
            for (int x = 0; x < bwidth; x++) {
                try {
                    int pixelC = bImage.getRGB(x, y);
                    rImage.setRGB(x, y, pixelC);
                } catch (NullPointerException e) {

                }
            }
        }
        return rImage;
    }

    public static BufferedImage pixelbasedimg(BufferedImage bImage, BufferedImage cImage) {
        int bheight = bImage.getHeight();
        int bwidth = bImage.getWidth();
        int cheight = cImage.getHeight();
        int cwidth = cImage.getWidth();
        BufferedImage rImage = new BufferedImage(bwidth, bheight, BufferedImage.TYPE_4BYTE_ABGR);
        int same = 0;
        int diff = 0;
        int wrongDim = 0;
        // create diff image and count diff pixel
        for (int y = 0; y < bheight; y++) {
            for (int x = 0; x < bwidth; x++) {
                try {
                    //if pixel are the same, set color = basecolor
                    int pixelC = cImage.getRGB(x, y);
                    int pixelB = bImage.getRGB(x, y);
                    if (pixelB == pixelC) {
                        same++;
                        rImage.setRGB(x, y, pixelC);
                    } else {
                        //Case if pixel do not match set pixel to errorcolor
                        int r = 255;
                        int g = 0;
                        int b = 0;
                        int col = (r << 16) | (g << 8) | b;
                        diff++;
                        rImage.setRGB(x, y, 0x80ff0000);
                        //Add different Pixelcounter
                    }
                } catch (NullPointerException e) {
                    // handled height or  width mismatch add different Pixelcounter
                    wrongDim++;
                    rImage.setRGB(x, y, 0x80ff0000);
                }
            }
        }
        System.out.println("Same: " + same);
        System.out.println("Diff: " + diff);
        System.out.println("WrongDim: " + wrongDim);
        return rImage;

    }

}
