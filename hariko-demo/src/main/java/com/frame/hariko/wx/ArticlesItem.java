package com.frame.hariko.wx;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("item")
public class ArticlesItem implements Serializable {
    @XStreamAlias("Title")
    //@XStreamCDATA
    private String Title;

    @XStreamAlias("Description")
   // @XStreamCDATA
    private String Description;

    @XStreamAlias("PicUrl")
   // @XStreamCDATA
    private String PicUrl;

    @XStreamAlias("Url")
   // @XStreamCDATA
    private String Url;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
