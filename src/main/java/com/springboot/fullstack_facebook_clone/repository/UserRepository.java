package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<User> searchUser(@Param("search") String search, Pageable pageable);
    @Query("SELECT u FROM User u WHERE u.userId IN :userIds")
    List<User> findUsersById(@Param("userIds") List<Long> userIds);
}
