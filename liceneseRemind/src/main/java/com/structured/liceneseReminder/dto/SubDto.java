package com.structured.liceneseReminder.dto;

import com.structured.liceneseReminder.enums.Status;
import com.structured.liceneseReminder.model.Department;
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
    private Long id;
    private String name;
    private String licensePermitName;
    private String description;
    private LocalDate expiryDate;
    private Status status;
    private Long departmentID;
}
