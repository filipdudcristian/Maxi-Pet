package com.example.MaxiPet.validators;

import com.example.MaxiPet.constants.UserConstants;
import com.example.MaxiPet.dto.UserDTO;
import com.example.MaxiPet.entity.User;
import com.example.MaxiPet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserValidator {

    private final UserRepository userRepository;

    public void validateUserId(Integer userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn(UserConstants.USER_NOT_FOUND, userId);
            throw new Exception(UserConstants.EXCEPTION_USER_NOT_FOUND);
        }
    }

    public void validateUserDTOForUpdate(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            log.warn(UserConstants.USER_DTO_EMPTY);
            throw new Exception(UserConstants.EXCEPTION_USER_DTO_NULL);
        }

        validateUserId(userDTO.getId());
        validateUserFields(userDTO);
    }

    public void validateUserFields(UserDTO userDTO) throws Exception {
        if (userDTO.getRole() == null || userDTO.getRole().isEmpty()) {
            log.warn(UserConstants.USER_ROLE_EMPTY);
            throw new Exception(UserConstants.EXCEPTION_USER_ROLE_NULL);
        }
        if (userDTO.getName() == null || userDTO.getName().isEmpty()) {
            log.warn(UserConstants.USER_NAME_EMPTY);
            throw new Exception(UserConstants.EXCEPTION_USER_NAME_NULL);
        }
        if (userDTO.getFirstName() == null || userDTO.getFirstName().isEmpty()) {
            log.warn(UserConstants.USER_FIRST_NAME_EMPTY);
            throw new Exception(UserConstants.EXCEPTION_USER_FIRST_NAME_NULL);
        }
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty() || !emailPatternValidator(userDTO.getEmail())) {
            log.warn(UserConstants.USER_EMAIL_INVALID);
            throw new Exception(UserConstants.EXCEPTION_USER_EMAIL_INVALID);
        }
        if (userDTO.getPhone() == null || userDTO.getPhone().isEmpty() || !phonePatternValidator(userDTO.getPhone())) {
            log.warn(UserConstants.USER_PHONE_INVALID);
            throw new Exception(UserConstants.EXCEPTION_USER_PHONE_INVALID);
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            log.warn(UserConstants.USER_PASSWORD_EMPTY);
            throw new Exception(UserConstants.EXCEPTION_USER_PASSWORD_NULL);
        }
    }

    public static boolean emailPatternValidator(String emailAddress) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(emailAddress).matches();
    }

    public static boolean phonePatternValidator(String phone) {
        String phoneRegex = "^\\d{10}$";
        return Pattern.compile(phoneRegex).matcher(phone).matches();
    }
}
