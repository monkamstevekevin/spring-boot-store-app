package com.codewithmosh.store.dto;

import com.codewithmosh.store.validations.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RegisterUserDtoRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 250, message = "Name must be between 3 and 250 characters")
     private String name;
        @NotBlank(message = "Email is required")
        @Email(message = "Email is not valid")
        @Lowercase(message = "Email must be in lowercase")
        private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
        private String password;
}
