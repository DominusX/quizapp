package com.themaster.quizzer;

import java.util.List;

public class CategoryModel {

    private String name, url, key;
    private List<String> sets;

    public CategoryModel(String name, List<String> sets, String url, String key) {
        //to avoid no-argument constructor exception
    }

    public CategoryModel(String name, String url, String key, List<String> sets) {
        this.name = name;
        this.url = url;
        this.key = key;
        this.sets = sets;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getSets() {
        return sets;
    }

    public void setSets(List<String> sets) {
        this.sets = sets;
    }
}