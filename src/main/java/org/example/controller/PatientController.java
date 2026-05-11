package org.example.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.AppointmentsDto;
import org.example.model.*;
import org.example.repository.IPrescriptionDetailRepository;
import org.example.service.IAppointmentService;
import org.example.service.IDoctorService;
import org.example.service.ISpecialtiesService;
import org.example.service.IUserService;
import org.example.service.impl.MedicalRecordService;
import org.example.service.impl.PrescriptionDetailService;
import org.example.service.impl.PrescriptionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/hospital/patient")
@RequiredArgsConstructor
public class PatientController {
    private final IDoctorService doctorService;
    private final IAppointmentService appointmentService;
    private final IUserService userService;
    private final ISpecialtiesService specialtiesService;
    private final PrescriptionService prescriptionsService;
    private final MedicalRecordService medicalRecordsService;
    private final PrescriptionDetailService prescriptionDetailService;
    @GetMapping
    public String patient(HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        return "patient/patient-dashboard";
    }

    @GetMapping("/appointment")
    public String appointment(HttpSession session, Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }

        AppointmentsDto appointmentDto = new AppointmentsDto();
        appointmentDto.setPatientId(users.getId());

        model.addAttribute("specialties", specialtiesService.findAll());
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("user", users);
        model.addAttribute("appointmentDto", appointmentDto);
        return "patient/patient-appointment";
    }
    @PostMapping("/appointment")
    public String addappointment(@Valid @ModelAttribute("appointmentDto") AppointmentsDto appointmentDto , BindingResult bindingResult,HttpSession session,Model model){
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        if (bindingResult.hasErrors()) {
            System.out.println("Binding errors found:");
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("specialties", specialtiesService.findAll());
            model.addAttribute("user", users);
            return "patient/patient-appointment";
        }
        Boolean isOverlap = appointmentService.checkOverlap(appointmentDto.getDoctorId(), appointmentDto.getStartDate(), appointmentDto.getEndDate());
        if (isOverlap) {
            model.addAttribute("errorMessage", "Bác sĩ đã có lịch hẹn trong thời gian này. Vui lòng chọn thời gian khác.");
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("specialties", specialtiesService.findAll());
            model.addAttribute("user", users);
            return "patient/patient-appointment";
        }
        appointmentService.save(appointmentDto);
        return "redirect:/hospital/patient/history";
    }
    @GetMapping("/history")
    public String history(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(value = "startDate", required = false)
            LocalDateTime startDate,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(value = "endDate", required = false)
            LocalDateTime endDate,

            HttpSession session,
            Model model) {

        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        if (startDate == null) {
            startDate = LocalDateTime.now();
        }

        if (endDate == null) {
            endDate = LocalDateTime.now().plusMonths(1);
        }

        List<Appointments> result = appointmentService
                .filterByDate(startDate, endDate)
                .stream()
                .filter(a -> a.getPatient().getId().equals(users.getId()))
                .toList();

        model.addAttribute("user", users);
        model.addAttribute("appointments", result);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "patient/patient-history";
    }
    @GetMapping("/cancel/{id}")
    public String delete(@PathVariable("id") Long id) {
        appointmentService.delete(id);
        return "redirect:/hospital/patient/history";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/hospital/login";
    }
    @GetMapping("/doctors-by-specialty/{specialtyId}")
    @ResponseBody
    public List<Doctors> getDoctorsBySpecialty(@PathVariable Long specialtyId) {
        return doctorService.findBySpecialtiesId(specialtyId);
    }
    @GetMapping("/history/{id}")
    public String showMedicalRecord(@PathVariable("id") Long id, Model model, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }

        MedicalRecords medicalRecords = medicalRecordsService.findByAppointmentsId(id);
        Prescriptions prescriptions = prescriptionsService.findByMedicalRecordId(medicalRecords.getId());
        List<PrescriptionDetail> prescriptionDetails = prescriptionDetailService.findByPrescriptionId(prescriptions.getId());

        model.addAttribute("user", users);
        model.addAttribute("medicalRecord", medicalRecords);
        model.addAttribute("prescription", prescriptions);
        model.addAttribute("prescriptionDetails", prescriptionDetails);
        return "patient/patient-history-detail";
    }
}
