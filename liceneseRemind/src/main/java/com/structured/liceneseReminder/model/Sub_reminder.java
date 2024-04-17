package com.structured.liceneseReminder.model;

import com.structured.liceneseReminder.enums.Status;
import jakarta.persistence.*;
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
    private String name;
    private String department;
    private String licensePermitName;
    private String description;
    private LocalDate expiryDate;
    private String email;
    private Status status;

//    private Duration duration;
//
//    public Duration getDuration() {
//        return Duration.ofMinutes(2);
//    }
//
//    public void setDuration(Duration duration) {
//        this.duration = duration;
//    }

}
