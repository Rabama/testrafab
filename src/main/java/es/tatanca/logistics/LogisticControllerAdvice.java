package es.tatanca.logistics;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class LogisticControllerAdvice {

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public String handle(RuntimeException ex) {
        return new String(ex.getMessage());
    }

}
