package com.sparta.nexusteam.salary.dto;

public class SalaryRequestDto {
    private double hourlyWage;
    private double dailyWorkingHours;
    private int weeklyWorkingDays;
    private double monthlyOvertimeHours;
    private boolean includeWeeklyAllowance;
    private Long employeeId;

    // Getters and Setters
    public double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public double getDailyWorkingHours() {
        return dailyWorkingHours;
    }

    public void setDailyWorkingHours(double dailyWorkingHours) {
        this.dailyWorkingHours = dailyWorkingHours;
    }

    public int getWeeklyWorkingDays() {
        return weeklyWorkingDays;
    }

    public void setWeeklyWorkingDays(int weeklyWorkingDays) {
        this.weeklyWorkingDays = weeklyWorkingDays;
    }

    public double getMonthlyOvertimeHours() {
        return monthlyOvertimeHours;
    }

    public void setMonthlyOvertimeHours(double monthlyOvertimeHours) {
        this.monthlyOvertimeHours = monthlyOvertimeHours;
    }

    public boolean isIncludeWeeklyAllowance() {
        return includeWeeklyAllowance;
    }

    public void setIncludeWeeklyAllowance(boolean includeWeeklyAllowance) {
        this.includeWeeklyAllowance = includeWeeklyAllowance;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}