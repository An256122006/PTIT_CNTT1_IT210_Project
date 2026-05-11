package org.example.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.UsersDto;
import org.example.model.Role;
import org.example.model.UserProfiles;
import org.example.model.Users;
import org.example.service.impl.UserProfileService;
import org.example.service.impl.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hospital")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserProfileService userProfilesService;
    @GetMapping
    public String home(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if(user == null) {
            return "redirect:/hospital/login";
        }
        if(user.getRole() == Role.PATIENT) {
            return  "redirect:/hospital/patient";
        }
        if(user.getRole()==Role.DOCTOR){
            return  "redirect:/hospital/doctor";
        }
        return "redirect:/hospital/admin";
    }
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UsersDto());
        return "register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UsersDto user,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        Users existingUser = userService.findByUsername(user.getUsername());

        if (existingUser != null) {
            result.rejectValue("username", "username", "Tên đăng nhập đã tồn tại");
            return "register";
        }

        userService.save(user);
        return "redirect:/hospital/login";
    }
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password , Model model, HttpSession session) {
        if(username.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập tên đăng nhập và mật khẩu");
            return "login";
        }

        if(!userService.islogin(username, password)) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không chính xác");
            return "login";
        }
        session.setAttribute("user", userService.findByUsername(username));
        return "redirect:/hospital";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/hospital/login";
    }
    @GetMapping("/patient/profile")
    public String profile(HttpSession session,Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/hospital/login";
        }
        UserProfiles profiles = userProfilesService.findByUser(user);
        if (profiles != null) {
            user.setUserProfiles(profiles);
        } else {
            user.setUserProfiles(new UserProfiles());
        }
        model.addAttribute("user", user);
        return "patient/patient-profile";
    }
    @PostMapping("/patient/profile")
    public String saveProfile(@ModelAttribute("user") Users user,
                              HttpSession session) {

        Users sessionUser = (Users) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/hospital/login";
        }
        
        Users existingUser = userService.findById(sessionUser.getId());

        UserProfiles profile = userProfilesService.findByUser(existingUser);

        if (profile == null) {
            profile = new UserProfiles();
            profile.setUser(existingUser);
        }

        profile.setPhone(user.getUserProfiles().getPhone());
        profile.setEmail(user.getUserProfiles().getEmail());
        profile.setAddress(user.getUserProfiles().getAddress());
        profile.setGender(user.getUserProfiles().getGender());
        profile.setDate_of_birth(user.getUserProfiles().getDate_of_birth());

        userProfilesService.save(profile);

        return "redirect:/hospital/patient/profile";
    }
}
