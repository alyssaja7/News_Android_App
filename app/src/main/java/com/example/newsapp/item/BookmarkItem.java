package com.example.newsapp.item;

public class BookmarkItem {

    private String imgUrl;
    private String title;
    private String section;


    private String date;
    private String webUrl;
    private String id;

    public BookmarkItem(String imgUrl, String title, String section, String date, String webUrl, String id) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.section = section;
        this.date = date;
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
        return date;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getID() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.id.equals(((BookmarkItem)obj).id))
            return true;
        return false;
    }

}
