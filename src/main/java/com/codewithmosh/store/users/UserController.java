package com.codewithmosh.store.users;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public Iterable<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort") String sort) {
       if( !Set.of("name","email").contains(sort)){
           sort = "name";
       }
        return userRepository.findAll(Sort.by(sort)).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();


        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }
    @PostMapping
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterUserDtoRequest request,
                                              UriComponentsBuilder uriBuilder)

     {
        var existingUser = userRepository.existsByEmail(request.getEmail());
        if (existingUser) {
            return ResponseEntity.badRequest().body(Map.of("email", "Email already exists"));
        }
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        var savedUser = userRepository.save(user);
         var userDto =    userMapper.toDto(savedUser);
    var uri  =      uriBuilder.path("/users/{id}")
                .buildAndExpand(userDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(userDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id,
                                              @RequestBody UpdateUserRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userMapper.update(request, user);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));

    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changeUserPassword(@PathVariable(name = "id", required = true) Long id,
                                                   @RequestBody ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if(!user.getPassword().equals(request.oldPassword)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }


}
