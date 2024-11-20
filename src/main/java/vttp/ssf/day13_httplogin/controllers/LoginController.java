package vttp.ssf.day13_httplogin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import vttp.ssf.day13_httplogin.models.Database;
import vttp.ssf.day13_httplogin.models.User;

@Controller
@RequestMapping
public class LoginController {
    
    // initial login page
    @GetMapping(path={"/", "/login"})
    public String getLogin(Model model) {
        // create new user and store in model to put on view
        model.addAttribute("user", new User());
        // go to login.html
        return "login";
    }

    // prevent unauthorised access to secret or captcha pages
    @GetMapping(path={"/secret", "/captcha"})
    public String unauthorisedAccess() {
        return "redirect:/login";
    }

    @PostMapping(path={"/auth"})
    public String postLogin(@ModelAttribute("user") User user, BindingResult binding, HttpSession session, Model model) {

        // Check if username and password at each login
        System.out.printf("Received user [Username: %s, Password: %s]\n", user.getUsername(), user.getPassword());

        // store user in model for view
        model.addAttribute("user", user);

        // Create boolean variables to signify validity check
        boolean isUsernameValid = Database.isUsernameValid(user.getUsername());
        boolean isPasswordValid = Database.isPasswordValid(user.getPassword());

        // Create Integer failedAttempts and store in session
        Integer failedAttempts = (Integer) session.getAttribute("failedAttempts");
        
        // set failedAttempts to 0 if new
        if (failedAttempts == null) {
            failedAttempts = 0;
        }

        // Check failedAttempts
        System.out.printf("Initial failed attempt count: %d\n", failedAttempts);

        // validate account details
        if (!isUsernameValid || !isPasswordValid) {
            // Check username & password validity
            System.out.printf("Validity check - [Username validity: %b, Password validity: %b]\n", isUsernameValid, isPasswordValid);

            if (!isUsernameValid) {
                // if username invalid, create new error showing user not found
                ObjectError usernameErr = new ObjectError("globalError", "User not found.");
                binding.addError(usernameErr);
            } else if (!isPasswordValid) {
                // if password invalid, create new error showing password invalid
                ObjectError passwordErr = new ObjectError("globalError", "Password is invalid.");
                binding.addError(passwordErr);
            }
            
            // add 1 to failed attempt for each unsuccessful login
            failedAttempts++;
            // Store failedAttempts in session
            session.setAttribute("failedAttempts", failedAttempts);

            if (failedAttempts == 2) {
                return "captcha"; // if failed attempts == 2, go to captcha page
            }
                return "login"; // if failed attempts < 2, go back to login page with errors
        }

        // Check failedAttempts after each iteration
        System.out.printf("Updated failed attempt count: %d\n", failedAttempts);
        
        // destroy session if login successful
        session.invalidate();
        // Check failedAttempts after session destroyed
        System.out.printf("After successful login failed attempt count: %d\n", failedAttempts);
        return "secret"; // go to secret page
    
    }
}
