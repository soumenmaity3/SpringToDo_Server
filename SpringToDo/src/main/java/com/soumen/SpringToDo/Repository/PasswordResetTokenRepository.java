package com.soumen.SpringToDo.Repository;

import com.soumen.SpringToDo.Model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO password_reset_tokens (otp_code, created_at, expires_at, used, user_email) " +
            "VALUES (:otp, :createdAt, :expiresAt, :used, :email)", nativeQuery = true)
    void saveAllTable(@Param("otp") String otp,
                      @Param("createdAt") LocalDateTime createdAt,
                      @Param("expiresAt") LocalDateTime expiresAt,
                      @Param("used") boolean used,
                      @Param("email") String email);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM password_reset_tokens t WHERE t.user_email = :email",nativeQuery = true)
    void deleteByUserEmail(@Param("email") String email);

    @Query(value = "SELECT otp_code FROM password_reset_tokens WHERE user_email=:email AND used=false",nativeQuery = true)
    String checkOtpByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE password_reset_tokens SET used=true WHERE user_email=:email",nativeQuery = true)
    int changeUsedToTrue(String email);


}
