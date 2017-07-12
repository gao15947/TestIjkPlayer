package com.ytj.ytjijkplayer.bean;

/**
 * Created by Administrator on 2017/7/11.
 */

public class VideoijkBean {
    //编号
    private int id;
    //视频流名称:标清、高清、超清
    private String name;
    //视频地址
    private String url;
    //备注
    private String mark;
    //是否被选中
    private boolean selected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
