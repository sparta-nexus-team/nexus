package com.sparta.nexusteam.employee.entity;

public enum Position {
    CHAIRMAN("회장"),
    PRESIDENT("사장"),
    VICE_PRESIDENT("부사장"),
    SENIOR_EXECUTIVE_DIRECTOR("전무"),
    EXECUTIVE_DIRECTOR("상무"),
    DIRECTOR("이사"),
    GENERAL_MANAGER("부장"),
    DEPUTY_GENERAL_MANAGER("차장"),
    MANAGER("과장"),
    ASSISTANT_MANAGER("대리"),
    WORKER("사원"),
    INTERN("인턴");

    private final String title;

    Position(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
