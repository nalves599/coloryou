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

public class TestsFragment extends Fragment implements View.OnClickListener {

    private static List<TesteModel> testes;
    private static int NUMBER_OF_TESTS;
    private static int currentTestPos;
    private static int currentAnswerPosition;
    private static List<String> currentAnswers;
    private static int[] buttons = {
            R.id.answer1,
            R.id.answer2,
            R.id.answer3
    };
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.testes_fragment, container, false);

        if (savedInstanceState == null) {
            currentAnswers = new ArrayList<>();
            testes = new ArrayList<>();
            currentTestPos = 0; // Number to Start Tests
            NUMBER_OF_TESTS = getContext().getResources().getInteger(R.integer.number_of_tests);

            parseXML();
            testes = setRandomTests(NUMBER_OF_TESTS);
            // Create Test
            createTeste(testes.get(0), view);
        }
        createPoints(view);
        displayTeste(currentTestPos, view);
        createListeners(view);

        return view;
    }

    private List<TesteModel> setRandomTests(int num) {
        List<TesteModel> newOrderTest = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());
        if (num > testes.size()) {
            num = testes.size();
        }

        for (int i = 0; i < num; i++) {
            int pos = random.nextInt(testes.size());
            newOrderTest.add(testes.get(pos));
            testes.remove(pos);
        }
        return newOrderTest;

    }

    private void createListeners(View view) {
        for (int i = 0; i < buttons.length; i++) {
            Button button = view.findViewById(buttons[i]);
            button.setOnClickListener(this);
        }
    }

    private void displayTeste(int position, View view) {
        TesteModel testeModel = testes.get(position);

        //Get Image And Display
        ImageView question = view.findViewById(R.id.question);
        question.setImageDrawable(getActivity().getResources().getDrawable(getActivity().getResources().getIdentifier(testeModel.question, "drawable", getContext().getPackageName())));

        for (int i = 0; i < buttons.length; i++) {
            Button button = view.findViewById(buttons[i]);
            button.setText(currentAnswers.get(i));
        }

    }

    private void createTeste(TesteModel testeModel, View view) {
        Random random = new Random(System.currentTimeMillis());
        List<AnswerModel> answers = new ArrayList<>();
        List<AnswerModel> answersToChoose = testeModel.answers;
        currentAnswers = new ArrayList<>();

        //Get Correct Answer
        for (int i = 0; i < answersToChoose.size(); i++) {
            if (answersToChoose.get(i).correct) {
                answers.add(answersToChoose.get(i));
                answersToChoose.remove(i);
                break;
            }
        }

        //Get Answers

        for (int i = 0; i < (buttons.length - 1); i++) {
            int pos = random.nextInt(answersToChoose.size());
            answers.add(answersToChoose.get(pos));
            answersToChoose.remove(pos);
        }


        // Display Answers
        for (int i = 0; i < buttons.length; i++) {
            int pos = random.nextInt(answers.size());
            AnswerModel ans = answers.get(pos);
            if (ans.correct) {
                currentAnswerPosition = i;
            }

            currentAnswers.add(ans.text);

            answers.remove(pos);
        }
    }

    private void createPoints(View view) {

        LinearLayout options_layout = view.findViewById(R.id.points);
        options_layout.removeAllViews();
        for (int i = 0; i < NUMBER_OF_TESTS && i < testes.size(); i++) {
            ImageView point = new ImageView(getContext());
            float scaleRatio = getResources().getDisplayMetrics().density;
            int size = (int) (getActivity().getResources().getDimension(R.dimen.point_size));
            size /= scaleRatio;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            point.setLayoutParams(layoutParams);
            if (testes.get(i).status.equals("correct")) {
                point.setImageResource(R.drawable.ic_circle_green);
            } else if (testes.get(i).status.equals("wrong")) {
                point.setImageResource(R.drawable.ic_circle_red);
            } else {
                point.setImageResource(R.drawable.ic_circle_grey);
            }
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
                            String correct;
                            try {
                                correct = parser.getAttributeValue(0);
                            } catch (IndexOutOfBoundsException ex) {
                                correct = "false";
                            }
                            AnswerModel answerModel = new AnswerModel();
                            answerModel.text = parser.nextText();
                            answerModel.correct = (correct != null && correct.equals("true"));
                            currentTeste.answers.add(answerModel);
                        }
                    }
                    break;
            }

            eventType = parser.next();
        }

    }

    private void startText() {

        // If it's not the last test
        if (currentTestPos < testes.size()) {
            createTeste(testes.get(currentTestPos), this.view);
            displayTeste(currentTestPos, this.view);
        } else {
            String[] testesResults = new String[testes.size()];
            for(int i = 0; i< testes.size(); i++){
                testesResults[i] = testes.get(i).status;
            }

            Bundle bundle = new Bundle();
            bundle.putStringArray("testes_results", testesResults);
            Fragment resultFragment = new TestsResultFragment();
            resultFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().remove(getActivity().getSupportFragmentManager().findFragmentByTag(FragmentsEnum.TESTS_FRAGMENT.getValor())).commit();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, resultFragment,FragmentsEnum.TESTS_RESULT_FRAGMENT.getValor()).commit();
        }
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;

        for (int i = 0; i < 3; i++) {
            if (buttons[i] == button.getId()) {
                testes.get(currentTestPos).status = i == currentAnswerPosition ? "correct" : "wrong";

                createPoints(this.view);
                // Next Test
                currentTestPos++;
                startText();
                return;
            }
        }


    }

}
