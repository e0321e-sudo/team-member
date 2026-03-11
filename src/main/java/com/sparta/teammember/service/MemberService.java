package com.sparta.teammember.service;

import com.sparta.teammember.dto.MemberRequestDto;
import com.sparta.teammember.dto.MemberResponseDto;
import com.sparta.teammember.entity.Member;
import com.sparta.teammember.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 팀원 등록
    @Transactional
    public MemberResponseDto saveMember(MemberRequestDto requestDto) {
        Member member = new Member(
                requestDto.getName(),
                requestDto.getAge(),
                requestDto.getMbti()
        );
        Member savedMember = memberRepository.save(member);
        return new MemberResponseDto(savedMember);
    }

    // 팀원 상세 조회
    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("해당 팀원을 찾을 수 없습니다: " + memberId)
        );
        return new MemberResponseDto(member);
    }

    // 팀원 전체 조회
    @Transactional(readOnly = true)
    public List<MemberResponseDto> getMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

}
