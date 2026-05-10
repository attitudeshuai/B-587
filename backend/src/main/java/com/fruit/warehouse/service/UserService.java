package com.fruit.warehouse.service;

import com.fruit.warehouse.entity.User;
import com.fruit.warehouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;

    public Optional<User> login(String username, String password) {
        logger.info("用户登录尝试: {}", username);
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        logger.info("创建/更新用户: {}", user.getUsername());
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        logger.info("删除用户: {}", id);
        userRepository.deleteById(id);
    }
}
