package com.winphyoethu.twoappstudiotest.util.ui;

import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.subjects.PublishSubject;
import kotlin.Unit;

public class ButtonHelper {

    public PublishSubject<Unit> bindButton(Button button) {
        PublishSubject<Unit> buttonSubject = PublishSubject.create();

        button.setOnClickListener(v -> {
            buttonSubject.onNext(Unit.INSTANCE);
        });

        return buttonSubject;
    }

    public PublishSubject<Unit> bindFabButton(FloatingActionButton button) {
        PublishSubject<Unit> buttonSubject = PublishSubject.create();

        button.setOnClickListener(v -> {
            buttonSubject.onNext(Unit.INSTANCE);
        });

        return buttonSubject;
    }

}
