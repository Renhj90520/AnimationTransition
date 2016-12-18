package com.example.renhaojie.animationtransition;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.renhaojie.animationtransition.transitions.Fold;
import com.example.renhaojie.animationtransition.transitions.Scale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ren Haojie on 2016/12/15.
 */

public class AlbumDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ALBUM_ART_RESID = "EXTRA_ALBUM_ART_RESID";

    @BindView(R.id.album_art)
    ImageView albumArtView;
    @BindView(R.id.fab)
    ImageButton fab;
    @BindView(R.id.title_panel)
    ViewGroup titlePanel;
    @BindView(R.id.track_panel)
    ViewGroup trackPanel;
    @BindView(R.id.detail_container)
    ViewGroup detailContainer;

    private TransitionManager mTransitionManager;
    private Scene mExpandedScene;
    private Scene mCollapsedScene;
    private Scene mCurrentScene;

    @OnClick(R.id.album_art)
    public void onAlbumArtClick(View view) {
//        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.scale);
//        animator.setTarget(fab);
//        animator.start();
//
//        int titleStartValue = titlePanel.getTop();
//        int titleEndValue = titlePanel.getBottom();
//        ObjectAnimator titleAnimatro = ObjectAnimator.ofInt(titlePanel, "bottom", titleStartValue, titleEndValue);
//
//        int trackStartValue = trackPanel.getTop();
//        int trackEndValue = trackPanel.getBottom();
//        ObjectAnimator trackAnimator = ObjectAnimator.ofInt(trackPanel, "bottom", trackStartValue, trackEndValue);
//
//        AnimatorSet set = new AnimatorSet();
//        set.playSequentially(titleAnimatro, trackAnimator);
//        set.start();

        Transition transition = createTransition();
        TransitionManager.beginDelayedTransition(detailContainer, transition);
        titlePanel.setVisibility(View.INVISIBLE);
        trackPanel.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);
    }

    private Transition createTransition() {
        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);

        Transition tFab = new Scale();
        tFab.setDuration(150);
        tFab.addTarget(fab);

        Transition tTitle = new Fold();
        tTitle.setDuration(150);
        tTitle.addTarget(titlePanel);

        Transition tTrack = new Fold();
        tTrack.setDuration(150);
        tTrack.addTarget(trackPanel);

        set.addTransition(tTitle);
        set.addTransition(tTrack);
        set.addTransition(tFab);

        return set;
    }

    @OnClick(R.id.track_panel)
    public void onTrackPanelClick(View view) {

//        TransitionManager.go(expandedScene, set);
        if (mCurrentScene == mCollapsedScene) {
            mCurrentScene = mExpandedScene;
        } else {
            mCurrentScene = mCollapsedScene;
        }

        mTransitionManager.transitionTo(mCurrentScene);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        ButterKnife.bind(this);

        populate();
        setupTransitions();
    }

    private void setupTransitions() {
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.excludeTarget(android.R.id.statusBarBackground, true);
//        getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        getWindow().setEnterTransition(slide);
        getWindow().setSharedElementsUseOverlay(false);
        mTransitionManager = new TransitionManager();
        ViewGroup transitionRoot = detailContainer;

        //Expanded Scene
        mExpandedScene = Scene.getSceneForLayout(transitionRoot, R.layout.activity_album_detail_expanded, this);
        mExpandedScene.setEnterAction(new Runnable() {
            @Override
            public void run() {
                ButterKnife.bind(AlbumDetailActivity.this);
                populate();
                mCurrentScene = mExpandedScene;
            }
        });

//        TransitionSet expandedTransitionSet = new TransitionSet();
//        expandedTransitionSet.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
//        ChangeBounds changeBounds = new ChangeBounds();
//        changeBounds.setDuration(200);
//        expandedTransitionSet.addTransition(changeBounds);
//
//        Fade fadeLyrics = new Fade();
//        fadeLyrics.addTarget(R.id.lyrics);
//        fadeLyrics.setDuration(150);
//        expandedTransitionSet.addTransition(fadeLyrics);

        Transition expandedTransitionSet = TransitionInflater.from(this).inflateTransition(R.transition.expand);

        //CollapsedScene
        mCollapsedScene = Scene.getSceneForLayout(transitionRoot, R.layout.activity_album_detail, this);
        mCollapsedScene.setEnterAction(new Runnable() {
            @Override
            public void run() {
                ButterKnife.bind(AlbumDetailActivity.this);
                populate();
                mCurrentScene = mCollapsedScene;
            }
        });


//        TransitionSet collapsedTransitionSet = new TransitionSet();
//        collapsedTransitionSet.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
//
//        Fade fadeoutLyrics = new Fade();
//        fadeoutLyrics.addTarget(R.id.lyrics);
//        fadeoutLyrics.setDuration(150);
//        collapsedTransitionSet.addTransition(fadeoutLyrics);
//
//        ChangeBounds resetBounds = new ChangeBounds();
//        resetBounds.setDuration(200);
//        collapsedTransitionSet.addTransition(resetBounds);

        Transition collapsedTransitionSet = TransitionInflater.from(this).inflateTransition(R.transition.collapse);

        mTransitionManager.setTransition(mExpandedScene, mCollapsedScene, collapsedTransitionSet);
        mTransitionManager.setTransition(mCollapsedScene, mExpandedScene, expandedTransitionSet);

        mCollapsedScene.enter();
    }

    private void populate() {
        int albumArtId = getIntent().getIntExtra(EXTRA_ALBUM_ART_RESID, R.drawable.mean_something_kinder_than_wolves);
        albumArtView.setImageResource(albumArtId);

        Bitmap albumBitmap = getReducedBitMap(albumArtId);
        colorizeFormImage(albumBitmap);
    }

    private void colorizeFormImage(Bitmap albumBitmap) {
        Palette palette = Palette.from(albumBitmap).generate();
        int defaultPanelColor = 0xFF808080;
        int defaultFabColor = 0xFFEEEEEE;

        titlePanel.setBackgroundColor(palette.getDarkVibrantColor(defaultPanelColor));
        trackPanel.setBackgroundColor(palette.getLightMutedColor(defaultPanelColor));

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled},
                new int[]{android.R.attr.state_pressed}
        };
        int[] colors = new int[]{
                palette.getVibrantColor(defaultFabColor),
                palette.getLightVibrantColor(defaultFabColor)
        };
    }

    private Bitmap getReducedBitMap(int albumArtId) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 8;
        return BitmapFactory.decodeResource(getResources(), albumArtId, options);
    }
}
