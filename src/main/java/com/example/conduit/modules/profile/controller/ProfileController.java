package com.example.conduit.modules.profile.controller;

import com.example.conduit.modules.profile.dto.ProfileDTO;
import com.example.conduit.modules.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@Tag(name = "Profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable String username) {
        ProfileDTO profileDTO = new ProfileDTO(profileService.getProfileWithUsername(username));
        return ResponseEntity.ok(profileDTO);
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<ProfileDTO> followUser(@PathVariable String username) {
        ProfileDTO profileDTO = new ProfileDTO(profileService.followProfileWithUsername(username));
        return ResponseEntity.ok(profileDTO);
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<ProfileDTO> unfollowUser(@PathVariable String username) {
        ProfileDTO profileDTO = new ProfileDTO(profileService.unfollowUser(username));
        return ResponseEntity.ok(profileDTO);
    }
}
