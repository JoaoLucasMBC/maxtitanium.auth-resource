package max.auth.exceptions;

public class BadPasswordException extends RuntimeException{
    
        public BadPasswordException(String message) {
            super(message);
        }
}
