package pt.coloryou.utils;

import pt.coloryou.models.TypeModel;

public class ColorPickerUtil {

    public  static TypeModel name(String color){
        color = color.toUpperCase();

        if(color.length() < 3 || color .length() > 7)
            return new TypeModel("#000000", "Invalid Color: " + color,"Preto", false);
        if(color.length() % 3 == 0)
            color = "#" + color;

        int[] rgb = toRGB(color);
        int red = rgb[0], green = rgb[1], blue = rgb[2];
        double[] hsl = toHSL(color);
        double h = hsl[0], s = hsl[1], l = hsl[2];
        double ndf1, ndf2, ndf;
        int cl = -1;double df = -1;

        for(int i = 0; i < names.length; i++)
        {
            if(color.equals("#" + names[i][0]))
                return new TypeModel("#" + names[i][0], names[i][1], names[i][2], true);
            double[] hsl1 = toHSL("#"+names[i][0]);
            ndf1 = Math.pow(red - Integer.parseInt(names[i][0].substring(0, 2), 16), 2) + Math.pow(green - Integer.parseInt(names[i][0].substring(2,4),16), 2) + Math.pow(blue - Integer.parseInt(names[i][0].substring(4, 6),16), 2);
            ndf2 = (Math.pow(h - hsl1[0], 2) + Math.pow(s - hsl1[1], 2) + Math.pow(l - hsl1[2], 2));
            ndf = ndf1 + ndf2 * 2;
            if(df < 0 || df > ndf)
            {
                df = ndf;
                cl = i;
            }
        }

        if(cl < 0)
            return  new TypeModel("#000000", "Invalid Color: " + color,"Preto", false);
        else
            return new TypeModel("#" + names[cl][0], names[cl][1],names[cl][2], false);

    }

    public static int[] rgb2cmyk (int r, int g, int b) {
        double computedC = 0;
        double computedM = 0;
        double computedY = 0;
        double computedK = 0;

        //remove spaces from input RGB values, convert to int


        // BLACK
        if (r==0 && g==0 && b==0) {
            computedK = 1;
            return new int[]{0,0,0,100};
        }

        computedC = 1 - (r/255.0);
        computedM = 1 - (g/255.0);
        computedY = 1 - (b/255.0);

        double minCMY = Math.min(computedC,
                Math.min(computedM,computedY));
        computedC = (computedC - minCMY) / (1 - minCMY) ;
        computedM = (computedM - minCMY) / (1 - minCMY) ;
        computedY = (computedY - minCMY) / (1 - minCMY) ;
        computedK = minCMY;

        return new int[]{(int)(computedC*100),(int)(computedM*100),(int)(computedY*100),(int)(computedK*100)};
    }

    public  static double[] toHSL(String color){
        double[] rgb = new double[3];
        rgb[0] = Integer.parseInt(color.substring(1, 3),16) / 255.0;
        rgb[1] = Integer.parseInt( color.substring(3, 5),16) / 255.0;
        rgb[2] = Integer.parseInt( color.substring(5, 7),16) / 255.0;
        double min, max, delta, h, s, l;
        double r = rgb[0], g = rgb[1], b = rgb[2];

        min = Math.min(r, Math.min(g, b));
        max = Math.max(r, Math.max(g, b));
        delta = max - min;
        l = (min + max) / 2;

        s = 0;
        if(l > 0 && l < 1)
            s = delta / (l < 0.5 ? (2 * l) : (2 - 2 * l));

        h = 0;
        if(delta > 0)
        {
            if (max == r && max != g) h += (g - b) / delta;
            if (max == g && max != b) h += (2 + (b - r) / delta);
            if (max == b && max != r) h += (4 + (r - g) / delta);
            h /= 6;
        }
        return new double[]{(h * 255), (s * 255), (l * 255)};
    }

    public static int[] toRGB(String color){
        int[] rgb = new int[3];
        rgb[0] = Integer.parseInt(color.substring(1,3),16);
        rgb[1] = Integer.parseInt(color.substring(3,5),16);
        rgb[2] = Integer.parseInt(color.substring(5,7),16);
        return rgb;
    }


    private static String[][] names = new String[][]{{"000000", "Preto","Preto"},{"000080", "Marinha","Azul"},{"00008b", "Azul Escuro","Azul"},{"0000cd", "Azul Médio","Azul"},{"0000ff", "Azul","Azul"},{"f0f8ff", "Azul Bebé","Azul"},{"faebd7", "Branco Antigo","Branco"},{"00ffff", "Azul Turquesa Claro","Azul"},{"8a2be2", "Azul Violeta","Violeta"},{"a52a2a", "Castanho","Castanho"},{"dc143c", "Carmesim", "Vermelho"},{"7fffd4", "Turquesa","Azul"},{"5f9ea0", "Azul Cadete","Azul"},{"d2691e", "Chocolate","Castanho"},{"cddf92","Verde Pálido","Verde"},{"deb887", "Castanho Claro","Castanho"},{"ffe4c4", "Creme","Castanho"},{"ffebcd", "Amêndoa Descascada","Castanho"},{"f0ffff", "Azul Celeste","Azul"},{"f5f5dc", "Bege","Branco"},{"7fff00", "Verde Amarelado","Verde"},{"6495ed",  "Azul Claro","Azul"},{"006400", "Verde Escuro","Verde"},{"008b8b", "Ciano Escuro","Azul"},{"ff7f50", "Coral","Laranja"},{"fff8dc", "Cor de Milho","Amarelo"},{"00ffff", "Ciano","Azul"},{"8b0000", "Vermelho Escuro","Vemelho"},{"483d8b", "Azul Arroxeado","Azul"},{"8b008b", "Magenta Escuro","Violeta"},{"2f4f4f", "Cinza Esverdeado","Cinza"},{"9932cc", "Rosa Orquídea","Violeta"},{"556b2f", "Verde Azeitona Escuro","Verde"},{"b8860b", "Dourado Escuro","Amarelo"},{"a9a9a9", "Cinzento Escuro","Cinza"},{"8fbc8f", "Verde Escuro Do Mar","Verde"},{"bdb76b", "Caqui Escuro","Amarelo"},{"00ced1", "Turquesa Escuro","Azul"},{"9400d3", "Violeta Escuro","Violeta"},{"00bfff", "Azul Ciano","Azul"},{"696969", "Cinzento Escuro","Cinza"},{"1e90ff", "Azul Claro","Azul"},{"228b22", "Verde Floresta","Verde"},{"808080", "Cinza","Cinza"},{"808080", "Cinzento","Cinza"},{"008000", "Amarelo","Amarelo"},{"4b0082", "Roxo","Violeta"},{"b22222", "Cor de Tijolo","Castanho"},{"ff8c00", "Laranja Escuro", "Laranja"},{"cd5c5c", "Vermelho Indiano","Vermelho"},{"daa520", "Dourado", "Amarelo"},{"ff69b4", "Rosa Seco","Vermelho"},{"e9967a", "Salmão Escuro", "Laranja"},{"dcdcdc", "Cinzento Claro", "Cinza"},{"ffd700", "Dourado","Amarelo"},{"f8f8ff", "Branco", "Branco"},{"ff1493", "Rosa Choque","Vermelho"},{"fffaf0", "Branco","Branco"},{"ff00ff", "Magenta","Violeta"},{"d7ed4b","Verde","Verde"},{"fefe51","Amarelo","Amarelo"},{"adff2f", "Verde Amarelado","Verde"},{"e6e6fa", "Lavanda","Violeta"},{"f0e68c", "Cáqui","Amarelo"},{"f0fff0", "Branco","Branco"},{"7cfc00", "Lima","Verde"},{"add8e6", "Azul Claro","Azul"},{"d3d3d3", "Cinzento Claro","Cinza"},{"f08080", "Coral Claro","Laranja"},{"fff0f5", "Branco Arroxeado","Branco"},{"fffacd", "Amarelo Claro", "Amarelo"},{"fffff0", "Marfim", "Branco"},{"90ee90", "Verde Claro","Verde"},{"20b2aa", "Azul Mar Claro","Azul"},{"87cefa", "Azul Céu Claro","Azul"},{"778899", "Cinzento Ardósia Claro","Cinza"},{"b0c4de", "Azul Aço Claro","Azul"},{"fafad2", "Amarelo Dourado Claro", "Amarelo"},{"ffa07a", "Salmão Claro", "Laranja"},{"ffb6c1", "Rosa Claro", "Vermelho"},{"ffffe0", "Amarelo Claro","Amarelo"},{"00ff00", "Lima","Verde"},{"800000", "Castanho", "Castanho"},{"32cd32", "Verde Limão", "Verde"},{"ba55d3", "Violeta de Orquídea", "Violeta"},{"66cdaa", "Água Marinha Média", "Azul"},{"9370db", "Roxo Médio", "Violeta"},{"3cb371", "Verde Vivo", "Verde"},{"7b68ee", "Lilás", "Violeta"},{"faf0e6", "Linho", "Branco"},{"00fa9a", "Verde Primavera Médio", "Verde"},{"ff0000", "Vermelho", "Vermelho"},{"c71585", "Rosa", "Vermelho"},{"330a51","Roxo Escuro","Violeta"},{"191970", "Azul da Meia Noite", "Azul"},{"4a1e8a","Roxo Escuro","Violeta"},{"800080", "Roxo", "Violeta"},{"663399", "Roxo", "Violeta"},{"48d1cc", "Turquesa Médio", "Azul"},{"4169e1", "Azul Real", "Azul"},{"8b4513", "Castanho", "Castanho"},{"808000", "Verde Azeitona", "Verde"},{"6b8e23", "Verde", "Verde"},{"2e8b57", "Verde Mar", "Verde"},{"ff00ff", "Magenta", "Violeta"},{"7245b8","Roxo","Violeta"},{"6a5acd", "Azul Ardósia", "Azul"},{"ff4500", "Laranja", "Laranja"},{"a0522d", "Castanho Alaranjado", "Castanho"},{"708090", "Cinzento Ardósia", "Cinza"},{"db7093", "Vermelho Violeta Pálido", "Vermelho"},{"da70d6", "Orquídea","Violeta"},{"cd853f", "Castanho Amarelado", "Castanho"},{"fa8072", "Salmão", "Vermelho"},{"ffa500", "Laranja", "Laranja"},{"bc8f8f", "Castanho Arrosado", "Castanho"},{"eee8aa", "Dourado Pálido", "Amarelo"},{"ffe4e1", "Rosa Claro", "Violeta"},{"ffdead", "Laranja Claro", "Laranja"},{"ffe4b5", "Amarelo Claro", "Amarelo"},{"f5fffa", "Creme De Menta", "Branco"},{"98fb98", "Verde Pálido", "Verde"},{"dda0dd", "Ameixa","Violeta"},{"afeeee", "Turquesa Pálido","Azul"},{"b0e0e6", "Pó Azul","Azul"},{"c0c0c0", "Prata", "Cinza"},{"87ceeb", "Azul Celeste","Azul"},{"f4a460", "Castanho Arenoso", "Castanho"},{"fdf5e6", "Amarelo Muito Claro", "Branco"},{"ffc0cb", "Rosa", "Vermelho"},{"ffdab9", "Pêssego","Amarelo"},{"ffefd5", "Amarelo Claro","Amarelo"},{"fffafa", "Neve","Branco"},{"fff5ee", "Branco Sujo","Branco"},{"00ff7f", "Verde Primavera","Verde"},{"4682b4", "Aço Azul","Azul"},{"008080", "Verde Azulado","Azul"},{"d2b48c", "Dourado Claro", "Castanho"},{"d8bfd8", "Lilás Esbranquiçado", "Violeta"},{"40e0d0", "Turquesa","Azul"},{"9acd32", "Verde Amarelado","Verde"},{"ee82ee", "Violeta","Violeta"},{"f5deb3", "Amarelo Claro","Amarelo"},{"f5f5f5", "Branco Fumo","Branco"},{"f06540","Laranja","Laranja"},{"fd6d2d","Laranja Avermelhado","Laranja"},{"ff6347", "Vermelho Alaranjado","Vermelho"},{"ffff00", "Amarelo","Amarelo"},{"ffffff", "Branco","Branco"}};//Names

}
