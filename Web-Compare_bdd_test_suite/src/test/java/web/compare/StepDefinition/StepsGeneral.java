package web.compare.StepDefinition;

import io.cucumber.java.en.Given;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import web.compare.fingerprint.Fingerprinter;
import web.compare.fingerprint.SaveMode;
import web.compare.modell.MetadataHelper;
import web.compare.util.comparer.ComparerProzess;
import web.compare.util.comparer.PrepareWebresult;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class StepsGeneral {

    protected WebDriver webDriver;

    @Given("I open Firefox with  {int} width and {int} height")
    public void i_open_firefox_with_width_and_height(Integer int1, Integer int2) {
        System.setProperty("webdriver.gecko.driver", Path.of("C:\\Users\\Speick\\Desktop\\driver", "geckodriver.exe").toString());
        WebDriver webDriverFirefox = new FirefoxDriver();
        webDriverFirefox.manage().window().setSize(new Dimension(int1, int2));
        webDriver = webDriverFirefox;
    }
    @Given("I open Edge with  {int} width and {int} height")
    public void i_open_edge_with_width_and_height(Integer int1, Integer int2) {
        System.setProperty("webdriver.edge.driver", Path.of("C:\\Users\\Speick\\Desktop\\driver", "msedgedriver.exe").toString());
        WebDriver webDriverEdge = new EdgeDriver();
        webDriverEdge.manage().window().setSize(new Dimension(int1, int2));
        webDriver = webDriverEdge;
    }

    @Given("I open Chrome with  {int} width and {int} height")
    public void i_open_chrome_with_width_and_height(Integer int1, Integer int2) {
        System.setProperty("webdriver.chrome.driver", Path.of("C:\\Users\\Speick\\Desktop\\driver", "chromedriver.exe").toString());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        WebDriver webDriverChrome = new ChromeDriver(options);
        webDriverChrome.manage().window().setSize(new Dimension(int1, int2));
        webDriver = webDriverChrome;
    }

    @Given("I open Opera with  {int} width and {int} height")
    public void i_open_opera_with_width_and_height(Integer int1, Integer int2) {
        System.setProperty("webdriver.opera.driver", Path.of("C:\\Users\\Speick\\Desktop\\driver", "operadriver.exe").toString());
        WebDriver webDriverOpera = new OperaDriver();
        webDriverOpera.manage().window().setSize(new Dimension(int1, int2));
        webDriver = webDriverOpera;
    }

    @Given("I go to {string}")
    public void i_go_to_website(String string) {
        webDriver.get(string);
    }

    @Given("I add the todo note make {string}")
    public void i_add_the_todo_note_make(String string) {
        var toDoInput = webDriver.findElement(By.ByClassName.className("new-todo"));
        toDoInput.sendKeys(string);
        toDoInput.sendKeys(Keys.ENTER);
    }

    @When("I finish todo {string}")
    public void i_finish_todo(String toDoName) {

        var toDos = webDriver.findElements(By.ByClassName.className("toggle"));
        for (var toDo :
                toDos) {
            System.out.println(toDo.getAttribute("data-reactid"));
            if (toDo.findElement(By.xpath("./following-sibling::label")).getText().equals(toDoName)) {
                //new WebDriverWait(webDriver, 10).until(ExpectedConditions.elementToBeClickable(toDo)).click();
                toDo.click();
            }
        }
    }

    @Then("I Fingerprint with Selector {string}, testName {string}, Stepname {string},Environment {string} at Resultdirectory {string}")
    public void i_fingerprint_with_selector_test_name_stepname_at_resultdirectory(String selector, String testName, String stepName, String environment, String resultDirectory) {
        HashMap<String, String> config = new HashMap<String, String>();
        config.putIfAbsent("selector", selector);
        config.putIfAbsent("uiTargetDir", resultDirectory);
        config.putIfAbsent("testName", testName);
        config.putIfAbsent("stepName", stepName);
        Fingerprinter fingerprinter = new Fingerprinter(config, webDriver, environment, "ls5vs013.cs.tu-dortmund.de/home", SaveMode.REGULAR);
        fingerprinter.takeFingerprint();
    }

    @Then("I Compare Testname {string}, Stepname {string} against Mainenvironment {string} at {string}")
    public void i_compare_testname_stepname_at(String testName, String stepName, String mainEnvrionment, String resultDirectory) {
        var config = new HashMap<String, String>();
        config.put("sourceDirectory", resultDirectory);
        config.put("resultDirectory", resultDirectory);
        config.put("test", testName + "/" + stepName);
        config.put("mainEnviroment", mainEnvrionment);
        config.putIfAbsent("serverDirectory", "c:\\Users\\Speick\\OneDrive\\Webanwendung\\server\\public");
        new ComparerProzess(config).doit();
    }


    @Then("I  prepare files from Testname {string}, Stepname {string}, source directory {string} to Webserver directory {string}")
    public void i_prepare_files_from_testname_stepname_source_directory_to_webserver_directory(String testname, String stepname, String sourceDirectory, String webserverDirectory) {

        PrepareWebresult.prepareImages(sourceDirectory,
                testname + "/" + stepname, webserverDirectory);
    }

    @Then("I close the browser")
    public void i_close_the_browser() {
        webDriver.close();
    }

    @Then("I clear directory {string}")
    public void i_clear_directory(String directory) {
        try {
            FileUtils.deleteDirectory(Path.of(directory).toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Then("I should see tag {string} with innerHtml {string}")
    public void i_should_see_tag_with_inner_html(String tag, String innerHtml) {
        boolean isElementOnSite = false;
        var elements = webDriver.findElements(By.tagName(tag));
        for (var element :
                elements) {
            var innerHtmlAttribute = (String)((JavascriptExecutor)webDriver).executeScript("return arguments[0].innerHTML;", element);
            if (innerHtmlAttribute != null){
                if (innerHtmlAttribute.equals(innerHtml)) {
                    isElementOnSite = true;
                }

            }


        }
        Assert.assertEquals(true, isElementOnSite);
    }
    @Then("test")
    public void test() {
        String selectorString = "//a,//button,//label,//form,//input,//select";
        String[] split = selectorString.split(",");

        List<String> elements = List.of(split).stream()
                .map(xPath -> webDriver.findElements(By.xpath(xPath)).stream()
                        .map(el -> el.getTagName())
                        .collect(Collectors.toList())
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        for (var a: elements){
            log.info(a);
        }
    }

    @Given("I zoom to {int}%")
    public void iZoomTo(int zoom) {
       var ex= (JavascriptExecutor)webDriver;
       ex.executeScript("document.body.style.zoom='"+zoom+"%'");
    }


}
