package com.joaovidal.receitron.adapter.in.web;

import com.joaovidal.receitron.domain.service.UserService;
import io.jsonwebtoken.Jwt;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add/cultures")
    public ResponseEntity<?> addCultures(@RequestBody List<String> cultures, @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.addCultures(userDetails.getUsername(), cultures);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/add/preferences")
    public ResponseEntity<?> addPreferences(@RequestBody List<String> preferences, @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.addPreferences(userDetails.getUsername(), preferences);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/add/restrictions")
    public ResponseEntity<?> addRestrictions(@RequestBody List<String> restrictions, @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.addRestrictions(userDetails.getUsername(), restrictions);
        return ResponseEntity.ok(user);
    }
}
