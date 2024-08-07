package net.gb.knox.petopia.exception;

import net.gb.knox.petopia.domain.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponseDto createErrorResponse(Exception e) {
        var simpleName = e.getClass().getSimpleName();
        var code = simpleName.replace("Exception", "");

        if (code.isEmpty()) {
            code = "Unknown";
        }

        return new ErrorResponseDto(code, e.getMessage());
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleExceptionAsNotFound(Exception e) {
        return new ResponseEntity<>(createErrorResponse(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidStatusActionException.class, UnsupportedStatusActionException.class})
    public ResponseEntity<ErrorResponseDto> handleExceptionAsBadRequest(Exception e) {
        return new ResponseEntity<>(createErrorResponse(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponseDto> handleExceptionAsInternalServerError() {
        var errorResponse = new ErrorResponseDto("General", "Something went wrong");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
