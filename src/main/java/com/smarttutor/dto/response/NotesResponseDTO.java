package com.smarttutor.dto.response;

import java.time.LocalDateTime;

public class NotesResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String subject;
    
    private String teacherName;
    private String className;
    private String divisionName;
    
    private String fileUrl;
    private LocalDateTime createdAt;
    
    // Keep existing fields for backward compatibility
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private ClassResponseDTO classEntity;
    private DivisionResponseDTO division;
    private TeacherResponseDTO teacher;
    private LocalDateTime uploadedAt;
    private Boolean active;

    // Constructors
    public NotesResponseDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ClassResponseDTO getClassEntity() {
        return classEntity;
    }

    public void setClassEntity(ClassResponseDTO classEntity) {
        this.classEntity = classEntity;
    }

    public DivisionResponseDTO getDivision() {
        return division;
    }

    public void setDivision(DivisionResponseDTO division) {
        this.division = division;
    }

    public TeacherResponseDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherResponseDTO teacher) {
        this.teacher = teacher;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
