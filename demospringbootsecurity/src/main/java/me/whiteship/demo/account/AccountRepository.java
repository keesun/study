package me.whiteship.demo.account;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Repository
public class AccountRepository {

    private Map<String, Account> accounts = new HashMap<>();
    private Random random = new Random();

    public Account save(Account account) {
        account.setId(random.nextInt());
        accounts.put(account.getEmail(), account);
        return account;
    }

    public Account findByEmail(String username) {
        return accounts.get(username);
    }
}
