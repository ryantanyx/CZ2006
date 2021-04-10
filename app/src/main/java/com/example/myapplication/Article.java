package com.example.myapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents the Article Entity for the news segment
 */
public class Article {

    /**
     * Source of the article
     */
    @SerializedName("source")
    @Expose
    private Source source;
    /**
     * Author of the article
     */
    @SerializedName("author")
    @Expose
    private String author;
    /**
     * Title of the article
     */
    @SerializedName("title")
    @Expose
    private String title;
    /**
     * Description of the article
     */
    @SerializedName("description")
    @Expose
    private String description;
    /**
     * URL of the article
     */
    @SerializedName("url")
    @Expose
    private String url;
    /**
     * URL to image of article
     */
    @SerializedName("urlToImage")
    @Expose
    private String urlToImage;
    /**
     * Published datetime of the article
     */
    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;

    /**
     * Get the source of this article
     * @return this article's source
     */
    public Source getSource() {
        return source;
    }

    /**
     * Set the source of this article
     * @param source
     */
    public void setSource(Source source) {
        this.source = source;
    }

    /**
     * Get the author of this article
     * @return this article's author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the author of this article
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get the title of this article
     * @return this article's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of this article
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the description of this article
     * @return this article's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of this article
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the URL of this article
     * @return this article's URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the URL of this article
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the URL of this article's image
     * @return this article's image's URL
     */
    public String getUrlToImage() {
        return urlToImage;
    }

    /**
     * Set the URL of this article's image
     * @param urlToImage
     */
    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    /**
     * Get the datetime when this article was published
     * @return this article's published datetime
     */
    public String getPublishedAt() {
        return publishedAt;
    }

    /**
     * Set this article's published datetime
     * @param publishedAt
     */
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
