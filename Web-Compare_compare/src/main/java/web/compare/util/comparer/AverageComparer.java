package web.compare.util.comparer;


import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import web.compare.modell.result.CompareResult;
import web.compare.modell.ElementStatus;
import web.compare.modell.PictureHolder;


import java.awt.image.BufferedImage;
import java.util.HashMap;

public class AverageComparer implements Comparer {

    @Override
    public CompareResult compareImages(PictureHolder bHolder, PictureHolder cHolder, String resultFile, ElementStatus elementStatus, BufferedImage siteFullpage,BufferedImage fullPageChangesScreenshot,HashMap<String,String> config) {

        var bImage = bHolder.getImage();
        var cImage = cHolder.getImage();


        HashingAlgorithm hasher = new AverageHash(64);

        Hash bHash = hasher.hash(bImage);
        Hash cHash = hasher.hash(cImage);

        double similarityScore = bHash.normalizedHammingDistance(cHash);
        var compareResult = new CompareResult(bHolder.getName(), cHolder.getName(),"", ElementStatus.SAME,"",new HashMap<String,String>(),new HashMap<String,String>());
        if(similarityScore > 0.1){
            compareResult.getScorePriority().put("averageHashScore", "Changed" );
        }else {
            compareResult.getScorePriority().put("averageHashScore", "Same");
        }
            compareResult.getScore().put("averageHashScore", String.valueOf(similarityScore));


        return compareResult;
    }

}
