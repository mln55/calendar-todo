package com.personalproject.todo.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("member")
public class MemberViewController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MemberViewController(MemberService memberService, BCryptPasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 페이지
    @GetMapping("signup")
    public String signup() {
        return "signup";
    }

    // 회원가입 처리 후 redirect
    @PostMapping("signupProcess")
    public RedirectView signupProcess(
        String id,
        String pw,
        RedirectAttributes redirectAttributes
    ) {
        String redirectURI;
        if (memberService.isOverlapId(id)) {
            redirectAttributes.addFlashAttribute("signupFailOverlapId", "이미 사용중인 아이디입니다.");
            redirectAttributes.addFlashAttribute("overlapId", id);
            redirectURI = "/member/signup";
        } else {
            Member member = new Member.Builder()
                .id(id)
                .password(passwordEncoder.encode(pw))
                .build();
            memberService.insertMember(member);
            redirectAttributes.addFlashAttribute("signupSuccessMsg", "회원가입이 완료되었습니다.");
            redirectURI = "/";
        }
        return new RedirectView(redirectURI);
    }
}
