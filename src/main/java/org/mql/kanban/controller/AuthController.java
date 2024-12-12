package org.mql.kanban.controller;

import org.mql.kanban.model.User;
import org.mql.kanban.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signin")
    public String loginPage() {
        return "login";
    }

    // Display the user registration form
    @GetMapping("/register")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "addUser"; // Ensure addUser.html exists for registration
    }

    // Handle the registration of a new user
    @PostMapping("/register")

    public String addUser(User user, Model model) {
        System.out.println(user.toString());
        // Check if the username already exists

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode the password
        userRepository.save(user); // Save the user in the database
        return "redirect:/signin"; // Redirect to the sign-in page after registration
    }


    // Display the form to add a user (for admins)
    @GetMapping("/admin/addUser")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser"; // Reuse addUser.html for admin addition
    }

    // Handle the addition of a user by an admin
    @PostMapping("/admin/addUser")
    public String addAdminUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode the password
        userRepository.save(user); // Save the user in the database
        return "redirect:/admin/users"; // Redirect to the user list after addition
    }

    // Display the list of users
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "userList"; // Page that displays the list of users
    }
}
