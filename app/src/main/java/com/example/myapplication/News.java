package com.example.myapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents the News Entity for the news section in the application
 */
public class News {
    /**
     * The status of the news/whether the request was successful
     */
    @SerializedName("status")
    @Expose
    private String status;
    /**
     * The total number of results available for the request
     */
    @SerializedName("totalResult")
    @Expose
    private String totalResult;
    /**
     * The list of articles
     */
    @SerializedName("articles")
    @Expose
    private List<Article> article;

    /**
     * Get the status of this news request
     * @return The status of this news request
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status of this news request
     * @param status The status of this news request
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the total results of this news request
     * @return The total result of this news request
     */
    public String getTotalResult() {
        return totalResult;
    }

    /**
     * Set the total result of this news request
     * @param totalResult
     */
    public void setTotalResult(String totalResult) {
        this.totalResult = totalResult;
    }

    /**
     * Get the list of articles from this news
     * @return The list of articles
     */
    public List<Article> getArticle() {
        return article;
    }

    /**
     * Set the list of articles from this news
     * @param article The list of articles
     */
    public void setArticle(List<Article> article) {
        this.article = article;
    }
}
