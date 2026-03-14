package com.stitch.user.service;

import com.stitch.commons.model.dto.Response;
import com.stitch.user.model.dto.ChangePasswordRequest;
import com.stitch.user.model.dto.PasswordResetRequest;

public interface PasswordService {


    Response requestPasswordReset(String emailAddress);

    Response resetPassword(PasswordResetRequest passwordResetRequest);

    Response changePassword(ChangePasswordRequest passwordResetRequest);

    String encode(String password);

    void validateNewPassword(PasswordResetRequest passwordResetRequest);

    void validateNewPassword(ChangePasswordRequest passwordResetRequest);

    void validateNewPassword(String password);

    Response validatePasswordResetCode(PasswordResetRequest passwordResetRequest);

    boolean passwordMatch(String rawPassword, String encrypted);
}
