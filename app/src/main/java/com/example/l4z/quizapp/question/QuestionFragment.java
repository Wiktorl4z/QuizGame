package com.example.l4z.quizapp.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.l4z.quizapp.R;
import com.example.l4z.quizapp.activitys.MainActivity;

public class QuestionFragment extends Fragment {

    private static final String questionKey = "question";

    private Question question;
    private TextView mQuestion;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton1, mRadioButton2, mRadioButton3;
    private Button mButtonAnswer;
    private ImageView mImageView;
    private boolean lastQuestion;
    private CheckBox checkbox1, checkbox2, checkbox3, checkbox4;
    private EditText editText;

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout;

        if (question instanceof CheckBoxQuestion) {
            layout = R.layout.fragment_question_checkbox;
        } else if (question instanceof EntryQuestion) {
            layout = R.layout.fragment_question_entry;
        } else {
            layout = R.layout.fragment_question_radio;
        }

        View rootView = inflater.inflate(layout, container, false);

        OnClickListener listener = view -> QuestionFragment.this.onAnswerClick(view);

        mQuestion = (TextView) rootView.findViewById(R.id.questionID);
        mRadioGroup = (RadioGroup) rootView.findViewById(R.id.radioButtonID);
        mButtonAnswer = (Button) rootView.findViewById(R.id.buttonAnswer);
        mRadioButton1 = (RadioButton) rootView.findViewById(R.id.answer1);
        mRadioButton2 = (RadioButton) rootView.findViewById(R.id.answer2);
        mRadioButton3 = (RadioButton) rootView.findViewById(R.id.answer3);
        mImageView = (ImageView) rootView.findViewById(R.id.imageView);

        checkbox1 = (CheckBox) rootView.findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox) rootView.findViewById(R.id.checkbox2);
        checkbox3 = (CheckBox) rootView.findViewById(R.id.checkbox3);
        checkbox4 = (CheckBox) rootView.findViewById(R.id.checkbox4);

        editText = (EditText) rootView.findViewById(R.id.edit_text_entry);

        mButtonAnswer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionFragment.this.showAnswers();
            }
        });

        if (lastQuestion) {
            mButtonAnswer.setVisibility(View.VISIBLE);
        }

        if (question == null && savedInstanceState != null && savedInstanceState.containsKey(questionKey)) {
            question = (Question) savedInstanceState.getSerializable(questionKey);
        }
        mQuestion.setText(question.getQuestion());
        mImageView.setImageResource(question.getResImage());
        if (question instanceof CheckBoxQuestion) {
            checkbox1.setText(question.getAnswer(1));
            checkbox1.setOnClickListener(listener);
            checkbox2.setText(question.getAnswer(2));
            checkbox2.setOnClickListener(listener);
            checkbox3.setText(question.getAnswer(3));
            checkbox3.setOnClickListener(listener);
            checkbox4.setText(question.getAnswer(4));
            checkbox4.setOnClickListener(listener);
        } else if (question instanceof EntryQuestion) {
            editText.setOnClickListener(listener);
        } else {
            mRadioButton1.setText(question.getAnswer(1));
            mRadioButton1.setOnClickListener(listener);
            mRadioButton2.setText(question.getAnswer(2));
            mRadioButton2.setOnClickListener(listener);
            mRadioButton3.setText(question.getAnswer(3));
            mRadioButton3.setOnClickListener(listener);
        }

        return rootView;
    }

    private void showAnswers() {
        MainActivity.getInstance().showFinalScreen();
    }

    private void onAnswerClick(View view) {
        if (view == mRadioButton1 || view == mRadioButton2 || view == mRadioButton3) {
            int checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
            RadioButton viewById = (RadioButton) mRadioGroup.findViewById(checkedRadioButtonId);
            if (viewById != null) {
                checkAnswerRadio(viewById.getText().toString());
            }
        }
        if (view == checkbox1 || view == checkbox2 || view == checkbox3 || view == checkbox4) {
            checkAnswerCheckBox();
        }
        if (view == editText) {
            checkAnswerEntry();
        }
    }

    private void checkAnswerEntry() {
        if (editText.getText() == null || editText.getText().length() == 0){
            question.setAnswered(false);
            question.setAnsweredCorrect(false);
        }
        question.setAnswered(true);
        question.setAnsweredCorrect(question.getCorrectAnswer().equals(editText.getText()));
    }

    private void checkAnswerCheckBox() {
        CheckBoxQuestion q = (CheckBoxQuestion) question;
        boolean[] correct = new boolean[4];
        if (q.getCorrectAnswers().contains(checkbox1.getText())) {
            correct[0] = checkbox1.isChecked();
        } else {
            correct[0] = !checkbox1.isChecked();
        }
        if (q.getCorrectAnswers().contains(checkbox2.getText())) {
            correct[1] = checkbox2.isChecked();
        } else {
            correct[1] = !checkbox2.isChecked();
        }
        if (q.getCorrectAnswers().contains(checkbox3.getText())) {
            correct[2] = checkbox3.isChecked();
        } else {
            correct[2] = !checkbox3.isChecked();
        }
        if (q.getCorrectAnswers().contains(checkbox4.getText())) {
            correct[3] = checkbox4.isChecked();
        } else {
            correct[3] = !checkbox4.isChecked();
        }
        question.setAnswered(true);
        for (boolean b : correct) {
            if (!b) {
                question.setAnsweredCorrect(false);
                return;
            }
        }
        question.setAnsweredCorrect(true);
    }

    private void checkAnswerRadio(String selected) {
        String answ = question.getCorrectAnswer();
        question.setAnswered(true);
        if (answ.equalsIgnoreCase(selected)) {
            question.setAnsweredCorrect(true);
        } else {
            question.setAnsweredCorrect(false);
        }
    }

    public void setLastQuestion(boolean lastQuestion) {
        this.lastQuestion = lastQuestion;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(questionKey, question);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(questionKey)) {
            question = (Question) savedInstanceState.getSerializable(questionKey);
        }
    }
}

