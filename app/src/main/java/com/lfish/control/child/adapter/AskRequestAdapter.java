package com.lfish.control.child.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.lfish.control.R;
import com.lfish.control.db.dao.AskInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by shenmegui on 2017/7/6.
 */
public class AskRequestAdapter   extends RecyclerView.Adapter<AskRequestAdapter.AskRequestHolder> {
    private List<AskInfo> askInfos;
    private Context context;
    public AskRequestAdapter(List<AskInfo> askInfos, Context context) {
        this.askInfos = askInfos;
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
        final AskInfo askInfo = askInfos.get(position);
        holder.name.setText(askInfo.getName());
        SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curDate = s.format(new Date(askInfo.getTime()));  //当前日期
        holder.time.setText(curDate);
        holder.des.setText(askInfo.getAskreson());
        holder.contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onClick(position,askInfo);
                }
            }
        });

        switch (askInfo.getStatue()){
            case AskInfo.STATUE_AGREE:
                holder.agree.setText("已同意");
                holder.agree.setClickable(false);
                holder.agree.setEnabled(false);
                break;
            case AskInfo.STATUE_UNREAD:
                holder.agree.setText("同意");
                holder.agree.setEnabled(true);
                holder.agree.setClickable(true);
                break;
            case AskInfo.STATUE_ASK:
                holder.agree.setText("同意");
                holder.agree.setEnabled(true);
                holder.agree.setClickable(true);
                break;
        }

        holder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onAgreeClick(position,askInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return askInfos.size();
    }

    public class AskRequestHolder extends RecyclerView.ViewHolder{
        private TextView name,des,time;
        private  View contain;
        private Button agree;
        public AskRequestHolder(View itemView) {
            super(itemView);
            contain = itemView.findViewById(R.id.v_askrequest_contain);
            name = (TextView) itemView.findViewById(R.id.tv_askrequest_name);
            des = (TextView) itemView.findViewById(R.id.tv_askrequest_reason);
            time = (TextView) itemView.findViewById(R.id.tv_askrequest_time);
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
        void onClick(int pos,AskInfo askInfo);
        void onAgreeClick(int pos,AskInfo askInfo);
    }


    public void setAskInfos(List<AskInfo> askInfos) {
        this.askInfos = askInfos;
    }
}
