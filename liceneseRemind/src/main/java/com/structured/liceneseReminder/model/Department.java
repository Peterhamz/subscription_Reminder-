package com.structured.liceneseReminder.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String department;

//    @OneToOne
//    @JoinColumn(name = "subReminder_id")
//    private SubReminder subReminder;

}