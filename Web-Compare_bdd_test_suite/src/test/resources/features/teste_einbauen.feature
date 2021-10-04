Feature: Teste das einbauen von Webcompare in eine bestehende BDD-Testsuite
  Scenario:
    Then I clear directory "/Users/Speick/Desktop/Result"
    Then I clear directory "C:\Users\Speick\OneDrive\Webanwendung\server\public\images"
    Then I clear directory "C:\Users\Speick\OneDrive\Webanwendung\server\public\tests"

  Scenario: Open TodoMVC in Chrome
    Given I open Chrome with  1920 width and 1080 height
    When  I go to "http://192.168.1.226:8080/examples/react/#/"
    Then  I should see tag "h1" with innerHtml "todos"
    Then  I Fingerprint with Selector "//a,//button,//label,//form,//input,//select", testName "Test TodoMVCReact", Stepname "Open Website",Environment "Chrome_1920_1080" at Resultdirectory "/Users/Speick/Desktop/Result"

  Scenario: Open TodoMVC in Chrome second time
    Given I open Chrome with  1920 width and 1080 height
    When  I go to "http://192.168.1.226:8080/examples/react/#/"
    Then  I should see tag "h1" with innerHtml "todos"
    Then  I Fingerprint with Selector "//a,//button,//label,//form,//input,//select", testName "Test TodoMVCReact", Stepname "Open Website",Environment "Chrome_1920_1080_2" at Resultdirectory "/Users/Speick/Desktop/Result"

  Scenario: Compare above Scenarios
     When I Compare Testname "Test TodoMVCReact", Stepname "Open Website" against Mainenvironment "Chrome_1920_1080" at "/Users/Speick/Desktop/Result"
     Then I  prepare files from Testname "Test TodoMVCReact", Stepname "Open Website", source directory "C:\\Users\\Speick\\Desktop\\Result" to Webserver directory "C:\\Users\\Speick\\OneDrive\\Webanwendung\\server\\public\\images"

#  Scenario: Open TodoMVC in Chrome
#    Given I open Chrome with  1920 width and 1080 height
#    When  I go to "http://192.168.1.226:8080/examples/react/#/"
#    Then  I should see tag "h1" with innerHtml "todos"