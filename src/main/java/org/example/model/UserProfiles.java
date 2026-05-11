package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @Column(name = "phone")
    private String Phone;
    @Column(name = "address")
    private String Address;
    @Column(name = "gender")
    private String Gender;
    @Column(name = "date_of_birth")
    private LocalDate Date_of_birth;
    @Column(name = "email")
    private String Email;
}
