package com.smarttutor.dto;

public class DashboardStatsDTO {
    
    private Long totalClasses;
    private Long totalTeachers;
    private Long totalStudents;
    private Double overallAttendancePercentage;
    private Long todayAttendance;
    private Double averageQuizScore;
    private Long totalQuizzes;
    private Long totalNotes;
    private Long unreadNotifications;
    
    public DashboardStatsDTO() {}
    
    public Long getTotalClasses() {
        return totalClasses;
    }
    
    public void setTotalClasses(Long totalClasses) {
        this.totalClasses = totalClasses;
    }
    
    public Long getTotalTeachers() {
        return totalTeachers;
    }
    
    public void setTotalTeachers(Long totalTeachers) {
        this.totalTeachers = totalTeachers;
    }
    
    public Long getTotalStudents() {
        return totalStudents;
    }
    
    public void setTotalStudents(Long totalStudents) {
        this.totalStudents = totalStudents;
    }
    
    public Double getOverallAttendancePercentage() {
        return overallAttendancePercentage;
    }
    
    public void setOverallAttendancePercentage(Double overallAttendancePercentage) {
        this.overallAttendancePercentage = overallAttendancePercentage;
    }
    
    public Long getTodayAttendance() {
        return todayAttendance;
    }
    
    public void setTodayAttendance(Long todayAttendance) {
        this.todayAttendance = todayAttendance;
    }
    
    public Double getAverageQuizScore() {
        return averageQuizScore;
    }
    
    public void setAverageQuizScore(Double averageQuizScore) {
        this.averageQuizScore = averageQuizScore;
    }
    
    public Long getTotalQuizzes() {
        return totalQuizzes;
    }
    
    public void setTotalQuizzes(Long totalQuizzes) {
        this.totalQuizzes = totalQuizzes;
    }
    
    public Long getTotalNotes() {
        return totalNotes;
    }
    
    public void setTotalNotes(Long totalNotes) {
        this.totalNotes = totalNotes;
    }
    
    public Long getUnreadNotifications() {
        return unreadNotifications;
    }
    
    public void setUnreadNotifications(Long unreadNotifications) {
        this.unreadNotifications = unreadNotifications;
    }
}
