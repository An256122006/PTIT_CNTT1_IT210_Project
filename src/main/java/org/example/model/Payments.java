package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "status")
    private String status;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Users admin;
    @OneToOne
    @JoinColumn(name = "prescription_id")
    private Prescriptions prescription;
    @Column(name = "payment_date")
    private LocalDate paymentDate;
}
