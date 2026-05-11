package org.example.service.impl;
import lombok.RequiredArgsConstructor;
import org.example.dto.AppointmentsDto;
import org.example.model.Appointments;
import org.example.repository.IAppointmentsRepository;
import org.example.service.IAppointmentService;
import org.example.service.IDoctorService;
import org.example.service.IUserService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AppointmentService implements IAppointmentService {
    private final IAppointmentsRepository appointmentsRepository;
    private final IUserService userService;
    private final IDoctorService doctorService;
    @Override
    public List<Appointments> findAll() {
        return appointmentsRepository.findAll();
    }

    @Override
    public Appointments save(AppointmentsDto appointmentsDto) {
        Appointments appointments = new Appointments();
        appointments.setPatient(userService.findById(appointmentsDto.getPatientId()));
        appointments.setDoctor(doctorService.findById(appointmentsDto.getDoctorId()));
        appointments.setStartDate(appointmentsDto.getStartDate());
        appointments.setEndDate(appointmentsDto.getEndDate());
        appointments.setStatus("PENDING");
        return appointmentsRepository.save(appointments);
    }

    @Override
    public Appointments save(Appointments appointments) {
        return appointmentsRepository.save(appointments);
    }

    @Override
    public Boolean checkOverlap(Long doctorId, LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentsRepository.checkOverlap(doctorId, startDate, endDate);
    }

    @Override
    public List<Appointments> filterByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentsRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public List<Appointments> findByPatientId(Long patientId) {
        return appointmentsRepository.findByPatientId(patientId);
    }

    @Override
    public void delete(Long id) {
        appointmentsRepository.deleteById(id);
    }
    @Override
    public List<Appointments> findByDoctorIdAndStatus(Long doctorId) {
        return appointmentsRepository.findByDoctorIdAndStatus(doctorId);
    }

    @Override
    public List<Appointments> findByDoctorId(Long doctorId) {
        return appointmentsRepository.findByDoctorId(doctorId);
    }

    @Override
    public Appointments findById(Long id) {
        return appointmentsRepository.findById(id).orElse(null);
    }
}
