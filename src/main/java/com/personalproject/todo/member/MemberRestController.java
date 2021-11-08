package com.personalproject.todo.member;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("member")
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 아이디 중복 여부 반환
    @PostMapping("isOverlapId")
    public boolean isOverlapId(String id) {
        return memberService.isOverlapId(id);
    }
}
