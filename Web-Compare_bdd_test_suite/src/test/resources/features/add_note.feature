Feature: Test ToDoMVC
  Scenario:
    Then I clear directory "/Users/Speick/Desktop/Result"
    Then I clear directory "C:\Users\Speick\OneDrive\Webanwendung\server\public\images"
    Then I clear directory "C:\Users\Speick\OneDrive\Webanwendung\server\public\tests"

  Scenario: Fingerprint ToDoMVC after adding toDo bulletpoint "test changes" in Chrome with 1920x1080
    Given I open Chrome with  1920 width and 1080 height
    Given I go to "http://192.168.1.226:8080/examples/react/#/"
    When I add the todo note make "test changes"
    When I finish todo "test changes"
    Then I Fingerprint with Selector "//button,//label,//input,", testName "Test TodoMVCReact", Stepname "test changes",Environment "Chrome_1920_1080_finish_test" at Resultdirectory "/Users/Speick/Desktop/Result"
    Then I close the browser

  Scenario: Fingerprint ToDoMVC after adding toDo bulletpoint "test changes" in Chrome with 600x1080
    Given I open Chrome with  1920 width and 1080 height
    Given I go to "http://192.168.1.226:8080/examples/react-Changed/#/"
    When I add the todo note make "test changes"
    When I finish todo "test changes"
    Then I Fingerprint with Selector "//button,//label,//input,", testName "Test TodoMVCReact", Stepname "test changes",Environment "Chrome_1920_1080_add_test" at Resultdirectory "/Users/Speick/Desktop/Result"
    Then I close the browser

  Scenario: Fingerprint ToDoMVC after adding toDo bulletpoint "test changes" in Chrome with 600x1080
    Given I open Chrome with  1920 width and 1080 height
    Given I go to "http://192.168.1.226:8080/examples/react/#/"
    Then I Fingerprint with Selector "//button,//label,//input,", testName "Test TodoMVCReact", Stepname "test changes",Environment "Chrome_1920_1080" at Resultdirectory "/Users/Speick/Desktop/Result"
    Then I close the browser

  Scenario: Compare above Scenarios
    When I Compare Testname "Test TodoMVCReact", Stepname "test changes" against Mainenvironment "Chrome_1920_1080_add_test" at "/Users/Speick/Desktop/Result"
    Then I  prepare files from Testname "Test TodoMVCReact", Stepname "test changes", source directory "C:\\Users\\Speick\\Desktop\\Result" to Webserver directory "C:\\Users\\Speick\\OneDrive\\Webanwendung\\server\\public\\images"
