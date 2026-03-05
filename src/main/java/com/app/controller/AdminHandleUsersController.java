package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.dto.ApiResponse;
import com.app.dto.DashboardOverviewDto;
import com.app.pojos.User;
import com.app.repository.UserRepository;
import com.app.service.AdminDashboardService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminHandleUsersController {

	@Autowired
    private final UserRepository userRepository;
	
	@Autowired
	 private final AdminDashboardService adminDashboardService;

    // GET /api/admin/users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // GET /api/admin/users/{userId}
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // PUT /api/admin/users/{userId}/role?role=ADMIN
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<User> updateUserRole(
            @PathVariable Long userId,
            @RequestParam("role") String role
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setRole(Enum.valueOf(com.app.pojos.Role.class, role.toUpperCase()));
        User updated = userRepository.save(user);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // DELETE /api/admin/users/{userId}
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        userRepository.delete(user);

        ApiResponse res = new ApiResponse();
        res.setStatus(true);
        res.setMessage("User deleted successfully");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    
        @GetMapping("/dashboard/overview")
        public ResponseEntity<DashboardOverviewDto> overview() {
            return ResponseEntity.ok(adminDashboardService.getOverview());
        }
    }


