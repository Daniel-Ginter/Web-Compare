package web.compare.util;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import web.compare.fingerprint.Fingerprinter;
import web.compare.fingerprint.SaveMode;

import java.nio.file.Path;
import java.util.HashMap;

@Slf4j
public class TestCapture {
    private static final String uiTestDir = "/Users/Speick/Desktop/Result";

    public static void main(String[] args) {
        log.info("Start Test");
       capture_Chrome();
         //capture_Firfox();

    }

    private static void capture_Chrome() {
        System.setProperty("webdriver.chrome.driver", Path.of("/Users/Speick/OneDrive/Web-Compare/Web-Compare_fingerprint/src/test/resources", "chromedriver.exe").toString());

        HashMap<String, String> config = new HashMap<String, String>();
        config.putIfAbsent("uiTargetDir", "/Users/Speick/Desktop/Result");
        config.putIfAbsent("selector", "a,button,form,input,select");
        config.putIfAbsent("site", "home");
        config.putIfAbsent("testName", "Test ToDoMVc");
        config.putIfAbsent("stepName", "Nothing");
        String[] names = {"Chrome_1900x700", "Chrome_test", "Chrome_test2"};
        for (var name : names
        ) {
            WebDriver webDriverChrome = new ChromeDriver();
            webDriverChrome.manage().window().setSize(new Dimension(1920, 1080));
            webDriverChrome.get("https://todomvc.com/examples/react/#/");
            Fingerprinter fingerprinter = new Fingerprinter(config,webDriverChrome, name, "ls5vs013.cs.tu-dortmund.de/home", SaveMode.REGULAR);
            fingerprinter.takeFingerprint();
            webDriverChrome.close();
        }
    }

    private static void capture_Chrome2() {
        System.setProperty("webdriver.chrome.driver", Path.of("/Users/Speick/OneDrive/Web-Compare/Web-Compare_fingerprint/src/test/resources", "chromedriver.exe").toString());

        HashMap<String, String> config = new HashMap<String, String>();
        config.putIfAbsent("uiTargetDir", "/Users/Speick/Desktop/Result");
        config.putIfAbsent("selector", "a,button,form,input,select");
        config.putIfAbsent("site", "home");
        config.putIfAbsent("testName", "Test test");
        config.putIfAbsent("stepName", "Nothing");
        String[] names = {"Chrome_1900x700", "Chrome_test", "Chrome_test2"};
        for (var name : names
        ) {
            WebDriver webDriverChrome = new ChromeDriver();
            webDriverChrome.manage().window().setSize(new Dimension(1920, 1080));
            webDriverChrome.get("file:///C:/Users/Speick/Desktop/test.html");
            Fingerprinter fingerprinter = new Fingerprinter(config,webDriverChrome, name, "ls5vs013.cs.tu-dortmund.de/home", SaveMode.REGULAR);
            fingerprinter.takeFingerprint();
            webDriverChrome.close();
        }
    }

}
