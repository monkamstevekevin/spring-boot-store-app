package com.codewithmosh.store.auths;
import com.codewithmosh.store.users.User;
import com.codewithmosh.store.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final  UserRepository userRepository;

    public User getCurrentUser() {
        // This method should return the currently authenticated user.
        // 
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId  = (Long) authentication.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }

}
