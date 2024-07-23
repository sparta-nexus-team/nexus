package com.sparta.nexusteam.vacation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostVacationTypeRequest {
    @NotBlank(message="이름은 빈칸이 될 수 없습니다.")
    @Size(min=1, max=15, message="이름은 최소 1글자에서 최대 15글자까지 가능합니다.")
    private String name;
    @NotNull(message="일수는 필수값입니다.")
    private int days;
}
