package com.example.demo.service;

import com.example.demo.entity.Users;

import com.example.demo.mapper.UsersMapper;

import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsersMapper usersMapper;

    public String login(Users users) {
        Users existingUser = usersMapper.findByUsername(users.getUsername());
        if (existingUser == null || !existingUser.getPassword().equals(users.getPassword())) {
            return "Invalid username or password";
        }
        return JwtUtil.generateToken(existingUser.getUsername());
    }

    public String register(Users user) {
        Users existingUser = usersMapper.findByUsername(user.getUsername());
        if (existingUser != null) {
            return "Username already exists";
        }
        usersMapper.saveUser(user);
        return "Registration successful";
    }
}
