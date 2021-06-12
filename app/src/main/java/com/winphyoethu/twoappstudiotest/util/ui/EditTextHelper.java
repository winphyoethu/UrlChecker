package com.winphyoethu.twoappstudiotest.util.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import io.reactivex.subjects.PublishSubject;

public class EditTextHelper {

    public PublishSubject<String> bindEditText(EditText editText) {

        PublishSubject<String> editTextSubject = PublishSubject.create();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextSubject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return editTextSubject;
    }

}
