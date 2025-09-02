package me.byunyoungjae.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.byunyoungjae.springbootdeveloper.domain.User;
import me.byunyoungjae.springbootdeveloper.dto.AddUserRequest;
import me.byunyoungjae.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    //private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    //userId 조회
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    //이메일을 입력받아 user조회, 없을시 예외발생
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
