Feature: Test ToDoMVC
  Scenario:
    Then I clear directory "/Users/Speick/Desktop/Result"
    Then I clear directory "C:\Users\Speick\OneDrive\Webanwendung\server\public\images"
    Then I clear directory "C:\Users\Speick\OneDrive\Webanwendung\server\public\tests"

  Scenario: Fingerprint ToDoMVC after adding toDo bulletpoint "Cake good" in Chrome with 1920x1080
    Given I open Chrome with  1920 width and 1080 height
    Given I go to "https://ToDoMVC.com/examples/react/#/"
    Given I add the todo note make "Cake good"
    Given I finish todo "Cake good"
    When I Fingerprint with Selector "a,button,label,form,input,select", testName "BDD ToDoMVC", Stepname "Cake good",Environment "Chrome_1920_1080" at Resultdirectory "/Users/Speick/Desktop/Result"
    When I close the browser
