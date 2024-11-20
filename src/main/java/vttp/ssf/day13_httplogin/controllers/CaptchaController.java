package vttp.ssf.day13_httplogin.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.google.code.kaptcha.impl.DefaultKaptcha;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vttp.ssf.day13_httplogin.models.Database;
import vttp.ssf.day13_httplogin.models.User;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {
    
    // dependency injection
    @Autowired
    private DefaultKaptcha captchaGenerator;
    
    // to generate captcha
    @GetMapping("/generate")
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) {

        // set HTTP response header content type
        response.setContentType("image/jpeg");

        // generate captcha text
        String captchaText = captchaGenerator.createText();

        // store captcha text in session
        // .getSession() finds session associated with request, if none then create new one
        request.getSession().setAttribute("captcha", captchaText);

        // creates image with distorted text and takes in captchaText
        BufferedImage captchaImage = captchaGenerator.createImage(captchaText);
        
        // writes image as jpg file with output of bufferedimage
        try {
            ImageIO.write(captchaImage,"jpg", response.getOutputStream());
        } catch (IOException e) {
            System.err.printf("IO Exception found: %s\n", e.getMessage());
        }
    }

    // captcha page
    @PostMapping
    public String getCaptcha(@ModelAttribute("user") User user, BindingResult binding, @RequestParam("captcha") String captchaInput, @SessionAttribute("failedAttempts") Integer failedAttempts, HttpServletRequest request, Model model, HttpSession session) {
        
        System.out.printf("Received user [Username: %s, Password: %s]\n", user.getUsername(), user.getPassword());

        // store user in model for view
        model.addAttribute("user", user);

        // Create boolean variables to signify validity check
        boolean isUsernameValid = Database.isUsernameValid(user.getUsername());
        boolean isPasswordValid = Database.isPasswordValid(user.getPassword());

        // get captcha text from session
        String captcha = (String) request.getSession().getAttribute("captcha");

        // debugging
        System.out.printf("Captcha failed attempt count: %d\n", failedAttempts);

        // validate captcha
        if (captcha.equals(captchaInput)) {

            // debugging
            System.out.println("Captcha successful");

            // validate account details
            if (!isUsernameValid || !isPasswordValid) {
                System.out.printf("Validity check [Username validity: %b, Password validity: %b]\n", isUsernameValid, isPasswordValid);

                if (!isUsernameValid) {
                    ObjectError usernameErr = new ObjectError("globalError", "User not found.");
                    binding.addError(usernameErr);
                } else if (!isPasswordValid) {
                    ObjectError passwordErr = new ObjectError("globalError", "Password is invalid.");
                    binding.addError(passwordErr);
                }
                
                failedAttempts++;
            }

        } else {
            // debugging
            System.out.println("Captcha unsuccessful");

            ObjectError captchaError = new ObjectError("objectError", "Invalid captcha input. Try again.");
            binding.addError(captchaError);

            return "captcha"; // return to captcha page with errors
        }
        
        System.out.printf("Updated failed attempt count: %d\n", failedAttempts);
        session.setAttribute("failedAttempts", failedAttempts);

        if (failedAttempts > 2) {
            session.invalidate(); // destroy session
            return "locked"; // go to locked page after third unsuccessful attempt

        } else {

            session.invalidate();
            System.out.printf("After successful login failed attempt count: %d\n", failedAttempts);
            return "secret";
        }
    } 

}
