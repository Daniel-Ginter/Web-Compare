package web.compare.modell.result;

import web.compare.modell.ElementStatus;

import java.util.HashMap;

public class CompareResult {

    private  int id;
    private String mainElement;
    private String compareElement;
    private ElementStatus elementStatus = ElementStatus.NEW;
    private String resultPicture;
    private HashMap<String,String> score ;
    private HashMap<String,String> scorePriority;
    private String selector;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMainElement() {
        return mainElement;
    }

    public void setMainElement(String mainElement) {
        this.mainElement = mainElement;
    }

    public String getCompareElement() {
        return compareElement;
    }

    public void setCompareElement(String compareElement) {
        this.compareElement = compareElement;
    }

    public ElementStatus getElementStatus() {
        return elementStatus;
    }

    public void setElementStatus(ElementStatus elementStatus) {
        this.elementStatus = elementStatus;
    }

    public HashMap<String, String> getScore() {
        return score;
    }

    public HashMap<String, String> getScorePriority() {
        return scorePriority;
    }

    public void setScorePriority(HashMap<String, String> scorePriority) {
        this.scorePriority = scorePriority;
    }

    public void setScore(HashMap<String, String> score) {
        this.score = score;
    }

    public String getResultPicture() {
        return resultPicture;
    }

    public void setResultPicture(String resultPicture) {
        this.resultPicture = resultPicture;
    }

    public CompareResult(String mainElement, String compareElement,String selector, ElementStatus elementStatus,String resultPicture, HashMap<String,String> score ,HashMap<String,String> scorePriority)  {
        this.mainElement = mainElement;
        this.compareElement = compareElement;
        this.elementStatus = elementStatus;
        this.resultPicture= resultPicture;
        this.score = score;
        this.scorePriority= scorePriority;
        this.selector=selector;
    }

    public CompareResult() {

    }

}
