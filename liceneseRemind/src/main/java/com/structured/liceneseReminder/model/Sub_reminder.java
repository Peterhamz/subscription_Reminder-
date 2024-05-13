package com.structured.liceneseReminder.model;

import com.structured.liceneseReminder.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sub_reminder")
public class Sub_reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Name field is required...")
    private String name; // required by the user
    private String department; // should be automated
    private String licensePermitName; //required by the user
    private String description; // optional
    private LocalDate expiryDate; //drop down
    private String email; // drop down
    private Status status; //should be automated
}
