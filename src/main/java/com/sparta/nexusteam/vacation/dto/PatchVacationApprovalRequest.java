package com.sparta.nexusteam.vacation.dto;

import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PatchVacationApprovalRequest {
    @NotBlank(message = "승인상태는 빈칸이 될 수 없습니다.")
    private ApprovalStatus approvalStatus;
}
