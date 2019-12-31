package pt.coloryou.enums;

public enum FragmentsEnum {

    COLOR_PICKER_FRAGMENT("COLOR_PICKER_FRAGMENT"),
    COLOR_FRAGMENT("COLOR_FRAGMENT"),
    COLOR_ADD_FRAGMENT("COLOR_ADD_FRAGMENT");

    private final String valor;
    FragmentsEnum(String valorOpcao){
        valor = valorOpcao;
    }
    public String getValor(){
        return valor;
    }
}