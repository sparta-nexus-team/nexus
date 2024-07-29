package com.sparta.nexusteam.work.entity;

public enum WorkStatus {
    VACATION("휴가"),
    CUSTOMIZED_WORK("맞춤근무"),
    OVER_WORK("맞춤근무");

    private final String workType;
    WorkStatus(String workType){
        this.workType = workType;
    }
}
