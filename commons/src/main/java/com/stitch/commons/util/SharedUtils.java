package com.stitch.commons.util;

import com.stitch.commons.exception.StitchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class SharedUtils {

    public static Optional<String> getLoggedInUser() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            if(Objects.nonNull(context)){
                Authentication authentication= context.getAuthentication();
                String loggedInUser = authentication.getName();
                System.err.println("logged in user --> "+loggedInUser);
                return Optional.ofNullable(loggedInUser);
            }
            return Optional.empty();
        }catch (Exception e){
            log.error("Exception occurred while fetching logged in user ==> {}",e.getMessage());
            return Optional.empty();
        }
    }

    public static void validateField(String value, String type) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new StitchException(String.format("%s is required", type));
        }
    }
}
