package com.VTool;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "create")
public class ProjectRequest {

    private ProjectBase projectBase;

    @XmlElement(name = "ProjectBase")
    public ProjectBase getProjectBase() {
        return projectBase;
    }

    public void setProjectBase(ProjectBase projectBase) {
        this.projectBase = projectBase;
    }

    public static class ProjectBase {
        private boolean isPrivate;
        private String startDate;
        private int statusId;
        private String title;
        private String projectType;
        private String folder;
        private String code;

        @XmlElement(name = "IsPrivate")
        public boolean isIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
        }

        @XmlElement(name = "StartDate")
        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        @XmlElement(name = "StatusId")
        public int getStatusId() {
            return statusId;
        }

        public void setStatusId(int statusId) {
            this.statusId = statusId;
        }

        @XmlElement(name = "Title")
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @XmlElement(name = "ProjectType")
        public String getProjectType() {
            return projectType;
        }

        public void setProjectType(String projectType) {
            this.projectType = projectType;
        }

        @XmlElement(name = "Folder")
        public String getFolder() {
            return folder;
        }

        public void setFolder(String folder) {
            this.folder = folder;
        }

        @XmlElement(name = "Code")
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
