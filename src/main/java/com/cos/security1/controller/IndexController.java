package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import com.cos.security1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//test
@Controller
public class IndexController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"","/"})
    public String index(){
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정 : templates(prefix)..mustache(suffix)  pom 기본 되어있음
        return "index";
    }

    // Oauth 로그인을 하여도 -> PrincipalDetails
    // 일반 로그인을 하여도 -> PrincipalDetails
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    // 시큐리티콘피그 파일 생성후 작동 안함
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(@Valid User user, Errors errors, Model model){

//      if(bindingResult.hasErrors()){
//          Map<String, String> errorMap = new HashMap<>();
//
//          for (FieldError error : bindingResult.getFieldErrors()){
//              errorMap.put(error.getField(),error.getDefaultMessage());
//          }
        if (errors.hasErrors()) {
            // 회원가입 실패시, 입력 데이터를 유지
            model.addAttribute("user", user);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
          return "/joinForm";
      }
        user.setRole("ROLE_USER");

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);  // 회원가입은 잘됨 but.비밀번호 1234   -> 시큐리티 로그인이 불가 .. 패스워드 암호화가 안되었기 때문
        return "redirect:/loginForm";
    }

    @GetMapping("/joinProc")
    public @ResponseBody String joinProc(){
        return "회원가입완료됨";
    }

    // @EnableGlobalMethodSecurity 사용 하여 가능한 어노테이션 ,, 간단하게 권한 걸때 사용
    @Secured("ROLE_USER")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    // @EnableGlobalMethodSecurity 사용 하여 가능한 어노테이션 ,, 간단하게 권한 걸때 사용
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보";
    }

    // 접근에러
    @GetMapping("/access-denied")
    public String accessDenied(){
        return "AccessDenied";
    }
}
