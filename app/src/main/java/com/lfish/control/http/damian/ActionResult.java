package com.lfish.control.http.damian;

import com.lfish.control.http.BaseJsonParser;
import com.lfish.control.http.BaseResultData;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by SuZhiwei on 2017/5/8.
 */
@HttpResponse(parser = BaseJsonParser.class)
public class ActionResult  extends BaseResultData{
    private List<ActionResultBean> data;

    public List<ActionResultBean> getData() {
        return data;
    }

    public void setData(List<ActionResultBean> data) {
        this.data = data;
    }

    public class ActionResultBean{
        private int id;
        private String menuName;
        private String menuShortName;
        private double normalPrice;
        private double promotionPrice;
        private String promotionInfo;
        private String imgUrl;
        private int useFlag;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public String getMenuShortName() {
            return menuShortName;
        }

        public void setMenuShortName(String menuShortName) {
            this.menuShortName = menuShortName;
        }

        public double getNormalPrice() {
            return normalPrice;
        }

        public void setNormalPrice(double normalPrice) {
            this.normalPrice = normalPrice;
        }

        public double getPromotionPrice() {
            return promotionPrice;
        }

        public void setPromotionPrice(double promotionPrice) {
            this.promotionPrice = promotionPrice;
        }

        public void setNormalPrice(long normalPrice) {
            this.normalPrice = normalPrice;
        }


        public void setPromotionPrice(long promotionPrice) {
            this.promotionPrice = promotionPrice;
        }

        public String getPromotionInfo() {
            return promotionInfo;
        }

        public void setPromotionInfo(String promotionInfo) {
            this.promotionInfo = promotionInfo;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getUseFlag() {
            return useFlag;
        }

        public void setUseFlag(int useFlag) {
            this.useFlag = useFlag;
        }
    }
}
