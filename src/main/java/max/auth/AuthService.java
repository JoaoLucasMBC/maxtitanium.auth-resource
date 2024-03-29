package max.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import max.account.AccountController;
import max.account.AccountIn;
import max.account.AccountOut;
import max.account.LoginIn;

@Service
public class AuthService {

    @Autowired
    private AccountController accountController;

    @Autowired
    private JwtService jwtService;

    public String register(Register in) {

        final String password = in.password().trim();
        if (null == password || password.isEmpty()) { throw new IllegalArgumentException("Password is required");}
        if (password.length() < 8) { throw new IllegalArgumentException("Password must be at least 8 characters long");}

        return accountController.create(AccountIn.builder()
            .name(in.name())
            .email(in.email())
            .password(in.password())
            .build()
        ).getBody().id();
        
    }

    public LoginOut authenticate(String email, String password) {
        ResponseEntity<AccountOut> response = accountController.login(LoginIn.builder()
            .email(email)
            .password(password)
            .build()
        );

        if (response.getStatusCode().isError()) {throw new IllegalArgumentException("Invalid credentials");}
        if (null == response.getBody()) {throw new IllegalArgumentException("Invalid credentials");}

        final AccountOut account = response.getBody();
        
        // Cria um token JWT
        final String token = jwtService.create(account.id(), account.name(), "regular");

        return LoginOut.builder()
            .token(token)
            .build();
    }

    public Token solve(String token) {
        return jwtService.getToken(token);
    }

}
