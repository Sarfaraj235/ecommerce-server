package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.config.JwtService;
import com.app.exceptions.UserException;
import com.app.pojos.User;
import com.app.repository.UserRepository;


    @Service
    public class UserServiceImplementation implements UserService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private JwtService jwtService;

        @Override
        public User findUserById(Long userId) throws UserException {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new UserException("User not found with id: " + userId));
        }

        @Override
        public User findUserProfileByJwt(String jwt) throws UserException {

            String token = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
            String email = jwtService.extractUsername(token);

            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UserException("User not found with email: " + email);
            }

            return user;
        }
    }

