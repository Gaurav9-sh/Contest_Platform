package com.codeplatform.controller;

import com.codeplatform.model.User;
import com.codeplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<User> joinContest(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        return ResponseEntity.ok(userService.createOrGetUser(username, email));
    }
}
