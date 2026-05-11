package org.example.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.MedicalRecordsDto;
import org.example.dto.PrescriptionDetailDto;
import org.example.dto.PrescriptionDto;
import org.example.model.*;
import org.example.service.IAppointmentService;
import org.example.service.impl.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/hospital/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final MedicalRecordService medicalRecordService;
    private final UserService userService;
    private final DoctorsService doctorService;
    private final IAppointmentService appointmentsService;
    private final MedicineService medicineService;
    private final PrescriptionService prescriptionService;
    private final PrescriptionDetailService prescriptionDetailService;
    @GetMapping
    public String doctor(HttpSession session,Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        Doctors doctors=doctorService.findByUserId(users.getId());
        model.addAttribute("user", doctors);
        model.addAttribute("appointments", appointmentsService.findByDoctorIdAndStatus(doctors.getId()));
        return "doctor/doctor-dashboard";
    }

    @GetMapping("/waiting")
    public String waiting(HttpSession session, Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        Doctors doctors = doctorService.findByUserId(users.getId());
        model.addAttribute("user", doctors);
        model.addAttribute("appointments", appointmentsService.findByDoctorIdAndStatus(doctors.getId()));
        return "doctor/doctor-waiting";
    }

    @GetMapping("/profiles")
    public String profiles(HttpSession session, Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        Doctors doctors = doctorService.findByUserId(users.getId());
        model.addAttribute("user", doctors);
        model.addAttribute("medical",medicalRecordService.findAll());
        return "doctor/doctor-profiles";
    }

    @GetMapping("/prescription")
    public String prescription(HttpSession session, Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        Doctors doctors = doctorService.findByUserId(users.getId());
        model.addAttribute("user", doctors);

        model.addAttribute("medical", medicalRecordService.findByDoctorIdAndDate(doctors.getId(), LocalDate.now()));
        model.addAttribute("prescription", new PrescriptionDto());
        model.addAttribute("medicines", medicineService.findAll(""));
        return "doctor/doctor-prescription";
    }


    @GetMapping("/diagnosis")
    public String diagnosis(HttpSession session, Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        Doctors doctors = doctorService.findByUserId(users.getId());
        model.addAttribute("user", doctors);
        model.addAttribute("appointments", appointmentsService.findByDoctorIdAndStatus(doctors.getId()));
        model.addAttribute("medicalRecordsDto", new MedicalRecordsDto());
        return "doctor/doctor-diagnosis";
    }
    @PostMapping("/diagnosis")
    public String saveDiagnosis(
            @ModelAttribute("medicalRecordsDto")
            MedicalRecordsDto medicalRecordsDto,
            HttpSession session,
            Model model
    ) {

        Users users = (Users) session.getAttribute("user");

        if (users == null) {
            return "redirect:/hospital/login";
        }

        try {

            Appointments appointment =
                    appointmentsService.findById(
                            medicalRecordsDto.getAppointmentId()
                    );
            List<MedicalRecords> existingRecords = medicalRecordService.findByPatientId(appointment.getPatient().getId());
            
            MedicalRecords medicalRecords = new MedicalRecords();
            medicalRecords.setAppointments(appointment);
            

            String diagnosisText = medicalRecordsDto.getDescription();

            if (!existingRecords.isEmpty() && diagnosisText != null && !diagnosisText.trim().isEmpty()) {
                diagnosisText = "Bệnh nhân tái khám\n" + diagnosisText;
            }
            medicalRecords.setDate(medicalRecordsDto.getDate());
            medicalRecords.setDescription(diagnosisText);

            medicalRecordService.save(medicalRecords);

            appointment.setStatus("CONFIRMED");

            appointmentsService.save(appointment);

            return "redirect:/hospital/doctor/profiles";

        } catch (Exception e) {

            model.addAttribute(
                    "errorMessage",
                    "Lỗi khi lưu kết quả chẩn đoán: "
                            + e.getMessage()
            );

            return diagnosis(session, model);
        }
    }

    @PostMapping("/prescription")
    public String savePrescription(@Valid @ModelAttribute("prescription") PrescriptionDto prescriptionDto, BindingResult bindingResult,HttpSession session,Model model){
        Users users=(Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        if (bindingResult.hasErrors()) {

            model.addAttribute("user", doctorService.findByUserId(users.getId()));

            model.addAttribute(
                    "medical",
                    medicalRecordService.findByDoctorIdAndDate(
                            doctorService.findByUserId(users.getId()).getId(),
                            LocalDate.now()
                    ).stream().filter(e->e.getAppointments().getStatus().equals("CONFIRMED")).toList()
            );

            model.addAttribute("medicines", medicineService.findAll(""));

            return "doctor/doctor-prescription";
        }
        
        if (prescriptionDto.getMedicines() == null || prescriptionDto.getMedicines().isEmpty()) {
            model.addAttribute("errorMessage", "Vui lòng thêm ít nhất một loại thuốc vào đơn");
            model.addAttribute("user", doctorService.findByUserId(users.getId()));
            model.addAttribute("medical", medicalRecordService.findByDoctorIdAndDate(
                    doctorService.findByUserId(users.getId()).getId(),
                    LocalDate.now()
            ));
            model.addAttribute("medicines", medicineService.findAll(""));
            return "doctor/doctor-prescription";
        }
        Prescriptions prescription = new Prescriptions();
        prescription.setDescription(prescriptionDto.getDescription());
        prescription.setMedicalRecord(medicalRecordService.findById(prescriptionDto.getMedicalRecordId()));
        prescription.setStatus("PENDING");
        prescription = prescriptionService.save(prescription);
        MedicalRecords medicalRecord = prescription.getMedicalRecord();
        Appointments appointment = medicalRecord.getAppointments();
        appointment.setStatus("COMPLETED");
        appointmentsService.save(appointment);
        for (PrescriptionDetailDto dt:prescriptionDto.getMedicines()){
            PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
            prescriptionDetail.setMedicines(medicineService.findMedicineById(dt.getMedicineId()));
            prescriptionDetail.setPrescriptions(prescription);
            prescriptionDetail.setQuantity(dt.getQuantity());
            prescriptionDetailService.save(prescriptionDetail);
        }
        return "redirect:/hospital/doctor/prescription";
    }
    @GetMapping("/profiles/{id}")
    public String editMedicalRecord(@PathVariable("id") Long id, HttpSession session, Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        Doctors doctors = doctorService.findByUserId(users.getId());
        model.addAttribute("user", doctors);

        MedicalRecords medicalRecord = medicalRecordService.findById(id);
        if (medicalRecord == null || !medicalRecord.getAppointments().getDoctor().getId().equals(doctors.getId())) {
            return "redirect:/hospital/doctor/profiles";
        }

        MedicalRecordsDto medicalRecordsDto = new MedicalRecordsDto();
        medicalRecordsDto.setPatientId(medicalRecord.getAppointments().getPatient().getId());
        medicalRecordsDto.setAppointmentId(medicalRecord.getAppointments().getId());
        medicalRecordsDto.setDescription(medicalRecord.getDescription());
        medicalRecordsDto.setDate(medicalRecord.getDate());

        model.addAttribute("medicalRecordDto", medicalRecordsDto);
        model.addAttribute("medicalRecord", medicalRecord);

        return "doctor/components/doctor-profiles-edit";
    }
    @PostMapping("/profiles/{id}")
    public String updateMedicalRecord(@PathVariable("id") Long id,
                                     @Valid @ModelAttribute("medicalRecordDto") MedicalRecordsDto medicalRecordsDto,
                                     BindingResult result, HttpSession session, Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        Doctors doctors = doctorService.findByUserId(users.getId());

        MedicalRecords medicalRecord = medicalRecordService.findById(id);
        if (medicalRecord == null || !medicalRecord.getAppointments().getDoctor().getId().equals(doctors.getId())) {
            return "redirect:/hospital/doctor/profiles";
        }

        if (result.hasErrors()) {
            model.addAttribute("user", doctors);
            model.addAttribute("medicalRecord", medicalRecord);
            return "doctor/components/doctor-profiles-edit";
        }

        try {
            medicalRecord.setDescription(medicalRecordsDto.getDescription());
            medicalRecord.setDate(medicalRecordsDto.getDate());
            medicalRecordService.save(medicalRecord);

            return "redirect:/hospital/doctor/profiles";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi cập nhật hồ sơ: " + e.getMessage());
            model.addAttribute("user", doctors);
            model.addAttribute("medicalRecord", medicalRecord);
            return "doctor/components/doctor-profiles-edit";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/hospital/login";
    }
}
