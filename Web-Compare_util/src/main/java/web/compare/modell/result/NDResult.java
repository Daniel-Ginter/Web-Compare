package web.compare.modell.result;

import web.compare.modell.ElementStatus;

import java.util.HashMap;

public class NDResult {
    private String element;
    private String selector;
    private ElementStatus elementStatus;
    private int id;

    public NDResult(String element, String selector, ElementStatus elementStatus) {
        this.element = element;
        this.selector = selector;
        this.elementStatus = elementStatus;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public ElementStatus getElementStatus() {
        return elementStatus;
    }

    public void setElementStatus(ElementStatus elementStatus) {
        this.elementStatus = elementStatus;
    }


}
