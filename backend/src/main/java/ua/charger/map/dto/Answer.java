package ua.charger.map.dto;

public class Answer<T>  {
    private String error;
    private T elements;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getElements() {
        return elements;
    }

    public void setElements(T elements) {
        this.elements = elements;
    }
}
