package max.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException;
import max.account.AccountController;
import max.account.AccountIn;
import max.account.AccountOut;
import max.account.LoginIn;
import max.auth.exceptions.AccountAlreadyExistsdException;
import max.auth.exceptions.BadEmailException;
import max.auth.exceptions.BadNameException;
import max.auth.exceptions.BadPasswordException;
import max.auth.exceptions.LoginFailedException;

@Service
public class AuthService {

    @Autowired
    private AccountController accountController;

    @Autowired
    private JwtService jwtService;

    public String register(Register in) {

        final String password = in.password().trim();
        if (null == in.name() || in.name().isEmpty()) { throw new BadNameException("Name is required");}
        if (null == in.email() || in.email().isEmpty()) { throw new BadEmailException("Email is required");}
        if (null == password || password.isEmpty()) { throw new BadPasswordException("Password is required");}
        if (password.length() < 8) { throw new BadPasswordException("Password must be at least 8 characters long");}
        
        try {
            return accountController.create(AccountIn.builder()
                .name(in.name())
                .email(in.email())
                .password(in.password())
                .build()
            ).getBody().id();
        } catch (FeignException e) {
            throw new AccountAlreadyExistsdException(in.email());
        }
            
    }

    public LoginOut authenticate(String email, String password) {

        try {
            ResponseEntity<AccountOut> response = accountController.login(LoginIn.builder()
                .email(email)
                .password(password)
                .build()
            );

            if (response.getStatusCode().isError()) {throw new LoginFailedException(email);}
            if (null == response.getBody()) {throw new LoginFailedException(email);}

            final AccountOut account = response.getBody();
            
            // Cria um token JWT
            final String token = jwtService.create(account.id(), account.name(), "regular");

            return LoginOut.builder()
                .token(token)
                .build();
                
        } catch (FeignException e) {
            throw new LoginFailedException(email);
        }
    }

    public Token solve(String token) {
        return jwtService.getToken(token);
    }

}
