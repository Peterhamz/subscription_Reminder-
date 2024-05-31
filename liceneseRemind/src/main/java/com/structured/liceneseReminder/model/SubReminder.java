package com.structured.liceneseReminder.model;

import com.structured.liceneseReminder.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
public class SubReminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name; // required by the user
    private String licensePermitName; //required by the user
    private String description; // optional
    private LocalDate expiryDate; //drop down
    private Status status; //should be automated

    @ManyToOne
    @JoinColumn
    private Department department;
}
