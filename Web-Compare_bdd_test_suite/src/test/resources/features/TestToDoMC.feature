Feature: Test ToDoMVC
  Scenario:
    Then I clear directory "/Users/Speick/Desktop/Result"
    Then I clear directory "C:\Users\Speick\OneDrive\Webanwendung\server\public\images"
    Then I clear directory "C:\Users\Speick\OneDrive\Webanwendung\server\public\tests"
    
  Scenario: Fingerprint ToDoMVC after adding toDo bulletpoint "Cake good" in Chrome with 1920x1080
    Given I open Chrome with  1920 width and 1080 height
    Given I go to "https://ToDoMVC.com/examples/react/#/"
    When I add the todo note make "Cake good"
    When I finish todo "Cake good"
    Then I Fingerprint with Selector "//a,//button,//label,//form,//input,//select", testName "BDD ToDoMVC", Stepname "Cake good",Environment "Chrome_1920_1080" at Resultdirectory "/Users/Speick/Desktop/Result"
    Then I close the browser

  Scenario: Fingerprint ToDoMVC after adding toDo bulletpoint "Cake good" in Chrome with 600x1080
    Given I open Chrome with  600 width and 1080 height
    Given I go to "https://ToDoMVC.com/examples/react/#/"
    When I add the todo note make "Cake good"
    When I finish todo "Cake good"
    Then I Fingerprint with Selector "//a,//button,//label,//form,//input,//select", testName "BDD ToDoMVC", Stepname "Cake good",Environment "Chrome_600_1080" at Resultdirectory "/Users/Speick/Desktop/Result"
    Then I close the browser

  Scenario: Fingerprint ToDoMVC after adding toDo bulletpoint "Cake good" in Chrome with 600x600
    Given I open Chrome with  600 width and 400 height
    Given I go to "https://ToDoMVC.com/examples/react/#/"
    When I add the todo note make "Cake good"
    Then I Fingerprint with Selector "//a,//button,//label,//form,//input,//select", testName "BDD ToDoMVC", Stepname "Cake good",Environment "Chrome_600_600" at Resultdirectory "/Users/Speick/Desktop/Result"
    Then I close the browser

  Scenario: Fingerprint ToDoMVC after adding toDo bulletpoint "Cake good" in Chrome with 1920x1080_compare
    Given I open Chrome with  1920 width and 1080 height
    Given I go to "https://ToDoMVC.com/examples/react/#/"
    When I add the todo note make "Cake good"
    Then I Fingerprint with Selector "//a,//button,//label,//form,//input,//select", testName "BDD ToDoMVC", Stepname "Cake good",Environment "Chrome_1920_1080_compare" at Resultdirectory "/Users/Speick/Desktop/Result"
    Then I close the browser

  Scenario: Comapre
    Then I Compare Testname "BDD ToDoMVC", Stepname "Cake good" against Mainenvironment "Chrome_1920_1080" at "/Users/Speick/Desktop/Result"

  Scenario: Prepare Webresult
    Then I  prepare files from Testname "BDD ToDoMVC", Stepname "Cake good", source directory "C:\\Users\\Speick\\Desktop\\Result" to Webserver directory "C:\\Users\\Speick\\OneDrive\\Webanwendung\\server\\public\\images"



  Scenario: Fingerprint ToDoMVC after adding toDo bulletpoint "Cake bad" in Chrome with 1920x1080
    Given I open Chrome with  1920 width and 1080 height
    Given I go to "https://ToDoMVC.com/examples/react/#/"
    When I add the todo note make "Cake bad"
    When I finish todo "Cake bad"
    Then I Fingerprint with Selector "//a,//button,//label,//form,//input,//select", testName "BDD ToDoMVC", Stepname "Cake bad",Environment "Chrome_1920_1080" at Resultdirectory "/Users/Speick/Desktop/Result"
    Then I close the browser

  Scenario: Fingerprint ToDoMVC after adding toDo bulletpoint "Cake good" in Chrome with 600x1080
    Given I open Chrome with  600 width and 1080 height
    Given I go to "https://ToDoMVC.com/examples/react/#/"
    When I add the todo note make "Cake bad"
    When I finish todo "Cake bad"
    Then I Fingerprint with Selector "//a,//button,//label,//form,//input,//select", testName "BDD ToDoMVC", Stepname "Cake bad",Environment "Chrome_600_1080" at Resultdirectory "/Users/Speick/Desktop/Result"
    Then I close the browser

  Scenario: Fingerprint ToDoMVC after adding toDo bulletpoint "Cake good" in Chrome with 600x1080_2
    Given I open Chrome with  600 width and 1080 height
    Given I go to "https://ToDoMVC.com/examples/react/#/"
    When I add the todo note make "Cake bad"
    When I finish todo "Cake bad"
    Then I Fingerprint with Selector "//a,//button,//label,//form,//input,//select", testName "BDD ToDoMVC", Stepname "Cake bad",Environment "Chrome_600_1080_2" at Resultdirectory "/Users/Speick/Desktop/Result"
    Then I close the browser

  Scenario: Comapre
    Then I Compare Testname "BDD ToDoMVC", Stepname "Cake bad" against Mainenvironment "Chrome_1920_1080" at "/Users/Speick/Desktop/Result"

  Scenario: Prepare Webresult
    Then I  prepare files from Testname "BDD ToDoMVC", Stepname "Cake bad", source directory "C:\\Users\\Speick\\Desktop\\Result" to Webserver directory "C:\\Users\\Speick\\OneDrive\\Webanwendung\\server\\public\\images"


