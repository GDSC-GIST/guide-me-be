package guideme.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    public String getLoginRequest() {


    }


    @GetMapping("/api/auth/callback")
    public void gatAccessToken(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {

    }
}
