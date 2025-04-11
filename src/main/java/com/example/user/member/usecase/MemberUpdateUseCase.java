package com.example.user.member.usecase;


import com.example.user.member.domain.Member;

public interface MemberUpdateUseCase {
    Member updatePassword(String loginId, String newPassword);
}