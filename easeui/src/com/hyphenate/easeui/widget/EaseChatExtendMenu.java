package com.hyphenate.easeui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.DensityUtil;

/**
 * Extend menu when user want send image, voice clip, etc
 *
 */
public class EaseChatExtendMenu extends GridView{

    protected Context context;
    private List<ChatMenuItemModel> itemModels = new ArrayList<ChatMenuItemModel>();
    private boolean isTestNumber = false;

    public EaseChatExtendMenu(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public EaseChatExtendMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EaseChatExtendMenu(Context context) {
        super(context);
        init(context, null);
    }
    
    private void init(Context context, AttributeSet attrs){
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseChatExtendMenu);
        int numColumns = ta.getInt(R.styleable.EaseChatExtendMenu_numColumns, 4);
        ta.recycle();
        
        setNumColumns(numColumns);
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        setGravity(Gravity.CENTER_VERTICAL);
        setVerticalSpacing(DensityUtil.dip2px(context, 8));
    }
    
    /**
     * init
     */
    public void init(){
        setAdapter(new ItemAdapter(context, itemModels, false));
    }

    /**
     * init
     */
    public void init(boolean needShowSailes){
        setAdapter(new ItemAdapter(context, itemModels,needShowSailes));
    }

    /**
     * 设置是否为testnumber
     * @param isTestNumber
     */
    public void isTestNumber(boolean isTestNumber){
        EaseChatExtendMenu.this.isTestNumber = isTestNumber;
    }
    
    /**
     * register menu item
     * 
     * @param name
     *            item name
     * @param drawableRes
     *            background of item
     * @param itemId
     *             id
     * @param listener
     *            on click event of item
     */
    public void registerMenuItem(String name, String drawableRes, int itemId,boolean lock,double price, EaseChatExtendMenuItemClickListener listener) {
        ChatMenuItemModel item = new ChatMenuItemModel();
        item.name = name;
//        item.image = drawableRes;
        item.imageServer = drawableRes;
        item.id = itemId;
        item.clickListener = listener;
        item.lock = lock;
        item.price = price;
        itemModels.add(item);
    }
    
    /**
     * register menu item
     * 
     * @param nameRes
     *            resource id of itme name
     * @param drawableRes
     *            background of item
     * @param itemId
     *             id
     * @param listener
     *             on click event of item
     */
    public void registerMenuItem(int nameRes, String drawableRes, int itemId,boolean lock,double price, EaseChatExtendMenuItemClickListener listener) {
        registerMenuItem(context.getString(nameRes), drawableRes, itemId,lock,price, listener);
    }
    
    
    private class ItemAdapter extends ArrayAdapter<ChatMenuItemModel>{

        private Context context;
        private boolean needShowLocdes;

        public ItemAdapter(Context context, List<ChatMenuItemModel> objects,boolean needShowLocdes) {
            super(context, 1, objects);
            this.context = context;
            this.needShowLocdes = needShowLocdes;
        }
        
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ChatMenuItem menuItem = null;
            if(convertView == null){
                convertView = new ChatMenuItem(context);
            }
            menuItem = (ChatMenuItem) convertView;
            menuItem.setImage(getItem(position).image);
            Glide.with(context).load(getItem(position).imageServer).placeholder(R.drawable.loddingaction_ico).into(menuItem.imageView);
            menuItem.setText(getItem(position).name);

            if(isTestNumber){
                menuItem.setTextPrice("体验");
                menuItem.lock(false);
            }else if(needShowLocdes){
                if(getItem(position).lock){
                    if(getItem(position).price<=0d){
                        menuItem.setTextPrice("免费试用");
                        menuItem.lock(false);
                    }else{
                        menuItem.setTextPrice(getItem(position).price + "￥");
                        menuItem.lock(getItem(position).lock);
                    }

                }else{
                    menuItem.setTextPrice("已解锁");
                    menuItem.lock(false);
                }
            }else{
                if(getItem(position).lock){
                    if(getItem(position).price<=0d){
                        menuItem.setTextPrice("免费试用");
                        menuItem.lock(false);
                    }else{
                        menuItem.setTextPrice(getItem(position).price + "￥");
                        menuItem.lock(getItem(position).lock);
                    }
                }else{
                    menuItem.setTextPrice("");
                    menuItem.lock(false);
                }
            }

            menuItem.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    if(getItem(position).clickListener != null){
                        getItem(position).clickListener.onClick(getItem(position).id, v);
                    }
                }
            });
            return convertView;
        }
        
        
    }
    
    
    public interface EaseChatExtendMenuItemClickListener{
        void onClick(int itemId, View view);
    }
    
    
    class ChatMenuItemModel{
        String name;
        int image;
        int id;
        double price;
        boolean lock;
        String imageServer;
        EaseChatExtendMenuItemClickListener clickListener;
    }
    
    class ChatMenuItem extends LinearLayout {
        public ImageView imageView;
        private TextView textView;
        private TextView textPrice;
        public ImageView imageLock;

        public ChatMenuItem(Context context, AttributeSet attrs, int defStyle) {
            this(context, attrs);
        }

        public ChatMenuItem(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context, attrs);
        }

        public ChatMenuItem(Context context) {
            super(context);
            init(context, null);
        }

        private void init(Context context, AttributeSet attrs) {
            LayoutInflater.from(context).inflate(R.layout.ease_chat_menu_item, this);
            imageView = (ImageView) findViewById(R.id.image);
            textView = (TextView) findViewById(R.id.text);
            imageLock = (ImageView) findViewById(R.id.iv_action_lock);
            textPrice = (TextView) findViewById(R.id.tv_action_price);
        }

        public void setImage(int resid) {
            imageView.setBackgroundResource(resid);
        }

        public void setText(int resid) {
            textView.setText(resid);
        }

        public void setText(String text) {
            textView.setText(text);
        }

        public void lock(boolean isLock){
            if(isLock){
                imageLock.setVisibility(View.VISIBLE);
//                textPrice.setVisibility(View.VISIBLE);
            }else{
                imageLock.setVisibility(View.GONE);
//                textPrice.setVisibility(View.GONE);
            }
        }

        public void setTextPrice(String price){
            textPrice.setText(price);
        }

    }
}
