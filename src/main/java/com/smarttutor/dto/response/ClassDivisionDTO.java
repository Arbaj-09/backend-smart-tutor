package com.smarttutor.dto.response;

public class ClassDivisionDTO {
    private Long classId;
    private String className;
    private Long divisionId;
    private String divisionName;

    // Constructors
    public ClassDivisionDTO() {}

    public ClassDivisionDTO(Long classId, String className, Long divisionId, String divisionName) {
        this.classId = classId;
        this.className = className;
        this.divisionId = divisionId;
        this.divisionName = divisionName;
    }

    // Getters and Setters
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Long getDivisionId() { return divisionId; }
    public void setDivisionId(Long divisionId) { this.divisionId = divisionId; }

    public String getDivisionName() { return divisionName; }
    public void setDivisionName(String divisionName) { this.divisionName = divisionName; }
}
