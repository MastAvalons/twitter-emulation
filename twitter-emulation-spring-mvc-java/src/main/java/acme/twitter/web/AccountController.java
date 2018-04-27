package acme.twitter.web;

import acme.twitter.data.AccountRepository;
import acme.twitter.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Account controller.
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    private AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = {"/login"}, method = GET)
    public String home(Model model) {
        return "loginForm";
    }

    @RequestMapping(value = "/register", method = GET)
    public String showRegistrationForm() {
        return "registrationForm";
    }

    @RequestMapping(value = "/register", method = POST)
    public String processRegistration(
            @Valid RegistrationForm registrationForm,
            Errors errors) {
        if (errors.hasErrors()) {
            return "registrationForm";
        }

        Account account = new Account(registrationForm.getUsername(), registrationForm.getPassword(), registrationForm.getDescription());
        accountRepository.save(account);

        return "redirect:/account/" + account.getUsername();
    }

    @RequestMapping(value = "/{username}", method = GET)
    public String showMainForm(@PathVariable String username, Model model) {
        Account account = accountRepository.findByUsername(username);
        model.addAttribute(account);

        return "mainForm";
    }
}