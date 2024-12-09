package com.VTool;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import org.hibernate.sql.ast.tree.expression.Star;

import jakarta.persistence.Column;

@Entity
public class ProjectData {

    @Id
    private String id; 

    @Column
    private String title;

    @Column
    private String status; 

    @Column
    private Integer progress;

    @Column
    private String costStatus;

    @Column
    private String StartDate;

    @Column
    private String EndDate;


    // Getter und Setter für alle Felder
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getCostStatus() {
        return costStatus;
    }

    public void setCostStatus(String costStatus) {
        this.costStatus = costStatus;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

}
