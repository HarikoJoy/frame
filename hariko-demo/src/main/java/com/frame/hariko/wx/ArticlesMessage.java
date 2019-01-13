package com.frame.hariko.wx;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

@XStreamAlias("xml")
public class ArticlesMessage  extends BaseMessage {
    @XStreamAlias("ArticleCount")
    private int ArticleCount;

    @XStreamAlias("Articles")
    private List<ArticlesItem> Articles;

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<ArticlesItem> getArticles() {
        return Articles;
    }

    public void setArticles(List<ArticlesItem> articles) {
        Articles = articles;
    }
}