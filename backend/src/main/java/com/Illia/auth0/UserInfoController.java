package com.Illia.auth0;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserInfoController {

    @GetMapping("/api/userinfo")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        Map<String, Object> response = new HashMap<>();

        String email = oidcUser.getEmail();
        List<String> roles = oidcUser.getClaim("https://example.com/roles/roles");
        boolean isAllowDispatcherContent = false;
        if(roles.contains("dispatcher"))
            isAllowDispatcherContent = true;
        response.put("email", email);
        response.put("isAllowDispatcherContent", isAllowDispatcherContent); 
        return response;
    }
}
