package me.whiteship.demo.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/create")
    public Account create() {
        Account account = new Account();
        account.setEmail("keesun@mail.com");
        account.setPassword("password");

        return accountService.save(account);
    }

}
