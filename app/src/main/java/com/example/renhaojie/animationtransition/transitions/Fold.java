package com.example.renhaojie.animationtransition.transitions;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ren Haojie on 2016/12/16.
 */

public class Fold extends Visibility {
    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createFoldAnimator(view, false);
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createFoldAnimator(view, true);
    }

    public Animator createFoldAnimator(View view, boolean isFolding) {
        int start = view.getTop();
        int end = view.getTop() + view.getMeasuredHeight() - 1;

        if (isFolding) {
            int temp = start;
            start = end;
            end = temp;
        }

        Animator animator = ObjectAnimator.ofInt(view, "bottom", start, end);
        return animator;
    }
}
