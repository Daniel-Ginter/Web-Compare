package web.compare.util.comparer;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.compare.modell.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import web.compare.modell.result.CompareResult;
import web.compare.util.ImageUtil;
import web.compare.util.JsonUtil;


public class ComparerProzess {
    private final String test;
    private final String sourceDirectory;
    private final String mainEnvironment;
    private final String resultDirectory;
    private String[] compare_priority;
    private LinkedList<Comparer> comparers;
    private String serverDirectory;
    private static Logger log = LoggerFactory.getLogger(ComparerProzess.class);
    private String path;
    private LinkedList<Comparer> comparersAll;

    public ComparerProzess(HashMap<String, String> config) {
        sourceDirectory = config.get("sourceDirectory");
        resultDirectory = config.get("resultDirectory");
        mainEnvironment = config.get("mainEnviroment");
        serverDirectory = config.get("serverDirectory");
        test = config.get("test");
        compare_priority = new String[]{"innerHtml",};
        comparers = new LinkedList<Comparer>();
        comparers.add(new PixelComparer());
        comparers.add((new AverageComparer()));
        comparers.add(new AverageColorHash());
        comparers.add(new PerceptiveHash());
        comparersAll = new LinkedList<Comparer>();
        path = sourceDirectory + "/" + test;
    }

    public void doit() {
        compareDomain();
        PrepareWebresult.makeDBJson(path, serverDirectory, mainEnvironment);
    }

    private CompareResult mergeResults(CompareResult result1, CompareResult result2) {
        //merge Scores
        var scores1 = result1.getScore();
        var scores2 = result2.getScore();
        scores1.putAll(scores2);
        result1.setScore(scores1);
        //merge scorePrios
        var scorePrio1 = result1.getScorePriority();
        var scorePrio2 = result2.getScorePriority();
        scorePrio1.putAll(scorePrio2);
        result1.setScorePriority(scorePrio1);
        return result1;
    }

    private ElementStatus checkAndSetStatus(CompareResult result) {
        var prio = result.getScorePriority();
        int changed = 0;
        int same = 0;
        for (var entry :
                prio.entrySet()) {
            if (entry.getValue().equals("Changed")) {
                changed++;
            } else if (entry.getValue().equals("Same")) {
                same++;
            }
        }
        if (changed == 0) {
            return ElementStatus.SAME;
        } else if (changed <= same) {
            return ElementStatus.MAYBE;
        } else {
            return ElementStatus.CHANGED;
        }
    }

    private void mergeImages(PictureHolder file, BufferedImage coloredImage, BufferedImage mainFullPage, String resultName) {
        try {
        var meta = file.getMetadata();
        var x = meta.x;
        var y = meta.y;
        var mixedPage = ImageUtil.clone(mainFullPage);
        var fileForImage = Path.of(resultName + "-fullpage.png").toFile();

        mixedPage.getGraphics().drawImage(coloredImage, x, y, null);

            ImageIO.write(mixedPage, "png", fileForImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void compareDomain() {
        //Get one environment to compare to
        for (var environment : Path.of(path).toFile().list()) {
            if (!environment.equals(mainEnvironment)) {
                int resultCounter = 0;
                var resultPath = Path.of(path, environment, "Result");
                resultPath.toFile().mkdirs();
                //Read all mainEnv json and png
                var mainFiles = getMainFiles();
                BufferedImage mainFullPage = null;
                var filesForScreen = Path.of(path, mainEnvironment).toFile().list();
                for (var file : filesForScreen) {
                    if (file.contains("-site")) {
                        BufferedImage image = null;
                        try {
                            log.info("FullPage Screenshot found");
                            image = ImageIO.read(Path.of(path, mainEnvironment, file).toFile());
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                        mainFullPage = image;
                    }
                }

                HashSet<PictureHolder> files = null;
                //Read all json and png from environment

                try {
                    files = getFiles(environment);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                var main = new PictureHolder();
                //Search each file for mainFile match, by selector
                var result = new CompareResult();
                var changesFullPage = ImageUtil.clone(mainFullPage);
                for (var file : files) {
                    boolean filenew = true;
                    for (var mainFile : mainFiles) {
                        if (file.getMetadata() != null && mainFile.getMetadata() != null) {
                            if (file.getMetadata().selector.equals(mainFile.getMetadata().selector)) {
                                main = mainFile;
                                var pictureName = mainFile.getName();
                                var resultName = makeResultname(resultPath.toString(), resultCounter, pictureName);
                                boolean first = true;
                                var changedCounter = 0;
                                var config = new HashMap<String, String>();
                                for (var comparer : comparers) {
                                    if (first) {
                                        result = comparer.compareImages(mainFile, file, resultPath.toString(), ElementStatus.SAME, mainFullPage, changesFullPage, config);
                                        if (result.getElementStatus().equals(ElementStatus.CHANGED)) {
                                            changedCounter++;
                                        }
                                        first = false;
                                    } else {
                                        var result2 = comparer.compareImages(mainFile, file, resultPath.toString(), ElementStatus.SAME, mainFullPage, changesFullPage, config);
                                        if (result2.getElementStatus().equals(ElementStatus.CHANGED)) {
                                            changedCounter++;
                                        }
                                        result = mergeResults(result, result2);

                                    }
                                }
                                result.setElementStatus(checkAndSetStatus(result));
                                if (result.getElementStatus().equals(ElementStatus.CHANGED)) {
                                    resultName = resultName + "-CHANGED";
                                } else if (result.getElementStatus().equals(ElementStatus.SAME)) {
                                    resultName = resultName + "-SAME";
                                }
                                JsonUtil.saveJson(result, resultName);
                                filenew = false;
                            }
                        }
                    }
                    //file had no match, element is new
                    if (filenew && !(file.getName().contains("-site"))  &&!(file.getName().contains("highlight"))) {
                        var pictureName = file.getName();
                        var resultName = makeResultname(resultPath.toString(), resultCounter, pictureName);
                        var resultFiles = new HashMap<String, String>();
                        resultFiles.put(pictureName, resultPath.toString());
                        mergeImages(file, ImageUtil.shadeImage(file.getImage(), 0, 100, 0), mainFullPage, resultName);
                        ImageUtil.mergeChanges(file, ImageUtil.shadeImage(file.getImage(),0, 100, 0), changesFullPage);
                        JsonUtil.saveJson(new CompareResult("", file.getName(), file.getMetadata().selector, ElementStatus.NEW, "", new HashMap<String, String>(), new HashMap<String, String>()), resultName + "-NEW");
                    }
                    mainFiles.remove(main);
                }
                //mainfile had no match, so element was deleted
                for (var mainFile : mainFiles) {
                    var pictureName = mainFile.getName();
                    var resultName = makeResultname(resultPath.toString(), resultCounter, pictureName);
                    var resultFiles = new HashMap<String, String>();
                    resultFiles.put(pictureName, resultPath.toString());
                    mergeImages(mainFile, ImageUtil.shadeImage(mainFile.getImage(), 100, 0, 0), mainFullPage, resultName);
                    ImageUtil.mergeChanges(mainFile, ImageUtil.shadeImage(mainFile.getImage(), 100, 0, 0), changesFullPage);
                    JsonUtil.saveJson(new CompareResult("", mainFile.getName(), mainFile.getMetadata().selector, ElementStatus.DELETED, "", new HashMap<String, String>(), new HashMap<String, String>()), resultName + "-DELETED");

                }
                try {
                    ImageIO.write(changesFullPage, "png", Path.of(resultPath.toString(),"changesFullpage.png").toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String makeResultname(String resultPath, int resultCouter, String name) {
        return resultPath + "/" + name;
        // return resultPath + "/" + resultCouter + "-" + name;
    }

    private Boolean sameSize(PictureHolder bImageHolder, PictureHolder cImageHolder) {
        var bImage = bImageHolder.getImage();
        var cImage = cImageHolder.getImage();
        boolean same = false;
        if (bImage.getHeight() == cImage.getHeight() && bImage.getWidth() == cImage.getWidth()) {
            same = true;
        }
        return same;
    }


    private HashSet<PictureHolder> getMainFiles() {
        var out = new HashSet<PictureHolder>();

        var files = Path.of(path, mainEnvironment).toFile().list();
        if (files == null) {
            log.error("mainEnviroment was empty/not found");
        }
        for (var file : files) {
            if (FilenameUtils.getExtension(file).equals("png")) {
                var filename = FilenameUtils.removeExtension(file);
                MetadataHelper metaData = null;
                if (!filename.contains("site") && !filename.contains("fullpage")) {
                    try {
                        metaData = JsonUtil.loadElementJson(path + "/" + mainEnvironment, filename + ".json");
                    } catch (FileNotFoundException e) {
                        if (filename.contains("site")) {
                            log.info("site hat keine json: " + filename);
                        } else {
                            log.error("Keine Json für : " + filename);
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                    BufferedImage image = null;
                    try {
                        image = ImageIO.read(Path.of(path, mainEnvironment, filename + ".png").toFile());
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                    var split = filename.split("-");
                    out.add(new PictureHolder(metaData, image, filename));
                }
            }
        }
        return out;
    }

    private HashSet<PictureHolder> getFiles(String enviroment) throws IOException {
        var out = new HashSet<PictureHolder>();
        if (!enviroment.toString().equals(mainEnvironment)) {
            for (var file : Path.of(resultDirectory, test, enviroment).toFile().list()) {
                if (FilenameUtils.getExtension(file).equals("png")) {
                    var filename = FilenameUtils.removeExtension(file);
                    MetadataHelper metaData = null;
                    if (!filename.equals("Result") && !filename.contains("fullpage")) {
                        try {
                            metaData = JsonUtil.loadElementJson(path + "/" + enviroment, filename + ".json");
                        } catch (FileNotFoundException e) {
                            if (filename.contains("site")) {
                                log.info("site hat keine json: " + filename);
                            } else {
                                log.error("Keine Json für : " + filename);
                            }
                        }
                        var image = ImageIO.read(Path.of(path, enviroment, filename + ".png").toFile());
                        out.add(new PictureHolder(metaData, image, filename));
                        log.debug("----" + file);
                    }
                }
            }
        }
        return out;
    }

}