package com.sparta.nexusteam.work.entity;

public enum SalaryType {
    VACATION("휴가"),
    CUSTOMIZED_WORK("맞춤근무"),
    OVER_WORK("초과근무");

    private final String workType;
    SalaryType(String workType){
        this.workType = workType;
    }
}
