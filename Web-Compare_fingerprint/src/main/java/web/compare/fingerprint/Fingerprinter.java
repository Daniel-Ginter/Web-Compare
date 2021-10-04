package web.compare.fingerprint;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.*;
import web.compare.modell.TestMetaData;
import web.compare.util.ImageUtil;
import web.compare.util.JsonUtil;
import web.compare.modell.MetadataHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class Fingerprinter {
    private final WebDriver webDriver;
    private final HashMap<String, String> config;
    private final String resultDirectory;
    private final String environment;
    private final String domain;
    private final String path;
    private final String testName;
    private final String stepName;

    public Fingerprinter(HashMap<String, String> config, WebDriver webDriver, String name, String domain, SaveMode saveMode) {
        String environment1;
        log.info("initialize Fingerprinter");
        this.config = config;
        this.webDriver = webDriver;
        this.resultDirectory = config.get("uiTargetDir");
        switch (saveMode) {
            case DATE:
                var date = LocalDateTime.now();
                var dtf = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");
                environment1 = name + dtf.format(date);
            case Version:
                environment1 = name + getVersion();
            default:
                environment1 = name;
        }
        environment = environment1;
        this.domain = domain;
        this.testName = config.get("testName");
        this.stepName = config.get("stepName");

        path = resultDirectory + "/" + testName + "/" + stepName + "/" + name;

    }

    public void takeFingerprint() {

        try {
            log.info("Clear " + resultDirectory);
            FileUtils.forceMkdir(Path.of(path).toFile());
        } catch (IOException e) {
            log.error(e.toString());
        }
        try {
            //     takeScreenshotFull();
            takeScreenshot();
            takeMetadata();
            getUniqueSelector("");
            highLightElement();
            makeMetadata();

        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    private void makeMetadata() {
        var date = LocalDateTime.now();
        var dtf = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");
        var formatedDate = dtf.format(date);
        var fileName = path + "/" + environment + "-metadata";
        JsonUtil.saveJson(new TestMetaData(config.get("testName"), config.get("stepName"), formatedDate), fileName);
    }

    private void highLightElement() {
        var screenShotName = environment + "-site.png";
        BufferedImage siteScreenshot = null;
        try {
            siteScreenshot = ImageIO.read(Path.of(path, screenShotName).toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage highLightScreenshot = ImageUtil.clone(siteScreenshot);
        var list = Path.of(path).toFile().list();
        for (var file : list) {
            if (!file.equals("Result") && !file.toString().equals(screenShotName)) {
                if (FilenameUtils.getExtension(file).equals("png")) {
                    boolean hasMeta = false;
                    boolean hasImage = false;
                    var filename = FilenameUtils.removeExtension(file);
                    MetadataHelper metaData = null;
                    try {
                        metaData = JsonUtil.loadElementJson(path, filename + ".json");
                        hasMeta = true;
                    } catch (IOException e) {
                        hasMeta = false;
                    }
                    BufferedImage elementImage = null;
                    try {
                        elementImage = ImageIO.read(Path.of(path, filename + ".png").toFile());
                        hasImage = true;
                    } catch (IOException e) {
                        hasImage = false;
                    }
                    if (hasMeta && hasImage) {
                        BufferedImage outline;
                        try {
                            outline = ImageUtil.outlineImage(elementImage, 100, 0, 0);
                        } catch (Exception e) {
                            outline = ImageUtil.shadeImage(elementImage, 100, 0, 0);
                        }
                        var clone = ImageUtil.clone(siteScreenshot);
                        ImageUtil.mergeChanges(metaData, outline, highLightScreenshot);
                        ImageUtil.mergeImages(metaData.x, metaData.y, outline, clone, Path.of(path, filename).toString());
                    }


                }
            }
        }
//        try {
//            ImageIO.write(highLightScreenshot, "png", Path.of(path,"highlight.png").toFile());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    private void takeScreenshotFull() {
        log.info("Take Screenshot for " + resultDirectory + "/" + environment);
        Path target = Path.of(path, environment + "-site-Full" + ".png");
        Shutterbug.shootPage(webDriver, Capture.FULL_SCROLL).save(path);

      /*  Screenshot myScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(webDriver);
        try {
            ImageIO.write( myScreenshot.getImage(),"png",target.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
       */
    }

    private void takeScreenshot() {
        try {
            log.info("Take Screenshot for " + resultDirectory + "/" + environment);
            Path target = Path.of(path, environment + "-site" + ".png");
            if (webDriver instanceof TakesScreenshot) {
                final var ts = (TakesScreenshot) webDriver;
                final var img = ts.getScreenshotAs(OutputType.BYTES);
                FileUtils.writeByteArrayToFile(target.toFile(), img);
            }
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    private void takeMetadata() throws Exception {
        String selectorString = config.get("selector");
        String[] split = selectorString.split(",");
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        var counter = new AtomicInteger(0);
        List<MetadataHelper> elements = List.of(split).stream()
                .map(xPath -> webDriver.findElements(By.xpath(xPath)).stream()
                        .map(el -> {
                            var rect = el.getRect();
                            var scriptStructure = ""
                                    + "let el = arguments[0];"
                                    + "const list = ['body'];"
                                    + "while(el.tagName !== 'BODY') {"
                                    + "  const list2 = [];"
                                    + "  let el2 = el;"
                                    + "  while(el2 !== null) {"
                                    + "     if(typeof el2.tagName !== 'undefined'){"
                                    + "         list2.push(el2.tagName.toLowerCase());"
                                    + "     }"
                                    + "      el2 = el2.previousSibling;"
                                    + "}"
                                    + "  list.push(list2.join(' ( '));"
                                    + "  el = el.parentNode;"
                                    + "}"
                                    + "return list.join(' < ');";
                            var scriptVisible = ""
                                    + "let el = arguments[0];"
                                    + "const style = window.getComputedStyle(el);"
                                    + "if (style.display === 'none' || style.visibility === 'hidden') {"
                                    + "return false;"
                                    + "}"
                                    + "return true ;";
                            var makeOuterHtml = ""
                                    + "let el = arguments[0];"
                                    + "const style = window.getComputedStyle(el);"
                                    + "let out = el.tagName.toLowerCase();"
                                    + "if (el.className !== null) {"
                                    + "  out = out + ' <class> '+el.className"
                                    + "}"
                                    + "return out ;";
                            var structure = (String) js.executeScript(scriptStructure, el);
                            var outerHtml = (String) js.executeScript(makeOuterHtml, el);
                            var innerHtml = (String) js.executeScript("return arguments[0].innerHTML;", el);
                            var visible = (Boolean) js.executeScript(scriptVisible, el);


                       /*     try {
                                TimeUnit.SECONDS.sleep(4);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/
                            var xPathName = xPath;
                            if (xPath.contains("/")) {
                                xPathName = xPath.replace("/", "[");
                            }
                            if (visible) {
                                try {
                                    File f = el.getScreenshotAs(OutputType.FILE);
                                    var filename = environment + "-" + el.getTagName() + "-" + counter + ".png";
                                    var filenameShutter = environment + "-" + el.getTagName() + "-" + counter + "-Shutterbug" + ".png";
                                    log.info("Save :" + filename);
                                    FileUtils.copyFile(f, Path.of(path, filename).toFile());
                                    //Shutterbug.shootPage(webDriver).highlight(el).save(Path.of(path, filenameShutter).toString());
                                    counter.getAndIncrement();

                                } catch (Exception e) {
                                    log.error("Konnte SubScreenshot File nicht erstellen" + e);
                                    return new MetadataHelper("I FUCKED UP", structure, outerHtml, innerHtml, structure, visible, rect.x, rect.y, rect.width, rect.height);
                                }
                            }
                            return new MetadataHelper(el.getTagName(), structure, outerHtml, innerHtml, structure, visible, rect.x, rect.y, rect.width, rect.height);

                        })
                        .collect(Collectors.toList())
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var counter2 = 0;
        for (var i = 0; i < elements.size(); i++) {

            var el = elements.get(i);
            if (el.visible&&!el.tag.equals("I FUCKED UP")) {
                var json = gson.toJson(el);
                var filename = environment + "-" + el.tag + "-" + counter2 + ".json";
                counter2++;
                FileUtils.writeStringToFile(Path.of(path, filename).toFile(), json, StandardCharsets.UTF_8);
            }
        }
    }

    private void outer_structure(MetadataHelper metaData, MetadataHelper metaData1, boolean outer, StringBuilder selector) {

        if (outer) {
            selector.insert(0, "outerHtml: " + metaData.outerHtml + " ");
        }
        if (metaData.outerHtml.equals(metaData1.outerHtml)) {
            selector.insert(0, "structure: " + metaData.structure + " ");
        }
    }

    private void structure(MetadataHelper metaData, MetadataHelper metaData1, boolean structure, StringBuilder selector) {
        if (structure) {
            selector.insert(0, "structure: " + metaData.structure + " ");
        }
    }

    private void getUniqueSelector(String mode) {
        var elementFiles = JsonUtil.loadElementJsons(path);
        for (var entry : elementFiles.entrySet()) {
            var metaData = entry.getValue();
            StringBuilder selector = new StringBuilder("innerHtml: " + metaData.innerHtml);
            boolean outer = true;
            boolean structure = true;
            for (var entry1 : elementFiles.entrySet()) {
                if (!entry.getKey().equals(entry1.getKey())) {
                    var metaData1 = entry1.getValue();
                    switch (mode) {
                        case "structure":
                            if (metaData.innerHtml.equals(metaData1.innerHtml)) {
                                structure(metaData, metaData1, structure, selector);

                                structure = false;
                            }
                            break;
                        default:
                            if (metaData.innerHtml.equals(metaData1.innerHtml)) {
                                outer_structure(metaData, metaData1, outer, selector);
                                outer = false;
                            }
                    }
                }
            }
            metaData.selector = selector.toString();
            var gson = new GsonBuilder().setPrettyPrinting().create();
            var json = gson.toJson(metaData);
            try {
                FileUtils.writeStringToFile(Path.of(path, entry.getKey()).toFile(), json, StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("Uhh uniqueSelector Failed hard" + e.toString());
            }
        }
    }

    private int getVersion() {
        return 1 + 1;
    }


}

