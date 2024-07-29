package com.sparta.nexusteam.work.entity;

public enum AllowedStatus {
    WORK_AllOWED("ALLOWED"),
    WORK_NOT_ALLOWED("NOT_ALLOWED")
    ;
    private final String permit;

    AllowedStatus(String permit) {
        this.permit = permit;
    }
}
