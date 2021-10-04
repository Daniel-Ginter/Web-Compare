package web.compare.util.comparer;
import web.compare.modell.result.CompareResult;
import web.compare.modell.ElementStatus;
import web.compare.modell.PictureHolder;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public interface Comparer {
    CompareResult compareImages(PictureHolder bHolder, PictureHolder cHolder, String resultFile, ElementStatus elementStatus , BufferedImage fullPageScreenshot,BufferedImage fullPageChangesScreenshot, HashMap<String,String> config);
}
