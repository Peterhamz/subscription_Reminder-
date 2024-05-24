package com.structured.liceneseReminder.model;

import com.structured.liceneseReminder.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sub_reminder")
public class Sub_reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_id")
    private Long subId;
    @NotNull(message = "Name field is required...")
    private String name; // required by the user
    private String licensePermitName; //required by the user
    private String description; // optional
    private LocalDate expiryDate; //drop down
    private Status status; //should be automated
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_id")
    private User user;
}
