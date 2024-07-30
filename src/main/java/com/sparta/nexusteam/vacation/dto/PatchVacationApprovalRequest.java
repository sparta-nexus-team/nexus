package com.sparta.nexusteam.vacation.dto;

import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatchVacationApprovalRequest {
    @NotNull(message = "승인상태는 필수값입니다.")
    private ApprovalStatus approvalStatus;
}
