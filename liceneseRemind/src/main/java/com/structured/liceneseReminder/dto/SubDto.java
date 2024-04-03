package com.structured.liceneseReminder.dto;

import com.structured.liceneseReminder.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubDto {
    private String name;
    private String department;
    private String licensePermitName;
    private String description;
    private LocalDate expiryDate;
    private String email;
    private Status status;
}
