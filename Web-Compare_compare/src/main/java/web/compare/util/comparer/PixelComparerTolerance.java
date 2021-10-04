package web.compare.util.comparer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.compare.modell.ElementStatus;
import web.compare.modell.PictureHolder;
import web.compare.modell.result.CompareResult;
import web.compare.util.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class PixelComparerTolerance implements Comparer {
    private static Logger log = LoggerFactory.getLogger(PixelComparerTolerance.class);

    @Override
    public CompareResult compareImages(PictureHolder bHolder, PictureHolder cHolder, String resultFile, ElementStatus elementStatus, BufferedImage siteFullPage,BufferedImage fullPageChangesScreenshot,HashMap<String,String> config) {

        var bImage = bHolder.getImage();
        var cImage = cHolder.getImage();
        long pixelDiff = 0;
        int bheight = bImage.getHeight();
        int bwidth = bImage.getWidth();
        int cheight = cImage.getHeight();
        int cwidth = cImage.getWidth();
        boolean sameDim;
        if (bheight == cheight && bwidth == bheight) {
            sameDim = true;
        } else {
            sameDim = false;
        }

        log.info("Vergleiche " + bHolder.getName() + " mit " + cHolder.getName());
        BufferedImage rImage = new BufferedImage(bwidth, bheight, BufferedImage.TYPE_4BYTE_ABGR);
        // create diff image and count diff pixel
        for (int y = 0; y < bheight; y++) {
            for (int x = 0; x < bwidth; x++) {
                try {
                    //if pixel are the same, set color = basecolor
                    int pixelC = cImage.getRGB(x, y);
                    int pixelB = bImage.getRGB(x, y);
                    if (pixelB <= pixelC+10 || pixelB >= pixelC+10 ) {
                        rImage.setRGB(x, y, pixelC);
                    } else {
                        //Case if pixel do not match set pixel to errorcolor
                        rImage.setRGB(x, y, 0x80ff0000);
                        //Add different Pixelcounter
                        pixelDiff++;
                    }
                } catch (NullPointerException e) {
                    // handled height or  width mismatch add different Pixelcounter
                    rImage.setRGB(x, y, 0x80ff0000);

                }
            }
        }
        var compareResult = new CompareResult(bHolder.getName(), cHolder.getName(),bHolder.getMetadata().selector, ElementStatus.SAME, "", new HashMap<String, String>(), new HashMap<String, String>());
        var score = calculateScore(bwidth, bheight, cwidth, cheight, pixelDiff);
        compareResult.getScore().put("pixelScore", Double.toString(score));
        if(score>0.05){
            compareResult.getScorePriority().put("pixelScore", "Changed" );
        }else {
            compareResult.getScorePriority().put("pixelScore", "Same");
        }
        compareResult.setElementStatus(ElementStatus.CHANGED);
        log.info("Pixeldiff is" + pixelDiff);
        try {
            var filename = bHolder.getName();
            var resultFileName = Path.of(resultFile, filename + ".png").toString();
            compareResult.setResultPicture(filename);
            var resultImage = Path.of(resultFileName).toFile();
            log.info("Result gespeichert unter : " + resultImage);
            ImageIO.write(rImage, "png", resultImage);
            var meta = cHolder.getMetadata();
            ImageUtil.mergeImages(meta.x, meta.y, rImage, siteFullPage, resultFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compareResult;
    }

    private double calculateScore(int bwidth, int bheight, int cwidth, int cheight, long diff) {
        var bPixel = bwidth * bheight;
        var cPixel = cwidth * cheight;
        int pixel = 0;
        if (cPixel > bPixel) {
            pixel = cPixel;

        } else {
            pixel = bPixel;
        }

        double score =  diff / pixel;
        return score;
    }
}
