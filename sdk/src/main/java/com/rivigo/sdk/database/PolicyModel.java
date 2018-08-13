package com.rivigo.sdk.database;

import java.io.Serializable;

/**
 * Created by atulsakhala on 19/02/18.
 */

public class PolicyModel implements Serializable {
    long id;
    String title;
    String content;
    String noticeType;
    String source;
    String createdTimeStamp;
    String lastUpdatedTimeStamp;
    String language;

    public PolicyModel(long id, String title, String content, String noticeType, String source, String createdTimeStamp, String lastUpdatedTimeStamp, String language) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.noticeType = noticeType;
        this.source = source;
        this.createdTimeStamp = createdTimeStamp;
        this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
        this.language = language;
    }

    public PolicyModel() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String notifceType) {
        this.noticeType = notifceType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(String createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public String getLastUpdatedTimeStamp() {
        return lastUpdatedTimeStamp;
    }

    public void setLastUpdatedTimeStamp(String lastUpdatedTimeStamp) {
        this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
