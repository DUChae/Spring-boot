package com.example.demo.controller;


import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원가입 폼 이동
    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new User());
        return "user/register";
    }

    //회원가입 처리
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               Model model) {
        if(bindingResult.hasErrors()){
            return "user/register"; //유효성 검사 실패 시 회원가입 폼으로 돌아감
        }
        try{
            userService.registerUser(user);
        }catch(IllegalArgumentException e){
            model.addAttribute("registrationError",e.getMessage());
            return "user/register"; //아이디 중복 등 예외 발생 시 회원가입 폼으로 돌아감
        }

        return "redirect:/login"; //회원가입 성공 후 로그인 페이지로 리다이렉트
    }

}
