package org.example.service;

import org.example.dto.AppointmentsDto;
import org.example.model.Appointments;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {
    List<Appointments> findAll();
    Appointments save(AppointmentsDto appointmentsDto);
    Appointments save(Appointments appointments);
    Boolean checkOverlap(Long doctorId, LocalDateTime startDate, LocalDateTime endDate);
    List<Appointments> filterByDate(LocalDateTime startDate, LocalDateTime endDate);
    List<Appointments> findByPatientId(Long patientId);
    void delete(Long id);
    Appointments findById(Long id);
    List<Appointments> findByDoctorIdAndStatus( Long doctorId);
    List<Appointments> findByDoctorId(Long doctorId);
}
