package com.smarttutor.dto;

import com.smarttutor.enums.Role;

public class LoginResponseDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String token;
    private Long classId;
    private Long divisionId;
    private String className;
    private String divisionName;
    
    public LoginResponseDTO() {}
    
    public LoginResponseDTO(Long id, String name, String email, Role role, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.token = token;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Long getClassId() {
        return classId;
    }
    
    public void setClassId(Long classId) {
        this.classId = classId;
    }
    
    public Long getDivisionId() {
        return divisionId;
    }
    
    public void setDivisionId(Long divisionId) {
        this.divisionId = divisionId;
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
}
