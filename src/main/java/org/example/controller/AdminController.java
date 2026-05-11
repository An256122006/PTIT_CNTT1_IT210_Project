package org.example.controller;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.MedicineDto;
import org.example.dto.SpecialtiesDto;
import org.example.model.Medicines;
import org.example.model.Specialties;
import org.example.model.Users;
import org.example.service.IPrensionService;
import org.example.service.impl.MedicineService;
import org.example.service.impl.PrescriptionDetailService;
import org.example.service.impl.SpecicaltiesService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Map;
@Controller
@RequestMapping("/hospital/admin")
@RequiredArgsConstructor
public class AdminController {
    private final SpecicaltiesService specialtyService;
    private final MedicineService medicineService;
    private final IPrensionService prescriptionService;
    private final PrescriptionDetailService prescriptionDetailService;
    @GetMapping
    public String admin(HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        return "admin/admin-dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        return "admin/admin-dashboard";
    }
    @GetMapping("/specialties")
    public String specialties(HttpSession session, Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        model.addAttribute("specialties", specialtyService.findAll());
        model.addAttribute("specialty", new SpecialtiesDto());
        return "admin/admin-specialties";
    }

    @GetMapping("/prescriptions")
    public String prescriptions(
            @RequestParam(value = "status", defaultValue = "") String status,

            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate fromDate,

            HttpSession session,
            Model model
    ) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }

        if (status.isBlank()) {
            status = null;
        }

        if (fromDate == null && status == null) {

            model.addAttribute(
                    "prescriptions",
                    prescriptionService.findAll()
            );

        } else {
            model.addAttribute(
                    "prescriptions",
                    prescriptionService.findByDateRangeAndStatus(
                            fromDate,
                            LocalDate.now(),
                            status
                    )
            );
        }

        return "admin/admin-prescriptions";
    }
    
    @GetMapping("/medicines")
    public String medicines(@RequestParam(value = "search",defaultValue = "") String search, HttpSession session, Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        model.addAttribute("medicines", medicineService.findAll(search));
        return "admin/admin-medicines";
    }

    @PostMapping("/specialties")
    public String addOrUpdateSpecialty(@Valid @ModelAttribute("specialty") SpecialtiesDto specialtiesDto, BindingResult result, HttpSession session, Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        if (result.hasErrors()) {
            model.addAttribute("specialties", specialtyService.findAll());
            return "admin/admin-specialties";
        }
        try {
            if (specialtiesDto.getId() != null) {
                specialtyService.update(specialtiesDto.getId(), specialtiesDto);
            } else {
                specialtyService.save(specialtiesDto);
            }
        } catch (RuntimeException e) {
            result.rejectValue("name", "name", e.getMessage());
            model.addAttribute("specialties", specialtyService.findAll());
            return "admin/admin-specialties";
        }
        return "redirect:/hospital/admin/specialties";
    }

    @GetMapping("/specialties/{id}")
    public ResponseEntity<?> getSpecialtyById(@PathVariable Long id, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        }
        try {
            Specialties specialty = specialtyService.findById(id);
            if (specialty == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(specialty);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/specialties/{id}")
    public ResponseEntity<?> updateSpecialty(@PathVariable Long id, @Valid @RequestBody SpecialtiesDto specialtiesDto, BindingResult result, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        }
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Validation errors"));
        }
        try {
            specialtyService.update(id, specialtiesDto);
            return ResponseEntity.ok(Map.of("message", "Specialty updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/specialties/{id}")
    public ResponseEntity<?> deleteSpecialty(@PathVariable Long id, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        }
        try {
            specialtyService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Specialty deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    @GetMapping("/medicines/add")
    public String medicinesform(HttpSession session,Model model){
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
           return "redirect:/hospital/login";
        }
        model.addAttribute("medicine", new MedicineDto());
        model.addAttribute("isEdit", false);
        return "admin/admin-medicine-add";
    }
    @GetMapping("/medicines/edit/{id}")
    public String editMedicineForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
        Medicines medicine = medicineService.findMedicineById(id);
        if (medicine == null) {
            return "redirect:/hospital/admin/medicines";
        }
        MedicineDto medicineDto = new MedicineDto();
        medicineDto.setId(medicine.getId());
        medicineDto.setName(medicine.getName());
        medicineDto.setDescription(medicine.getDescription());
        medicineDto.setPrice(medicine.getPrice());
        medicineDto.setQuantity(medicine.getQuantity());

        model.addAttribute("medicine", medicineDto);
        model.addAttribute("isEdit", true);
        return "admin/admin-medicine-add";
    }
    @PostMapping("medicines/add")
    public String medicineAdd(@Valid @ModelAttribute("medicine") MedicineDto medicineDto, BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("medicine", medicineDto);
            return "admin/admin-medicine-add";
        }
        medicineService.addMedicine(medicineDto);
        return "redirect:/hospital/admin/medicines";
    }
    @PostMapping("medicines/edit/{id}")
    public String medicineEdit(@PathVariable("id") Long id, @Valid @ModelAttribute("medicine") MedicineDto medicineDto, BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("medicine", medicineDto);
            model.addAttribute("isEdit", true);
            return "admin/admin-medicine-add";
        }
        medicineService.updateMedicine(id, medicineDto);
        return "redirect:/hospital/admin/medicines";
    }
    @GetMapping("/prescriptions/detail/{id}")
    public String prescriptionDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("prescription", prescriptionService.findById(id));
        model.addAttribute("prescriptionDetail", prescriptionDetailService.findByPrescriptionId(id));
        return "admin/admin-prescription-detail";
    }
    @GetMapping("/prescriptions/approve/{id}")
    public String approvePrescription(@PathVariable("id") Long id,
                                      RedirectAttributes redirectAttributes,
                                      HttpSession session) {
        try {
            prescriptionService.approvePrescription(id);

            redirectAttributes.addFlashAttribute("successMessage", "Duyệt đơn thuốc thành công!");
            return "redirect:/hospital/admin/prescriptions";
        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/hospital/admin/prescriptions/detail/" + id;
        }
    }
    @GetMapping("/prescriptions/reject/{id}")
    public String rejectPrescription(@PathVariable("id") Long id){
        prescriptionService.rejectPrescription(id);
        return "redirect:/hospital/admin/prescriptions";
    }
    
    @GetMapping("/medicines/delete/{id}")
    public String deleteMedicine(@PathVariable("id") Long id, HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        if (users == null) {
            return "redirect:/hospital/login";
        }
            medicineService.deleteMedicine(id);
            return "redirect:/hospital/admin/medicines";
    }
}
