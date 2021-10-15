package com.personalproject.todo.controller;

import com.personalproject.todo.service.MemberService;
import com.personalproject.todo.vo.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/signup")
    public ModelAndView signup(
            ModelAndView mv) {
        mv.setViewName("signup");
        return mv;
    }

    @PostMapping("/signupProcess")
    public RedirectView signupProcess(
            String id,
            String pw,
            RedirectAttributes redirectAttributes) {
        String redirectURI;

        if (memberService.isOverlapId(id)) {
            redirectAttributes.addFlashAttribute("signupFailOverlapId", "이미 사용중인 아이디입니다.");
            redirectAttributes.addFlashAttribute("overlapId", id);
            redirectURI = "/member/signup";
        } else {
            Member member = new Member();
            member.setId(id);
            member.setPassword(pw);
            memberService.insertMember(member);
            redirectAttributes.addFlashAttribute("signupSuccessMsg", "회원가입이 완료되었습니다.");
            redirectURI = "/";

        }

        return new RedirectView(redirectURI);
    }

    @PostMapping("/isOverlapId")
    @ResponseBody
    public boolean isOverlapId(String id) {
        return memberService.isOverlapId(id);
    }
}
