package com.maple.cse308.controller;

import com.maple.cse308.entity.User;
import com.maple.cse308.service.MovieServiceImpl;
import com.maple.cse308.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AccountController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MovieServiceImpl movieService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("comingSoonList", movieService.getMoviesComingSoon());
        model.addAttribute("outNowList", movieService.getMoviesOutNow());
        model.addAttribute("topBoxOfficeList", movieService.getTopBoxOffice());
        return "index";
    }

   /* @GetMapping("/loginFailed")
    public String loginFailed() {
        return "login_failed";
    }
*/
    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("comingSoonList", movieService.getMoviesComingSoon());
        model.addAttribute("outNowList", movieService.getMoviesOutNow());
        model.addAttribute("topBoxOfficeList", movieService.getTopBoxOffice());
        return "index";
    }

    @PostMapping("/registration")
    public String registerSubmit(@ModelAttribute User user, Model model) {
        try {
            userService.registerUser(user);
        } catch (Exception e) {
            model.addAttribute("comingSoonList", movieService.getMoviesComingSoon());
            model.addAttribute("outNowList", movieService.getMoviesOutNow());
            model.addAttribute("topBoxOfficeList", movieService.getTopBoxOffice());
            return "redirect:/index";

        }
        model.addAttribute("comingSoonList", movieService.getMoviesComingSoon());
        model.addAttribute("outNowList", movieService.getMoviesOutNow());
        model.addAttribute("topBoxOfficeList", movieService.getTopBoxOffice());
        return "redirect:/index";
    }

    @RequestMapping("/manage_account")
    public String manageAccount(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "manage_account";
    }

    @PostMapping("/changePhoto")
    public String changePhoto(@RequestParam String photo, Model model) {
            userService.changePhoto(photo);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("response", "Changes are saved!");
        return "manage_account :: photoSetting";
    }

    @PostMapping("/applyCritic")
    public String applyCritic(@RequestParam String website, @RequestParam String groups, @RequestParam String publications, Model model) {
        try {
            userService.criticApplication(website,groups,publications);
        } catch (Exception e) {
            return "manage_account";
        }
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("response", "Changes are saved!");
        return "manage_account :: applyCritic";
    }

    @PostMapping("/changeHomeTown")
    public String changeHomeTown(@RequestParam String hometown, Model model) {
        try {
            userService.changeHomeTown(hometown);
        } catch (Exception e) {
            model.addAttribute("user", userService.getCurrentUser());
            model.addAttribute("response", "Warning: You have entered illegal characters");
            return "manage_account :: hometownSetting";
        }
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("response", "Changes are saved!");
        return "manage_account :: hometownSetting";
    }

    @PostMapping("/changeBio")
    public String changeBio(@RequestParam String bio, Model model) {
        try {
            userService.changeBio(bio);
        } catch (Exception e) {
            model.addAttribute("user", userService.getCurrentUser());
            model.addAttribute("response", "Warning: You have exceeded the maximum length");
            return "manage_account :: bioSetting";
        }
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("response", "Changes are saved!");
        return "manage_account :: bioSetting";
    }


    @PostMapping("/changeEmail")
    public String changeEmail(@RequestParam String newEmail, @RequestParam String myPassword, Model model) {
        try {
            userService.changeEmail(newEmail, myPassword);
        } catch (Exception e) {
            model.addAttribute("user", userService.getCurrentUser());
            model.addAttribute("response", "Warning: You have entered an incorrect password");
            return "manage_account :: emailSetting";
        }
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("response", "Changes are saved!");
        return "manage_account :: emailSetting";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, Model model) {
        try {
            userService.changePassword(oldPassword, newPassword);
        } catch (Exception e) {
            model.addAttribute("user", userService.getCurrentUser());
            model.addAttribute("response", "Warning: You have entered an incorrect password");
            return "manage_account :: passwordSetting";
        }
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("response", "Changes are saved!");
        return "manage_account :: passwordSetting";
    }

    @PostMapping("/changeCountry")
    public String changeCountry(@RequestParam String country, Model model) {
        try {
            userService.changeCountry(country);
        } catch (Exception e) {
            model.addAttribute("user", userService.getCurrentUser());
            model.addAttribute("response", "Warning: You have entered illegal characters");
            return "manage_account :: changeCountry";
        }
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("response", "Changes are saved!");
        return "manage_account :: changeCountry";
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(Model model) {
        userService.deleteUser();
        SecurityContextHolder.clearContext();
        model.addAttribute("deleted", true);
        return "manage_account :: deleteAccount";
    }
}