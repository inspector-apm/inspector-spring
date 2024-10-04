package dev.inspector.spring.interceptors.error;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.Transaction;
import dev.inspector.spring.interceptors.context.InspectorMonitoringContext;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private InspectorMonitoringContext inspectorMonitoringContext;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex, HttpServletRequest request) {
        // Log the error
//        String methodName = request.getMethod() + " " + request.getRequestURI();
//        Transaction transaction = inspector.startTransaction("Error Transaction for " + methodName);
//
//        // Creare un oggetto JSON per il messaggio di errore
//        JSONObject errorContext = new JSONObject();
//        errorContext.put("message", ex.getMessage());
//        errorContext.put("exception", ex.getClass().getSimpleName());
//
//        // Aggiungere il contesto dell'errore alla transazione
//        transaction.addContext("Error", errorContext);
        Inspector inspector = inspectorMonitoringContext.getInspectorService();
        inspector.reportException(ex);
        inspector.getTransaction().setResult("error");

//        inspector.flush();

        // Creare una risposta generica
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        // Log the error
        String methodName = request.getMethod() + " " + request.getRequestURI();
        Inspector inspector = inspectorMonitoringContext.getInspectorService();
        Transaction transaction = inspector.startTransaction("Error Transaction for " + methodName);

        // Creare un oggetto JSON per il messaggio di errore
        JSONObject errorContext = new JSONObject();
        errorContext.put("message", "Resource not found");
        errorContext.put("exception", ex.getClass().getSimpleName());

        // Aggiungere il contesto dell'errore alla transazione
        transaction.addContext("Error", errorContext);
        inspector.flush();
        inspectorMonitoringContext.removeInspectorService();

        // Creare una risposta not found
        return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
    }
}
