package net.gb.knox.petopia.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/exception")
public class ExceptionController {

    @GetMapping("/resource-not-found")
    public void resourceNotFound() throws ResourceNotFoundException {
        throw new ResourceNotFoundException("Resource not found");
    }

    @GetMapping("/invalid-status-action")
    public void invalidStatusAction() throws InvalidStatusActionException {
        throw new InvalidStatusActionException("Invalid status action");
    }

    @GetMapping("/unsupported-status-action")
    public void unsupportedStatusAction() throws UnsupportedStatusActionException {
        throw new UnsupportedStatusActionException("Unsupported status action");
    }

    @GetMapping
    public void exception() throws Exception {
        throw new Exception("Exception");
    }
}
