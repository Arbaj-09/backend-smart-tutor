package com.smarttutor.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class TeacherResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String subject;
    private Boolean active;
    private LocalDateTime createdAt;
    private List<ClassDivisionDTO> assignedClasses;
    private String className;  // First assigned class name
    private String divisionName;  // First assigned division name

    // Constructors
    public TeacherResponseDTO() {}

    public TeacherResponseDTO(Long id, String name, String email, String phone, String subject, Boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.subject = subject;
        this.active = active;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ClassDivisionDTO> getAssignedClasses() { return assignedClasses; }
    public void setAssignedClasses(List<ClassDivisionDTO> assignedClasses) { 
        this.assignedClasses = assignedClasses;
        // Auto-populate className and divisionName from first assignment
        if (assignedClasses != null && !assignedClasses.isEmpty()) {
            ClassDivisionDTO first = assignedClasses.get(0);
            this.className = first.getClassName();
            this.divisionName = first.getDivisionName();
        }
    }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public String getDivisionName() { return divisionName; }
    public void setDivisionName(String divisionName) { this.divisionName = divisionName; }
}
