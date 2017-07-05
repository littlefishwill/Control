package com.lfish.control.control.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lfish.control.R;
import com.lfish.control.action.BaseAction;

import java.util.List;

/**
 * Created by shenmegui on 2016/7/1.
 */
public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionHoder> {
    private List<BaseAction> actions;
    private Context context;
    public ActionAdapter(List<BaseAction> actions,Context context) {
        this.actions = actions;
        this.context = context;
    }

    @Override
    public ActionHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_action, null);
        ActionHoder actionHoder = new ActionHoder(inflate);
        return actionHoder;
    }

    @Override
    public void onBindViewHolder(ActionHoder holder, int position) {
        BaseAction baseAction = actions.get(position);
        holder.ico.setImageResource(baseAction.getActionIco());
        holder.name.setText(baseAction.getActionName());
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    public class ActionHoder extends RecyclerView.ViewHolder{
        private ImageView ico;
        private TextView name;
        public ActionHoder(View itemView) {
            super(itemView);
            ico = (ImageView) itemView.findViewById(R.id.iv_ico);
            name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
