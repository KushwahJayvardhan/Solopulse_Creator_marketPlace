package org.solopulse.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionHandler extends  RuntimeException {

     @ExceptionHandler(InvalidRoleException.class)
     public ResponseEntity<Map<String, Object>> handleInvalidRoleException(InvalidRoleException ex){
         Map<String, Object> response = new HashMap<>();
         response.put("timestamp", LocalDateTime.now());
         response.put("status", HttpStatus.BAD_REQUEST.value());
         response.put("error", "Invalid Role");
         response.put("message", ex.getMessage());
         return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
     }

     @ExceptionHandler(IdNotFoundException.class)
     public ResponseEntity<Map<String, Object>> handleIdNotFoundException(IdNotFoundException ex){
         Map<String, Object> response = new HashMap<>();
         response.put("status", HttpStatus.NOT_FOUND.value());
         response.put("error", "ID Not Found");
         response.put("message", ex.getMessage());
         return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
     }
}
