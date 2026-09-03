package lk.sunrise.clinic.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String businessError(BusinessException ex, Model model) {
        model.addAttribute("errorTitle", "Request could not be completed");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("statusCode", 400);
        return "error";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidParameter(MethodArgumentTypeMismatchException ex, Model model) {
        model.addAttribute("errorTitle", "Invalid input");
        model.addAttribute("errorMessage", "One of the supplied values is invalid. Please return to the previous page and try again.");
        model.addAttribute("statusCode", 400);
        return "error";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String databaseRule(DataIntegrityViolationException ex, Model model) {
        log.warn("Database integrity rule rejected a request: {}", ex.getMostSpecificCause().getMessage());
        model.addAttribute("errorTitle", "Database rule prevented this change");
        model.addAttribute("errorMessage", "The information conflicts with an existing record or clinic scheduling rule. Please review the details and try again.");
        model.addAttribute("statusCode", 409);
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String unexpectedError(Exception ex, Model model) {
        log.error("Unexpected application error", ex);
        model.addAttribute("errorTitle", "Something went wrong");
        model.addAttribute("errorMessage", "The request could not be completed. Please return to the dashboard and try again.");
        model.addAttribute("statusCode", 500);
        return "error";
    }
}
