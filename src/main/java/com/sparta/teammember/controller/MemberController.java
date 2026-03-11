package com.sparta.teammember.controller;

import com.sparta.teammember.dto.MemberRequestDto;
import com.sparta.teammember.dto.MemberResponseDto;
import com.sparta.teammember.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    // 팀원 등록
    @PostMapping
    public ResponseEntity<MemberResponseDto> saveMember(@RequestBody MemberRequestDto requestDto) {
        log.info("[API - LOG] 팀원 등록 요청: 이름={}", requestDto.getName());
        return ResponseEntity.ok(memberService.saveMember(requestDto));
    }

    // 팀원 상세 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long memberId) {
        log.info("[API -LOG] 팀원 상세 조회 요청: ID={}", memberId);
        return ResponseEntity.ok(memberService.getMember(memberId));
    }

    // 팀원 전제 조회
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        log.info("[API - LOG] 팀원 전체 조회 요청");
        return ResponseEntity.ok(memberService.getMembers());
    }
}
