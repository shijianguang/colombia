package com.microsoft.xuetang.bean;

/**
 * Created by shijianguang on 9/19/16.
 */
public class EntityBean {
    private String id;
    private String name;

    public EntityBean() {
        this(null, null);
    }
    public EntityBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
