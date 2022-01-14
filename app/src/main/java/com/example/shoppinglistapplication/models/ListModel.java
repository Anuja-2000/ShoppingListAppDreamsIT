package com.example.shoppinglistapplication.models;

public class ListModel {
    private int listID;
    private String userName, listHeading, listContent, createTime, createDate;

    public ListModel(int listID, String userName, String listHeading, String listContent, String createTime, String createDate) {
        this.listID = listID;
        this.userName = userName;
        this.listHeading = listHeading;
        this.listContent = listContent;
        this.createTime = createTime;
        this.createDate = createDate;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getListHeading() {
        return listHeading;
    }

    public void setListHeading(String listHeading) {
        this.listHeading = listHeading;
    }

    public String getListContent() {
        return listContent;
    }

    public void setListContent(String listContent) {
        this.listContent = listContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
