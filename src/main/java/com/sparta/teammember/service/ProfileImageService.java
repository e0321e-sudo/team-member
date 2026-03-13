package com.sparta.teammember.service;

import com.sparta.teammember.entity.Member;
import com.sparta.teammember.repository.MemberRepository;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final S3Template s3Template;
    private final MemberRepository memberRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    // 이미지 업로드 및 DB 업데이트
    @Transactional
    public void uploadProfileImage(Long memberId, MultipartFile file) throws IOException {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("해당 팀원은 찾을 수 없습니다.")
        );
        // 파일명 중복 방지를 위해 UUID 생성 (S3에 저장될 Key)
        String s3Key = "profiles/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        // S3에 업로드
        s3Template.upload(bucketName, s3Key, file.getInputStream());
        // DB에 S3 Key 저장
        member.updateProfileImage(s3Key);
    }

    // presigned URL 생성 (유효기간: 7일)
    public String generatePresignedUrl(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("해당 팀원은 찾을 수 없습니다.")
        );

        String s3Key = member.getProfileImageUrl();
        if (s3Key == null || s3Key.isEmpty()) {
            throw new IllegalStateException("등록된 프로필 이미지가 없습니다.");

        }
        // 7일 유효기간 설정
        return s3Template.createSignedGetURL(bucketName, s3Key, Duration.ofDays(7)).toString();
    }
}
