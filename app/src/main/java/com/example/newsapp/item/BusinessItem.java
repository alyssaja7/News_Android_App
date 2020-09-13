package com.example.newsapp.item;

public class BusinessItem {
    private String imgUrl;
    private String title;
    private String section;
    private String time;
    private String webUrl;
    private String id;



    public BusinessItem(String imgUrl, String title, String section, String time, String webUrl, String id) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.section = section;
        this.time = time;
        this.webUrl = webUrl;
        this.id = id;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getTime() {
        return time;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getID() {
        return id;
    }


}
