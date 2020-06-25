package com.ajitapp.smartwork.models;

public class SubTaskModel {
    private String taskImage;
    private String taskName;
    private String taskId;

    private String subTaskImage;
    private String subTaskName;
    private String subTaskId;
    private String subTaskDesc;


    public SubTaskModel(String subTaskName,String subTaskId,String subTaskDesc, String taskId, String taskName) {
        this.taskImage = taskImage;
        this.taskName = taskName;
        this.taskId = taskId;
        this.subTaskName = subTaskName;
        this.subTaskId = subTaskId;
        this.subTaskDesc = subTaskDesc;
    }

    public String getTaskImage() {
        return taskImage;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getSubTaskImage() {
        return subTaskImage;
    }
    public String getSubTaskDesc() {
        return subTaskDesc;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public String getSubTaskId() {
        return subTaskId;
    }
}