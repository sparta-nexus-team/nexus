package com.sparta.nexusteam.work.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class DateRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}
