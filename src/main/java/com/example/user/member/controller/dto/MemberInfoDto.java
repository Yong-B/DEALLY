package com.example.user.member.controller.dto;

import com.example.user.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoDto {
    private String loginId;
    private String email;
    private String name;
}