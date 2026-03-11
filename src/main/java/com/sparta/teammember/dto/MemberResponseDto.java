package com.sparta.teammember.dto;

import com.sparta.teammember.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private Long id;
    private String name;
    private Integer age;
    private String mbti;

    public MemberResponseDto(Member member) {
        this.id = getId();
        this.name = getName();
        this.age = getAge();
        this.mbti = getMbti();
    }
}
