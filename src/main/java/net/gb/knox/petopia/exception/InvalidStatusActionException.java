package net.gb.knox.petopia.exception;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@StandardException
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidStatusActionException extends Exception {
}
