package web.compare.util.comparer;

import com.github.kilianB.hash
        .Hash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import web.compare.modell.ElementStatus;
import web.compare.modell.PictureHolder;
import web.compare.modell.result.CompareResult;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class AverageColorHash implements Comparer {
    @Override
    public CompareResult compareImages(PictureHolder bHolder, PictureHolder cHolder, String resultFile, ElementStatus elementStatus, BufferedImage siteFullpage,BufferedImage fullPageChangesScreenshot,HashMap<String,String> config) {

        var bImage = bHolder.getImage();
        var cImage = cHolder.getImage();


        HashingAlgorithm hasher = new com.github.kilianB.hashAlgorithms.AverageColorHash(64);

        Hash bHash = hasher.hash(bImage);
        Hash cHash = hasher.hash(cImage);

        double similarityScore = bHash.normalizedHammingDistance(cHash);
        var compareResult = new CompareResult(bHolder.getName(), cHolder.getName(),"", ElementStatus.SAME,"",new HashMap<String,String>(),new HashMap<String,String>());
        if(similarityScore > 0.1){
            compareResult.getScorePriority().put("averageColorHashScore", "Changed" );
        }else {
            compareResult.getScorePriority().put("averageColorHashScore", "Same");
        }
        compareResult.getScore().put("averageColorHashScore", String.valueOf(similarityScore));


        return compareResult;
    }
}
