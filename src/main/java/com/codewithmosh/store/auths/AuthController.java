package com.codewithmosh.store.auths;

import com.codewithmosh.store.users.UserDto;
import com.codewithmosh.store.users.UserMapper;
import com.codewithmosh.store.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserMapper userMapper;
    private AuthenticationManager authenticationManager;
    private com.codewithmosh.store.auths.JwtService jwtService;
    private UserRepository userRepository;
    private final JwtConfig  jwtConfig;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var userId = (Long) authentication.getPrincipal();
        System.out.println("this is the email : " + userId);
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,
                                   HttpServletResponse response
    ) {

//        var email = request.getEmail();
//        var password = request.getPassword();
//        var user = userRepository.findByEmail(email);
//        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password"));
//        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        var user  =  userRepository.findByEmail(request.getEmail()).orElseThrow();


      var accessToken  =  jwtService.generateAccessToken(user);
      var refreshToken  =  jwtService.generateRefreshToken(user);
      var cookie   = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7 days
        cookie.setSecure(true);
        response.addCookie(cookie);// Set to true if using HTTPS
        return ResponseEntity.ok().body(new JwtResponse(accessToken.toString()));
    }
    @PostMapping("/refresh")
public ResponseEntity<JwtResponse> refreshToken(
        @CookieValue(value = "refreshToken", required = false) String refreshToken,
        HttpServletResponse response){
        var jwt = jwtService.parse(refreshToken);
    if(jwt == null ||jwt.isExpired()){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
    var newAccessToken = jwtService.generateAccessToken(user);
    return  ResponseEntity.ok(new JwtResponse(newAccessToken.toString()));
}
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password"));
    }
}
