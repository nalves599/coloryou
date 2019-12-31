package pt.coloryou.enums;

public enum ErrorEnum {

    CREATE_IMAGE("Erro ao guardar imagem");

    private final String valor;
    ErrorEnum(String valorOpcao){
        valor = valorOpcao;
    }
    public String getValor(){
        return valor;
    }
}
