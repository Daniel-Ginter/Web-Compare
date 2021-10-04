package web.compare.util.comparer;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import web.compare.modell.ResultListHolder;
import web.compare.modell.TestMetaData;
import web.compare.modell.result.CompareResult;
import web.compare.util.JsonUtil;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
@Slf4j
public class PrepareWebresult {

    public static void prepareImages(String resultDirectory,String domain, String targetDirectory) {

        var environments = Path.of(resultDirectory,domain).toFile().list();
        for (var environment : environments) {
            var files =Path.of(resultDirectory,domain,environment).toFile().list();
            try {
                FileUtils.forceMkdir(Path.of(targetDirectory,domain,environment).toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(var file:files){
                if (FilenameUtils.getExtension(file).equals("png")) {
                    try {
                        var image = ImageIO.read(Path.of(resultDirectory,domain,environment,file).toFile());
                        var saveFile = Path.of(targetDirectory,domain,environment,file).toFile();
                        ImageIO.write(image, "png",saveFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            var results =Path.of(resultDirectory,domain,environment,"Result").toFile().list();
            try {
                FileUtils.forceMkdir(Path.of(targetDirectory,domain,environment,"Result").toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(results != null) {
                for (var result : results) {
                    if (FilenameUtils.getExtension(result).equals("png")) {
                        try {
                            var image = ImageIO.read(Path.of(resultDirectory,domain, environment, "Result", result).toFile());
                            var saveFile = Path.of(targetDirectory,domain, environment,"Result", result).toFile();
                            ImageIO.write(image, "png", saveFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }
    
    public static void makeDBJson(String path, String serverDirectory ,String mainEnvironment) {

        var environmentPath = Path.of(path);
        log.info("Environment "+environmentPath+": ");
        for (var environmentResults : environmentPath.toFile().list()) {
            int NEW=0;
            int DELETED=0;
            int MAYBE=0;
            int CHANGED=0;
            int SAME=0;
            int ALL=0;
            try {
                var file= FileUtils.getFile(environmentPath.toString(),environmentResults,environmentResults+"-metadata.json");
                var gson = new Gson();
                var envMetaData = gson.fromJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8), TestMetaData.class);
                var resultListHolder = new ResultListHolder(JsonUtil.loadCompareResult(Path.of(environmentPath.toString(), environmentResults, "Result"),""), environmentResults,mainEnvironment,"domain",envMetaData.getTestName(),envMetaData.getStepName(),envMetaData.getDate());
                for ( var result:resultListHolder.getList()) {
                    var result1 = (CompareResult) result;
                    switch (result1.getElementStatus()){
                        case NEW:
                            ALL++;
                            NEW++;
                            break;
                        case SAME:
                            ALL++;
                            SAME++;
                            break;
                        case MAYBE:
                            ALL++;
                            MAYBE++;
                            break;
                        case CHANGED:
                            ALL++;
                            CHANGED++;
                            break;
                        case DELETED:
                            ALL++;
                            DELETED++;
                            break;
                    }
                }
                log.info("Environment "+resultListHolder.getEnvironment());
                log.info("All "+ALL);
                log.info("New "+ NEW);
                log.info("Deleted "+DELETED);
                log.info("Same "+SAME);
                log.info("Maybe "+MAYBE);
                log.info("Changed "+CHANGED);

                JsonUtil.saveJson(resultListHolder,Path.of(serverDirectory,"tests",envMetaData.getTestName(),envMetaData.getStepName(), environmentResults+"-db").toString());
            }catch (IllegalArgumentException | IOException e){
            }

        }

    }


}
