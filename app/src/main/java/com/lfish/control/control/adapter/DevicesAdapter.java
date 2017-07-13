package com.lfish.control.control.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfish.control.R;
import com.lfish.control.action.BaseAction;
import com.lfish.control.db.dao.Device;

import java.util.List;

/**
 * Created by shenmegui on 2016/7/1.
 */
public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ActionHoder> {
    private List<Device> actions;
    private Context context;
    public DevicesAdapter(List<Device> actions, Context context) {
        this.actions = actions;
        this.context = context;
    }

    @Override
    public ActionHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_device, null);
        ActionHoder actionHoder = new ActionHoder(inflate);
        return actionHoder;
    }

    @Override
    public void onBindViewHolder(ActionHoder holder, final int position) {
        final Device device = actions.get(position);
        holder.firstName.setText(device.getNick().substring(0,1).toUpperCase());
        holder.name.setText(device.getNick());
        holder.des.setText(device.getDes());
        if(device.getDes() ==null || device.getDes().trim().length()<1){
            holder.des.setVisibility(View.GONE);
        }else{
            holder.des.setVisibility(View.VISIBLE);
        }
        holder.contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onClick(position,device);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    public class ActionHoder extends RecyclerView.ViewHolder{
        private TextView name,des,firstName;
        private  View contain;
        public ActionHoder(View itemView) {
            super(itemView);
            contain = itemView.findViewById(R.id.v_device_contain);
            firstName = (TextView) itemView.findViewById(R.id.iv_ico);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            des = (TextView) itemView.findViewById(R.id.tv_device_des);
        }
    }

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(int pos,Device device);
    }
}
