package org.example.service;

import org.example.model.UserProfiles;
import org.example.model.Users;

public interface IUserProfileService {
    void save(UserProfiles userProfiles);
    UserProfiles findByUser(Users user);

}
