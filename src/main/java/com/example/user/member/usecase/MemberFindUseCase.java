package com.example.user.member.usecase;

import com.example.user.member.domain.Member;
import java.util.Optional;

public interface MemberFindUseCase {
    Member findByLoginId(String loginId);

}
