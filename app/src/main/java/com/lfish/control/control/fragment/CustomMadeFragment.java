package com.lfish.control.control.fragment;

import android.view.View;
import android.widget.Button;

import com.lfish.control.BaseFragment;
import com.lfish.control.R;

/**
 * Created by SuZhiwei on 2016/7/31.
 */
public class CustomMadeFragment extends BaseFragment {

    private Button contancUs;
    private onContactClikListener onContactClikListener;

    @Override
    public int getLayout() {
        return R.layout.fragment_custommade;
    }

    @Override
    public void initFragment() {
        contancUs = (Button) findViewById(R.id.btn_custommade_contans_us);
        contancUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onContactClikListener!=null){
                    onContactClikListener.onClick();
                }
            }
        });
    }

    public interface onContactClikListener{
        void onClick();
    }

    public CustomMadeFragment.onContactClikListener getOnContactClikListener() {
        return onContactClikListener;
    }

    public void setOnContactClikListener(CustomMadeFragment.onContactClikListener onContactClikListener) {
        this.onContactClikListener = onContactClikListener;
    }
}
