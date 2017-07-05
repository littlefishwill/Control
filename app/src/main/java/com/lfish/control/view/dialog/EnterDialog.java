package com.lfish.control.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lfish.control.R;
import com.lfish.control.user.dao.User;


/**
 * Created by SuZhiwei on 2016/8/21.
 */
public class EnterDialog extends Dialog {
    private OnChooseListener onChooseListener;
    private LinearLayout control,controlChild;
    public EnterDialog(Context context) {
        super(context, android.R.style.Theme_Dialog);
    }

    public EnterDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chooseentertype);
        control = (LinearLayout) findViewById(R.id.ll_enter_control);
        controlChild = (LinearLayout) findViewById(R.id.ll_enter_controlchild);
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onChooseListener!=null){
                    onChooseListener.onChoose(User.EnterType.Control);
                }
            }
        });
        controlChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onChooseListener!=null){
                    onChooseListener.onChoose(User.EnterType.ControlChild);
                }
            }
        });
    }

    public interface OnChooseListener{
        void onChoose(User.EnterType enterType );
    }



    public void setOnChooseListener(OnChooseListener onChooseListener) {
        this.onChooseListener = onChooseListener;
    }
}
