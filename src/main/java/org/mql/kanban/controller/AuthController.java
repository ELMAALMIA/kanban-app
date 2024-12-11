package org.mql.kanban.controller;

import org.mql.kanban.model.User;
import org.mql.kanban.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // Méthode pour afficher le formulaire d'ajout d'utilisateur
    @GetMapping("/admin/addUser")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser"; // Le nom de la vue pour le formulaire d'ajout d'utilisateur
    }

    // Méthode pour traiter l'ajout d'utilisateur
    @PostMapping("/admin/addUser")
    public String addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin/users"; // Redirige vers la liste des utilisateurs après ajout
    }
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).orElse(null);

        if (user == null || !user.getRole().equals("ADMIN")) {
            return "error"; // Rediriger vers une page d'erreur ou afficher un message d'accès interdit
        }

        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "userList"; // Page qui affiche la liste des utilisateurs
    }


}
