package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.UserProfiles;
import org.example.model.Users;
import org.example.repository.UserProfileRepository;
import org.example.service.IUserProfileService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService implements IUserProfileService {
    private final UserProfileRepository userProfileRepository;
    @Override
    public void save(UserProfiles userProfiles) {
        userProfileRepository.save(userProfiles);
    }

    @Override
    public UserProfiles findByUser(Users user) {
        return userProfileRepository.findByUser(user);
    }
}
