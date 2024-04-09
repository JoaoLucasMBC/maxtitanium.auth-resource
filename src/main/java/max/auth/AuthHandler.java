package max.auth;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import max.auth.exceptions.AccountAlreadyExistsdException;
import max.auth.exceptions.BadEmailException;
import max.auth.exceptions.BadJwtException;
import max.auth.exceptions.BadNameException;
import max.auth.exceptions.BadPasswordException;
import max.auth.exceptions.ExceptionOut;
import max.auth.exceptions.LoginFailedException;

@ControllerAdvice
public class AuthHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(BadPasswordException.class)
    public ResponseEntity<ExceptionOut> handleBadPasswordException(BadPasswordException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionOut(HttpStatus.BAD_REQUEST, e.getMessage(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(BadEmailException.class)
    public ResponseEntity<ExceptionOut> handleBadEmailException(BadEmailException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionOut(HttpStatus.BAD_REQUEST, e.getMessage(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(BadNameException.class)
    public ResponseEntity<ExceptionOut> handleBadNameException(BadNameException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionOut(HttpStatus.BAD_REQUEST, e.getMessage(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(BadJwtException.class)
    public ResponseEntity<ExceptionOut> handleBadJwtException(BadJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionOut(HttpStatus.BAD_REQUEST, e.getMessage(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ExceptionOut> handleLoginFailedException(LoginFailedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionOut(HttpStatus.UNAUTHORIZED, e.getMessage(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionOut> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionOut(HttpStatus.UNAUTHORIZED, "This token has expired", LocalDateTime.now().toString()));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ExceptionOut> handleFeignException(FeignException e) {
        // for the open feign, I want just want to repass the exact same error message
        return ResponseEntity.status(e.status()).body(new ExceptionOut(HttpStatus.valueOf(e.status()), e.getMessage(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(AccountAlreadyExistsdException.class)
    public ResponseEntity<ExceptionOut> handleAccountAlreadyExistsdException(AccountAlreadyExistsdException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionOut(HttpStatus.CONFLICT, e.getMessage(), LocalDateTime.now().toString()));
    }
}
