package net.gb.knox.petopia.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/test/security")
    public void security() {
    }

    @GetMapping("/admin/test/security")
    public void adminSecurity() {
    }
}
