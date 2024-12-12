package com.VTool;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ProjectData {

    @Id
    @JsonProperty("id")
    @Column (columnDefinition = "text")
    private String id;

    @Column(columnDefinition = "text")
    private String code;

    @Column(columnDefinition = "text")
    private String type;

    @Column(columnDefinition = "text")
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String benefit;

    @Column(columnDefinition = "text")
    private String goal;

    @Column(columnDefinition = "text")
    private String privateField; 

    @Column(columnDefinition = "text")
    private String template;

    @Column(columnDefinition = "text")
    private String archived;

    @Column(columnDefinition = "text", name = "block_times_recording")
    private String blockTimesRecording;

    @Column(columnDefinition = "text")
    private String status;

    @Column(columnDefinition = "text")
    private String startDate;

    @Column(columnDefinition = "text")
    private String endDate;

    @Column
    private String budget;

    @Column(columnDefinition = "text")
    private String folder;

    @Column(columnDefinition = "text")
    private String department;

    @Column(columnDefinition = "text")
    private String location;

    @Column(columnDefinition = "text")
    private String projectManager;

    @Column(columnDefinition = "text", name = "project_manager_email")
    private String projectManagerEmail;

    @Column(columnDefinition = "text", name = "status_name")
    private String statusName;

    @Column(columnDefinition = "text", name = "status_date")
    private String statusDate;

    @Column(columnDefinition = "text", name = "Overall Status")
    private String overallStatus;

    @Column(columnDefinition = "text", name = "target_date")
    private String targetDate;

    @Column(columnDefinition = "text")
    private Integer progress;

    @Column(columnDefinition = "text", name = "appointments_status")
    private String appointmentsStatus;

    @Column(columnDefinition = "text", name = "costs_status")
    private String costsStatus;

    @Column(columnDefinition = "text", name = "goals_status")
    private String goalsStatus;

    @Column(columnDefinition = "text")
    private String explanation;

    @Column(columnDefinition = "text", name = "next_steps")
    private String nextSteps;

    @Column(columnDefinition = "text", name = "plan_cost")
    private String planCost;

    @Column(columnDefinition = "text", name = "actual_cost")
    private String actualCost;

    @Column(columnDefinition = "text", name = "plan_duration")
    private String planDuration;

    @Column(columnDefinition = "text", name = "actual_effort")
    private String actualEffort;

    @Column(columnDefinition = "text", name = "forecast_effort")
    private String forecastEffort;

    @Column(columnDefinition = "text", name = "recorded_time")
    private String recordedTime;

    @Column(columnDefinition = "text")
    private String workflow;

    @Column(columnDefinition = "text", name = "task_template")
    private String taskTemplate;

    @Column(columnDefinition = "text", name = "created_on")
    private String createdOn;

    @Column(columnDefinition = "text", name = "created_by")
    private String createdBy;

    @Column(columnDefinition = "text", name = "updated_on")
    private String updatedOn;

    @Column(columnDefinition = "text", name = "updated_by")
    private String updatedBy;

    @Column(columnDefinition = "text")
    private String labels;

    @Column(columnDefinition = "text")
    private String lockedFlavors;

    @Column(columnDefinition = "text", name="cost_status")
    private String costStatus;

    // Getter und Setter für alle Felder

    @JsonProperty("id")
    public String getId() {
        return id;
    }

   @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
       this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
       this.title = title;
    }

    public String getDescription() {
       return description;
    }

    public void setDescription(String description) {
       this.description = description;
    }

    public String getBenefit() {
       return benefit;
    }

    public void setBenefit(String benefit) {
       this.benefit = benefit;
    }

    public String getGoal() {
       return goal;
    }

    public void setGoal(String goal) {
       this.goal = goal;
    }

    public String getPrivateField() {
       return privateField;
    }

    public void setPrivateField(String privateField) {
       this.privateField = privateField;
    }

    public String getTemplate() {
       return template;
    }

    public void setTemplate(String template) {
       this.template = template;
    }

    public String getArchived() {
       return archived;
    }

    public void setArchived(String archived) {
       this.archived = archived;
    }

    public String getBlockTimesRecording() {
       return blockTimesRecording;
    }

    public void setBlockTimesRecording(String blockTimesRecording) {
       this.blockTimesRecording = blockTimesRecording;
    }

    public String getStatus() {
       return status;
    }

    public void setStatus(String status) {
       this.status = status;
    }

    public String getStartDate() {
       return startDate;
    }

    public void setStartDate(String startDate) {
       this.startDate = startDate;
    }

    public String getEndDate() {
       return endDate;
    }

    public void setEndDate(String endDate) {
       this.endDate = endDate;
    }

    public String getBudget() {
       return budget;
    }

    public void setBudget(String budget) {
       this.budget = budget;

    }

    public String getFolder() {
       return folder;
    }

    public void setFolder(String folder) {
       this.folder = folder;
    }

    public String getDepartment() {
       return department;
    }

    public void setDepartment(String department) {
       this.department = department;
    }

    public String getLocation() {
       return location;
    }

    public void setLocation(String location) {
       this.location = location;
    }

    public String getProjectManager() {
       return projectManager;
    }

    public void setProjectManager(String projectManager) {
       this.projectManager = projectManager;
    }

    public String getProjectManagerEmail() {
       return projectManagerEmail;
    }

    public void setProjectManagerEmail(String projectManagerEmail) {
       this.projectManagerEmail = projectManagerEmail;
    }

    public String getStatusName() {
       return statusName;
    }

    public void setStatusName(String statusName) {
       this.statusName = statusName;
    }

    public String getStatusDate() {
       return statusDate;
    }

    public void setStatusDate(String statusDate) {
       this.statusDate = statusDate;
    }

    public String getOverallStatus() {
       return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
       this.overallStatus = overallStatus;
    }

    public String getTargetDate() {
       return targetDate;
    }

    public void setTargetDate(String targetDate) {
       this.targetDate = targetDate;
    }

    public Integer getProgress() {
       return progress;
    }

    public void setProgress(Integer progress) {
       this.progress = progress;
    }

    public String getAppointmentsStatus() {
       return appointmentsStatus;
    }

    public void setAppointmentsStatus(String appointmentsStatus) {
       this.appointmentsStatus = appointmentsStatus;
    }

    public String getCostsStatus() {
       return costsStatus;
    }

    public void setCostsStatus(String costsStatus) {
       this.costsStatus = costsStatus;
    }

    public String getGoalsStatus() {
       return goalsStatus;
    }

    public void setGoalsStatus(String goalsStatus) {
       this.goalsStatus = goalsStatus;
    }

    public String getExplanation() {
       return explanation;
    }

    public void setExplanation(String explanation) {
       this.explanation = explanation;
    }

    public String getNextSteps() {
       return nextSteps;
    }

    public void setNextSteps(String nextSteps) {
       this.nextSteps = nextSteps;
    }

    public String getPlanCost() {
       return planCost;
    }

    public void setPlanCost(String planCost) {
       this.planCost = planCost;
    }

    public String getActualCost() {
       return actualCost;
    }

    public void setActualCost(String actualCost) {
       this.actualCost = actualCost;
    }

    public String getPlanDuration() {
       return planDuration;
    }

    public void setPlanDuration(String planDuration) {
       this.planDuration = planDuration;
    }

    public String getActualEffort() {
       return actualEffort;
    }

    public void setActualEffort(String actualEffort) {
       this.actualEffort = actualEffort;
    }

    public String getForecastEffort() {
       return forecastEffort;
    }

    public void setForecastEffort(String forecastEffort) {
       this.forecastEffort = forecastEffort;
    }

    public String getRecordedTime() {
       return recordedTime;
    }

    public void setRecordedTime(String recordedTime) {
       this.recordedTime = recordedTime;
    }

    public String getWorkflow() {
       return workflow;
    }

    public void setWorkflow(String workflow) {
       this.workflow = workflow;
    }

    public String getTaskTemplate() {
       return taskTemplate;
    }

    public void setTaskTemplate(String taskTemplate) {
       this.taskTemplate = taskTemplate;
    }

    public String getCreatedOn() {
       return createdOn;
    }

    public void setCreatedOn(String createdOn) {
       this.createdOn = createdOn;
    }

    public String getCreatedBy() {
       return createdBy;
    }

    public void setCreatedBy(String createdBy) {
       this.createdBy = createdBy;
    }

    public String getUpdatedOn() {
       return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
       this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
       return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
       this.updatedBy = updatedBy;
    }

    public String getLabels() {
       return labels;
    }

    public void setLabels(String labels) {
       this.labels = labels;
    }

    public String getLockedFlavors() {
       return lockedFlavors;
    }

    public void setLockedFlavors(String lockedFlavors) {
       this.lockedFlavors = lockedFlavors;
    }

    public String getCostStatus() {
        return costStatus;
    }

    public void setCostStatus(String costStatus) {
        this.costStatus = costStatus;
    }


    @Override
    public String toString() {
        return "ProjectData{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", benefit='" + benefit + '\'' +
                ", goal='" + goal + '\'' +
                ", privateField='" + privateField + '\'' +
                ", template='" + template + '\'' +
                ", archived='" + archived + '\'' +
                ", blockTimesRecording='" + blockTimesRecording + '\'' +
                ", status='" + status + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", budget='" + budget + '\'' +
                ", folder='" + folder + '\'' +
                ", department='" + department + '\'' +
                ", location='" + location + '\'' +
                ", projectManager='" + projectManager + '\'' +
                ", projectManagerEmail='" + projectManagerEmail + '\'' +
                ", statusName='" + statusName + '\'' +
                ", statusDate='" + statusDate + '\'' +
                ", overallStatus='" + overallStatus + '\'' +
                ", targetDate='" + targetDate + '\'' +
                ", progress=" + progress +
                ", appointmentsStatus='" + appointmentsStatus + '\'' +
                ", costsStatus='" + costsStatus + '\'' +
                ", goalsStatus='" + goalsStatus + '\'' +
                ", explanation='" + explanation + '\'' +
                ", nextSteps='" + nextSteps + '\'' +
                ", planCost='" + planCost + '\'' +
                ", actualCost='" + actualCost + '\'' +
                ", planDuration='" + planDuration + '\'' +
                ", actualEffort='" + actualEffort + '\'' +
                ", forecastEffort='" + forecastEffort + '\'' +
                ", recordedTime='" + recordedTime + '\'' +
                ", workflow='" + workflow + '\'' +
                ", taskTemplate='" + taskTemplate + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", labels='" + labels + '\'' +
                ", lockedFlavors='" + lockedFlavors + '\'' +
                ", costStatus='" + costStatus + '\'' +
                '}';
    }
}


