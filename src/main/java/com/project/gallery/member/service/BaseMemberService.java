package com.project.gallery.member.service;

import com.project.gallery.member.entity.Member;
import com.project.gallery.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BaseMemberService implements MemberService {

    private final MemberRepository memberRepository;

    // 회원 데이터 저장
    @Override
    public void save(String name, String loginId, String loginPw) {
        memberRepository.save(new Member(name, loginId, loginPw));
    }

    // 회원 데이터 조회
    @Override
    public Member find(String loginId, String loginPw) {
       Optional<Member> memberOptional = memberRepository.findByLoginIdAndLoginPw(loginId, loginPw);

       return memberOptional.orElse(null);
    }
}
