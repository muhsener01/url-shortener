package demo.muhsener01.urlshortener.io.controller.web;


import demo.muhsener01.urlshortener.command.SignUpCommand;
import demo.muhsener01.urlshortener.command.response.SignUpResponse;
import demo.muhsener01.urlshortener.exception.UserAlreadyExistsException;
import demo.muhsener01.urlshortener.io.controller.rest.AuthController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthMvcController {


    private final AuthController authController;

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new SignUpCommand());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") SignUpCommand command, Model model) {
        try {
            ResponseEntity<SignUpResponse> response = authController.signUp(command);
            model.addAttribute("success", "Kayıt başarılı! Giriş yapabilirsiniz.");
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("error", "Hata: " + e.getMessage());
        }

        return "signup";
    }


}
