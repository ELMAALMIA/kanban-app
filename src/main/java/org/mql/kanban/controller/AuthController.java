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

    // Afficher le formulaire d'ajout d'utilisateur (pour l'inscription)
    @GetMapping("/register")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "addUser"; // Ensure addUser.html exists
    }


    // Traiter l'inscription d'un nouvel utilisateur
    @PostMapping("/register")
    public String addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encoder le mot de passe
        userRepository.save(user); // Sauvegarder l'utilisateur dans la base de données
        return "redirect:/signin"; // Rediriger vers la page de connexion après l'inscription
    }

    // Afficher le formulaire d'ajout d'utilisateur pour les administrateurs
    @GetMapping("/admin/addUser")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser"; // Le nom de la vue pour le formulaire d'ajout d'utilisateur
    }

    // Méthode pour traiter l'ajout d'utilisateur par un administrateur
    @PostMapping("/admin/addUser")
    public String addAdminUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin/users"; // Redirige vers la liste des utilisateurs après ajout
    }

    // Afficher la liste des utilisateurs
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "userList"; // Page qui affiche la liste des utilisateurs
    }
}
