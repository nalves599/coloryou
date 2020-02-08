package pt.coloryou.models;

public class TypeModel {
    private String color;
    private String error;
    private String colorName;
    private boolean pass;

    public TypeModel(String color, String error, String colorName, boolean pass){
        this.color = color;
        this.error = error;
        this.colorName = colorName;
        this.pass = pass;
    }

    public TypeModel(String color, boolean pass){
        this.color = color;
        this.error = " ";
        this.colorName = " ";
        this.pass = pass;
    }

    public String getColor() {
        return color;
    }

    public String getError() {
        return error;
    }

    public boolean isPass() {
        return pass;
    }

    public String getColorName() {
        return colorName;
    }
}