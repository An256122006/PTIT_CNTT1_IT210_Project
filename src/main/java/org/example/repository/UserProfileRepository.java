package org.example.repository;

import org.example.model.UserProfiles;
import org.example.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfiles, Long> {
    UserProfiles findByUser(Users user);
}
