package com.sparta.teammember.controller;

import com.sparta.teammember.service.ProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberProfileController {

    private final ProfileImageService profileImageService;

    // 프로필 이미지 업로드
    @PostMapping("/{memberId}/profile-image")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable Long memberId,
            @RequestParam("file")MultipartFile file
            ) throws IOException {
        profileImageService.uploadProfileImage(memberId, file);
        return ResponseEntity.ok("프로필 이미지 업로드가 완료되었습니다.");
    }

    // Presigned URL 발급
    @GetMapping("{memberId}/profile-image")
    public ResponseEntity<String> getProfileImage(
            @PathVariable Long memberId
    ) {
        String presignedUrl = profileImageService.generatePresignedUrl(memberId);
        return ResponseEntity.ok(presignedUrl);
    }
}
