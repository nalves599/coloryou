package pt.coloryou.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import pt.coloryou.R;
import pt.coloryou.models.TypeModel;
import pt.coloryou.utils.ColorPickerUtil;

public class ColorFragment extends Fragment {
    private String pickedColor;
    private String colorName;
    private String primaryColorName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.color_fragment, container, false);

        this.pickedColor = getArguments().getString("color");

        TypeModel colorTypeModel = ColorPickerUtil.name(this.pickedColor);
        this.colorName = colorTypeModel.getError();
        this.primaryColorName = colorTypeModel.getColorName();

//        Toast.makeText(getActivity(), "Color" + pickedColor+  ", " + colorName + ". " + primaryColorName, Toast.LENGTH_LONG).show();

        int[] RGB = ColorPickerUtil.toRGB(this.pickedColor);
        float[] HSL = new float[3];
        DecimalFormat df = new DecimalFormat("#");
        int[] CMYK = ColorPickerUtil.rgb2cmyk(RGB[0], RGB[1], RGB[2]);
        android.graphics.Color.RGBToHSV(RGB[0], RGB[1], RGB[2], HSL);

        ImageView color = view.findViewById(R.id.color);
        TextView primaryColorName = view.findViewById(R.id.primary_color_name);
        TextView colorName = view.findViewById(R.id.color_name);
        TextView rgb = view.findViewById(R.id.rgb);
        TextView hex = view.findViewById(R.id.hex);
        TextView hsl = view.findViewById(R.id.hsl);
        TextView cmyk = view.findViewById(R.id.cmyk);
        ImageView colorAdd = view.findViewById(R.id.color_add);

        color.setBackgroundColor(android.graphics.Color.parseColor(pickedColor));
        colorName.setText(colorTypeModel.getError());
        primaryColorName.setText(colorTypeModel.getColorName());
        rgb.setText("( " + RGB[0] + " , " + RGB[1] + " , " + RGB[2] + " )");
        hex.setText(pickedColor);
        hsl.setText("( " + df.format(HSL[0]) + "º , " + df.format(HSL[1] * 100) + "% , " + df.format(HSL[2] * 100) + "% )");
        cmyk.setText("( " + CMYK[0] + "% , " + CMYK[1] + "% , " + CMYK[2] + "% , " + CMYK[3] + "% )");
        colorAdd.setImageResource(getColorAddImage(this.primaryColorName));

        return view;
    }

    /*PRIVATE METHODS*/
    private int getColorAddImage(String colorAddName) {
        int img;
        switch (colorAddName) {
            case "Vermelho":
                img = R.drawable.vermelho;
            case "Amarelo":
                img = R.drawable.amarelo;
                break;
            case "Laranja":
                img = R.drawable.laranja;
                break;
            case "Verde":
                img = R.drawable.verde;
                break;
            case "Azul":
                img = R.drawable.azul;
                break;
            case "Violeta":
                img = R.drawable.violeta;
                break;
            case "Castanho":
                img = R.drawable.castanho;
                break;
            case "Cinza":
                img = R.drawable.cinza;
                break;
            case "Branco":
                img = R.drawable.branco;
                break;
            case "Preto":
                img = R.drawable.preto;
                break;
            default:
                Toast.makeText(getContext(),"Color Add Image Not Found", Toast.LENGTH_SHORT).show();
                return -1;
        }

        return img;
    }
}
