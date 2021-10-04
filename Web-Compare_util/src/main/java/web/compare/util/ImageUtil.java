package web.compare.util;

import org.apache.commons.io.FilenameUtils;
import web.compare.modell.MetadataHelper;
import web.compare.modell.PictureHolder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.file.Path;

public class ImageUtil {
    public static BufferedImage mergeChanges(PictureHolder file, BufferedImage coloredImage, BufferedImage mainFullPage) {
        var meta = file.getMetadata();
        var x = meta.x;
        var y = meta.y;
        var fileForImage = Path.of("changed" + "-fullpage.png").toFile();

        mainFullPage.getGraphics().drawImage(coloredImage, x, y, null);
        return mainFullPage;
    }
    public static BufferedImage mergeChanges(MetadataHelper meta, BufferedImage coloredImage, BufferedImage mainFullPage) {
        var x = meta.x;
        var y = meta.y;
        var fileForImage = Path.of("changed" + "-fullpage.png").toFile();

        mainFullPage.getGraphics().drawImage(coloredImage, x, y, null);
        return mainFullPage;
    }

    public static BufferedImage shadeImage(BufferedImage image, int red, int green, int blue) {
        int width = image.getWidth();
        int height = image.getHeight();
        var out = image;


        // convert to red image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = (p >> 0) & 0xff;

                // set new RGB
                // keeping the r value same as in original
                // image and setting g and b as 0.
                p = (a << 24) | (r * red / 100 << 16) | (g * green / 100 << 8) | b * blue / 100;

                image.setRGB(x, y, p);
            }
        }
        return out;
    }

    public static BufferedImage outlineImage(BufferedImage image, int red, int green, int blue) {
        int width = image.getWidth();
        int height = image.getHeight();
        var out = image;

        for (int y = 0; y < height; y++) {
            setColor(image,red,green,blue,0,y);
            setColor(image,red,green,blue,1,y);
            setColor(image,red,green,blue,width-1,y);
            setColor(image,red,green,blue,width-2,y);
        }
        for (int x = 0; x < width; x++) {
            setColor(image,red,green,blue,x,0);
            setColor(image,red,green,blue,x,1);
            setColor(image,red,green,blue,x,height-1);
            setColor(image,red,green,blue,x,height-2);
        }

        return out;
    }

    private static void setColor(BufferedImage image, int red, int green, int blue, int x, int y) {
        int p = image.getRGB(x, y);
        int a = (p >> 24) & 0xff;
        int r = (p >> 16) & 0xff;
        int g = (p >> 8) & 0xff;
        int b = (p >> 0) & 0xff;

        // set new RGB
        // keeping the r value same as in original
        // image and setting g and b as 0.
        p = (a << 24) | (r * red / 100 << 16) | (g * green / 100 << 8) | b * blue / 100;

        image.setRGB(x, y, p);
    }

    public static BufferedImage clone(BufferedImage bufferImage) {
        ColorModel colorModel = bufferImage.getColorModel();
        WritableRaster raster = bufferImage.copyData(null);
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    public static void mergeImages(int x,int y, BufferedImage coloredImage, BufferedImage mainFullPage, String resultName) {
        var mixedPage = ImageUtil.clone(mainFullPage);
        var fileForImage = Path.of(FilenameUtils.removeExtension(resultName) + "-fullpage.png").toFile();

        mixedPage.getGraphics().drawImage(coloredImage, x, y, null);
        try {
            ImageIO.write(mixedPage, "png", fileForImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
