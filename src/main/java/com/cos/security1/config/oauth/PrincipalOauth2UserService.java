package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.oauth.provider.FacebookUserInfo;
import com.cos.security1.config.oauth.provider.GoogleUserInfo;
import com.cos.security1.config.oauth.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 userRequest 데이터 에 대한 후처리 되는 메서드
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //System.out.println("getClientRegistration" + userRequest.getClientRegistration());      // 어떤 oauth 로 로그인했는지 확인
        //System.out.println("getAccessToken" + userRequest.getAccessToken().getTokenValue());
        // 구글로그인 클릭 -> code를 리턴(oauth-client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글 회원프로필 받음

        OAuth2User oAuth2User = super.loadUser(userRequest);

        //System.out.println("getAttribute" + super.loadUser(userRequest).getAttributes());

        OAuth2UserInfo oAuth2UserInfo=null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        }else {
            System.out.println("우리는 구글과 페이스북만 지원해요~~");
        }

        String provider = oAuth2UserInfo.getProvider();    //  google
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;          // ex) google_00000
        String email = oAuth2User.getAttribute("email");
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
            System.out.println("구글 로그인이 최초입니다.");
        }else {
            System.out.println("이미 회원가입 되어있습니다.");
        }

        return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
    }
}
