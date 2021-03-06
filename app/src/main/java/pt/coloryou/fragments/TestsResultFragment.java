package pt.coloryou.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pt.coloryou.R;
import pt.coloryou.enums.FragmentsEnum;
import pt.coloryou.models.AnswerModel;
import pt.coloryou.models.TesteModel;

public class TestsResultFragment extends Fragment {

    private String[] testesResults;
    private int correct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.testes_result_fragment, container, false);


        if (savedInstanceState == null) {

        }

        testesResults = getArguments().getStringArray("testes_results");
        createPoints(view);
        setMessages(view);


        return view;
    }

    private void setMessages(View view){
        TextView resultText = view.findViewById(R.id.result_text);
        TextView resultTextValue = view.findViewById(R.id.result_text_value);
        ImageView imageView = view.findViewById(R.id.result_image);


        if(0.70 < ((correct*1.0) / testesResults.length)){
            resultText.setText("Parabéns, conseguiste!");
             imageView.setImageResource(R.drawable.smile_happy);
        } else if(0.30 < ((correct*1.0)  / testesResults.length)){
             imageView.setImageResource(R.drawable.smile_sad);
            resultText.setText("Eu sei que consegues melhor.");
        } else {
             imageView.setImageResource(R.drawable.smile_silly);
            resultText.setText("O Color You foi feito para ti.");
        }

        resultTextValue.setText(correct + " / " + testesResults.length);
    }

    private void createPoints(View view) {

        LinearLayout options_layout = view.findViewById(R.id.points);
        options_layout.removeAllViews();
        correct = 0;
        for (int i = 0; i < testesResults.length; i++) {
            ImageView point = new ImageView(getContext());
            float scaleRatio = getResources().getDisplayMetrics().density;
            int size = (int) (getActivity().getResources().getDimension(R.dimen.point_size));
            size /= scaleRatio;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            point.setLayoutParams(layoutParams);
            if (testesResults[i].equals("correct")) {
                correct++;
                point.setImageResource(R.drawable.ic_circle_green);
            } else if (testesResults[i].equals("wrong")) {
                point.setImageResource(R.drawable.ic_circle_red);
            } else {
                point.setImageResource(R.drawable.ic_circle_grey);
            }
            options_layout.addView(point);
        }

    }

}
