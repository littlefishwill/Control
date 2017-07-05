package com.lfish.control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by shenmegui on 2016/7/1.
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(),null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragment();
    }

    public View findViewById(int id){
        return getView().findViewById(id);
    }

    public abstract int getLayout();
    public abstract void initFragment();

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 300);
        } else {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 300);
        }
    }
}
