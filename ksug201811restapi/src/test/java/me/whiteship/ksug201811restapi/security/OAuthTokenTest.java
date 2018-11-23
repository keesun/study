package me.whiteship.ksug201811restapi.security;

import me.whiteship.ksug201811restapi.accounts.Account;
import me.whiteship.ksug201811restapi.accounts.AccountRoles;
import me.whiteship.ksug201811restapi.accounts.AccountService;
import me.whiteship.ksug201811restapi.accounts.MyAppProperties;
import me.whiteship.ksug201811restapi.common.ControllerTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.net.http.HttpHeaders;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OAuthTokenTest extends ControllerTests {

    @Autowired
    AccountService accountService;

    @Autowired
    MyAppProperties myAppProperties;

    @Test
    public void getAccessToken() throws Exception {
        String username = "user@email.com";
        String password = "user";

        Account account = Account.builder()
                .username(username)
                .password(password)
                .roles(Set.of(AccountRoles.ADMIN))
                .build();

        this.accountService.createAccount(account);

        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(myAppProperties.getClientId(), myAppProperties.getClientSecret()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("grant_type", "password")
                .param("username", username)
                .param("password", password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").hasJsonPath())
        ;



    }
}
