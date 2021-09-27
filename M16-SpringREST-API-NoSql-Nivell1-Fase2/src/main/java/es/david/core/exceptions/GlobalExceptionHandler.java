package es.david.core.exceptions;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//No hay anotaciones del tipo @ExceptionHandler en esta clase ya que heredo de ResponseEntityExceptionHandler, 
//quien es la que sí que tiene implementada esas anotaciones.

//Al heredar de ella, puedo sobrescribir sus métodos, que en mi caso, son los siguientes: 

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String message = ex.getMessage();

		List<String> details = new ArrayList<>();
		details.add("Request method not supported");

		ApiErrors errors = new ApiErrors(message, details, status, LocalDateTime.now());

		return ResponseEntity.status(status).body(errors);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String message = ex.getMessage();

		List<String> details = new ArrayList<>();
		details.add("Media not supported");

		ApiErrors errors = new ApiErrors(message, details, status, LocalDateTime.now());

		return ResponseEntity.status(status).body(errors);
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String message = ex.getMessage();

		List<String> details = new ArrayList<>();
		details.add("Path variable is missing");

		ApiErrors errors = new ApiErrors(message, details, status, LocalDateTime.now());

		return ResponseEntity.status(status).body(errors);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		String message = ex.getMessage();
		
		List<String> details = new ArrayList<>();
		details.add("Mismatch of type");
		
		ApiErrors errors = new ApiErrors(message, details, status, LocalDateTime.now());
		
		
		return ResponseEntity.status(status).body(errors);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String message = ex.getMessage();
		
		List<String> details = new ArrayList<>();
		details.add("Request body is not readable");
		
		ApiErrors errors = new ApiErrors(message, details, status, LocalDateTime.now());
		
		
		return ResponseEntity.status(status).body(errors);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String message = ex.getMessage();
		
		List<String> details = new ArrayList<>();
		details.add("Request param is missing");
		
		ApiErrors errors = new ApiErrors(message, details, status, LocalDateTime.now());
		
		
		return ResponseEntity.status(status).body(errors);
	}

	//Para los manejos de errores personalizados, que se adecuen al programa, sí que tendrán que anotarse con @ExceptionHandler
	@ExceptionHandler(DiceThrowNotFoundException.class)
	public ResponseEntity<Object> handleDiceThrowNotFoundException(DiceThrowNotFoundException ex){
		
		String message = ex.getMessage();
		
		List<String> details = new ArrayList<>();
		details.add("Dice throw not found");
		
		ApiErrors errors = new ApiErrors(message, details, HttpStatus.BAD_REQUEST, LocalDateTime.now());
		
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	@ExceptionHandler(PlayerNotFoundException.class)
	public ResponseEntity<Object> handlePlayerNotFoundException(PlayerNotFoundException ex){
		
		String message = ex.getMessage();
		
		List<String> details = new ArrayList<>();
		details.add("Player not found");
		
		ApiErrors errors = new ApiErrors(message, details, HttpStatus.BAD_REQUEST, LocalDateTime.now());
		
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	@ExceptionHandler(APIException.class)
	public ResponseEntity<Object> handleOthersException(APIException ex){
		
		String message = ex.getMessage();
		
		List<String> details = new ArrayList<>();
		details.add("Other exceptions");
		details.add(ex.getMessage());
		
		ApiErrors errors = new ApiErrors(message, details, HttpStatus.NOT_FOUND, LocalDateTime.now());
		
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	//Metodo para recoger cualquier otro error que no hayamos podido manejar
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGlobalException(Exception ex){
		
		String message = ex.getMessage();
		
		List<String> details = new ArrayList<>();
		details.add("ERROR");
		details.add(ex.getMessage());
		
		ApiErrors errors = new ApiErrors(message, details, HttpStatus.BAD_REQUEST, LocalDateTime.now());
		
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

}
