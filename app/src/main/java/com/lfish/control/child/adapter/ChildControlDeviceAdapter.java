package com.lfish.control.child.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lfish.control.R;
import com.lfish.control.db.dao.AskInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shenmegui on 2017/7/6.
 */
public class ChildControlDeviceAdapter extends RecyclerView.Adapter<ChildControlDeviceAdapter.AskRequestHolder> {
    private List<String> devices;
    private Context context;
    private int[] bgcolors = new int[]{Color.parseColor("#26b99a"),Color.parseColor("#004178"),Color.parseColor("#025196")};
    public ChildControlDeviceAdapter(List<String> devices, Context context) {
        if(devices==null){
            this.devices = new ArrayList<>();
        }else {
            this.devices = devices;
        }
        this.context = context;
    }

    @Override
    public AskRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_askrequest, null);
        AskRequestHolder askRequestHolder = new AskRequestHolder(inflate);
        return askRequestHolder;
    }

    @Override
    public void onBindViewHolder(AskRequestHolder holder, final int position) {
        holder.firstName.setText(devices.get(position).substring(0,1).toUpperCase());
        holder.firstName.setBackgroundColor(bgcolors[position%bgcolors.length]);
        holder.name.setText(devices.get(position));
        holder.contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onClick(position,devices.get(position));
                }
            }
        });

        holder.agree.setText("删除");

        holder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onAgreeClick(position,devices.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class AskRequestHolder extends RecyclerView.ViewHolder{
        private TextView name,des,time,firstName;
        private  View contain;
        private Button agree;
        public AskRequestHolder(View itemView) {
            super(itemView);
            contain = itemView.findViewById(R.id.v_askrequest_contain);
            name = (TextView) itemView.findViewById(R.id.tv_askrequest_name);
            des = (TextView) itemView.findViewById(R.id.tv_askrequest_reason);
            time = (TextView) itemView.findViewById(R.id.tv_askrequest_time);
            firstName = (TextView) itemView.findViewById(R.id.tv_askrequest_firstname);
            agree = (Button) itemView.findViewById(R.id.btn_askrequest_agree);
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
        void onClick(int pos, String name);
        void onAgreeClick(int pos, String name);
    }

    public List<String> getDevices() {
        return devices;
    }

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }

    public void setAskInfos(List<String> devices) {
        this.devices = devices;
    }
}
