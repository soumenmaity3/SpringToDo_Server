package com.soumen.SpringToDo.Repository;

import com.soumen.SpringToDo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToDoRepo extends JpaRepository<User,Long> {
    @Transactional
    @Modifying
    @Query(value = "Insert into users(email,password,user_name) values(:email,:password,:user_name)",nativeQuery = true)
    int register(String email, String password, String user_name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM to_do WHERE email = :email AND completed = true", nativeQuery = true)
    int deleteHistory(@Param("email") String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    void deleteByEmailAndPassword(String email, String password);

    @Query(value = "SELECT email FROM users WHERE email = :email", nativeQuery = true)
    List<String> checkUserEmail(@Param("email") String email);

    @Query(value = "SELECT password FROM users  WHERE email = :email",nativeQuery = true)
    String checkUserPasswordByEmail(@Param("email")String  email);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT user_name FROM users WHERE email = :email",nativeQuery = true)
    String findUsernameByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "UPDATE to_do SET completed = false WHERE completed = true AND email=:email", nativeQuery = true)
    int recoverDataByEmail(@Param("email") String email);

    @Query(value = "SELECT email FROM users WHERE email = :email", nativeQuery = true)
    String checkUserEmail2(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET password=:password WHERE email=:email",nativeQuery = true)
    int resatPassword(@Param("email") String email,@Param("password") String password);
}
