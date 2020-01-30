package pt.coloryou.fragments;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pt.coloryou.R;
import pt.coloryou.models.AnswerModel;
import pt.coloryou.models.TesteModel;

public class TestsFragment extends Fragment {

    ArrayList<TesteModel> testes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.testes_fragment, container, false);

        parseXML();

        createTestes(view,inflater);

        return view;
    }

    private void createTestes(View view,LayoutInflater inflater) {

        LinearLayout options_layout = (LinearLayout) view.findViewById(R.id.points);
        for (TesteModel testeModel : testes) {
            ImageView point = new ImageView(getContext());
            point.setImageResource(R.drawable.ic_circle_grey);
            options_layout.addView(point);
        }


    }

    private void parseXML() {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getResources().openRawResource(R.raw.tests);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            processParsing(parser);

        } catch (XmlPullParserException e) {

        } catch (IOException e) {
        }
    }

    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException {
        int eventType = parser.getEventType();
        TesteModel currentTeste = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("teste".equals(eltName)) {
                        currentTeste = new TesteModel();
                        testes.add(currentTeste);
                    } else if (currentTeste != null) {

                        if ("question".equals(eltName)) {
                            currentTeste.question = parser.nextText();
                        } else if ("answer".equals(eltName)) {
                            String correct = parser.getAttributeValue(0);
                            AnswerModel answerModel = new AnswerModel();
                            answerModel.text = parser.getText();
                            answerModel.correct = (correct != null && correct.equals("true"));
                            currentTeste.answers.add(answerModel);
                        }
                    }
                    break;
            }

            eventType = parser.next();
        }

    }

}