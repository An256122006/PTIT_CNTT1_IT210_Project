package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prescription_details")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrescriptionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescriptions_id")
    private Prescriptions prescriptions;
    @JoinColumn(name = "medicines_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Medicines medicines;
    @Column(name = "quantity")
    private int quantity;
}
