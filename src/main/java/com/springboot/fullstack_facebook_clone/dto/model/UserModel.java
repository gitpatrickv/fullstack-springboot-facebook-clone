package com.springboot.fullstack_facebook_clone.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.fullstack_facebook_clone.entity.constants.Gender;
import com.springboot.fullstack_facebook_clone.entity.constants.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import validation.ConfirmPasswordValid;
import validation.PasswordLengthValid;
import validation.UniqueEmailValid;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfirmPasswordValid
@JsonInclude(NON_DEFAULT)
public class UserModel {
    private Long userId;
    @NotBlank(message = "{first.name.required}")
    private String firstName;

    @NotBlank(message = "{last.name.required}")
    private String lastName;

    @UniqueEmailValid
    @NotBlank(message = "{email.required}")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @PasswordLengthValid
    @NotBlank(message = "{password.not.null}")
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "{confirm.password.required}")
    private String confirmPassword;

    @NotNull(message = "{birth.date.required}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String profilePicture;
    private String coverPhoto;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Enumerated(EnumType.STRING)
    private Role role;
}
