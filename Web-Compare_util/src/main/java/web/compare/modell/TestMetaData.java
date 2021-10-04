package web.compare.modell;

public class TestMetaData {
private String testName;
private String stepName;
private String date;

    public TestMetaData(String testName, String stepName, String date) {
        this.testName = testName;
        this.stepName = stepName;
        this.date = date;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
