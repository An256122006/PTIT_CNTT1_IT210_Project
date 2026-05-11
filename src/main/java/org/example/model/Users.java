package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String  username;
    @Column(name = "password")
    private String  password;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role= Role.PATIENT;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;
    @OneToOne(mappedBy = "user")
    private UserProfiles userProfiles;
}