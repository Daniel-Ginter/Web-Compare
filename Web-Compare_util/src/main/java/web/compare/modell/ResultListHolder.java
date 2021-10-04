package web.compare.modell;

import java.util.LinkedList;

public class ResultListHolder {
    LinkedList<?> list;
    String environment;
    String mainEnvironment;
    String domain;
    String testName;
    String testStep;
    String date;


    public ResultListHolder(LinkedList<?> list, String environment, String mainEnvironment,String domain) {
        this.list = list;
        this.environment = environment;
        this.mainEnvironment = mainEnvironment;
        this.domain = domain;
    }

    public ResultListHolder(LinkedList<?> list, String environment, String mainEnvironment, String domain, String testName, String testStep, String date) {
        this.list = list;
        this.environment = environment;
        this.mainEnvironment = mainEnvironment;
        this.domain = domain;
        this.testName = testName;
        this.testStep = testStep;
        this.date = date;
    }

    public LinkedList<?> getList() {
        return list;
    }

    public void setList(LinkedList<?> list) {
        this.list = list;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestStep() {
        return testStep;
    }

    public void setTestStep(String testStep) {
        this.testStep = testStep;
    }
}
