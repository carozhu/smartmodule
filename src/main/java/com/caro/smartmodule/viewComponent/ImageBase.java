package com.caro.smartmodule.viewComponent;

/**
 * Created by Administrator on 2017/6/12.
 */

public class ImageBase {
    private int type;
    private String url;
    private String endTime;
    private String link;

    public ImageBase(String url,String endTime,String link,int type){
        this.type = type;
        this.url = url;
        this.endTime = endTime;
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
