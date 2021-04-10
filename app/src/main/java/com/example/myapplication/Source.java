package com.example.myapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents the Source Entity which is the source of the articles
 */
public class Source {
    /**
     * ID of the source
     */
    @SerializedName("id")
    @Expose
    private String id;
    /**
     * Name of the source
     */
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * Get the ID of this source
     * @return This source's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Set the ID of this source
     * @param id This source's new ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the name of this source
     * @return This source's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this source
     * @param name This source's new name
     */
    public void setName(String name) {
        this.name = name;
    }
}
