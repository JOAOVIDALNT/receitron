package com.joaovidal.receitron.adapter.in.web;

import com.joaovidal.receitron.adapter.in.web.dto.UpdateUserResponse;
import com.joaovidal.receitron.adapter.in.web.dto.UserDtoMapper;
import com.joaovidal.receitron.domain.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User", description = "User preferences managment")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add/cultures")
    public ResponseEntity<UpdateUserResponse> addCultures(@RequestBody List<String> cultures, @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.addCultures(userDetails.getUsername(), cultures);
        return ResponseEntity.ok(UserDtoMapper.toUpdatedResponse(user));
    }

    @PostMapping("/add/preferences")
    public ResponseEntity<UpdateUserResponse> addPreferences(@RequestBody List<String> preferences, @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.addPreferences(userDetails.getUsername(), preferences);
        return ResponseEntity.ok(UserDtoMapper.toUpdatedResponse(user));
    }

    @PostMapping("/add/restrictions")
    public ResponseEntity<UpdateUserResponse> addRestrictions(@RequestBody List<String> restrictions, @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.addRestrictions(userDetails.getUsername(), restrictions);
        return ResponseEntity.ok(UserDtoMapper.toUpdatedResponse(user));
    }
}
