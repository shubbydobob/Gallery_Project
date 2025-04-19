package com.project.gallery.member.service;

import com.project.gallery.common.util.HashUtils;
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
        // 솔트 생성
        String loginPwSalt = HashUtils.generateSalt(16);

        // 입력 패스워드에 솔트를 적용
        String loginPwSalted = HashUtils.generateHash(loginPw, loginPwSalt);

        // 회원 데이터 저장
        memberRepository.save(new Member(name, loginId, loginPwSalted, loginPwSalt));
    }

    @Override
    public Member find(String loginId) {
        return memberRepository.findByLoginId(loginId).orElse(null);
    }

    // 회원 데이터 조회
    @Override
    public Member find(String loginId, String loginPw) {
       Optional<Member> memberOptional = memberRepository.findByLoginId(loginId);

        // 회원 데이터가 있으면
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            // 솔트 조회
            String loginPwSalt = memberOptional.get().getLoginPwSalt();

            // 입력 패스워드에 솔트를 적용
            String loginPwSalted = HashUtils.generateHash(loginPw, loginPwSalt);

            if (member.getLoginPw().equals(loginPwSalted)) {
                return member;
            }
        }

       return null;
    }
}
