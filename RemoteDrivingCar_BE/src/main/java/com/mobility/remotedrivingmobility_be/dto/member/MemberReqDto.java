package com.mobility.remotedrivingmobility_be.dto.member;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberReqDto {
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    public static class LoginReq {

        @NotBlank(message = "아이디를 입력해주세요.")
        private String loginId;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(max = 17)
        private String password;
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter @Setter
    public static class SignupReq {

        @Size(max = 12)
        private String loginId;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(message = "최소 한개 이상의 대문자 또는 소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.",
                regexp = "^(?=.*[a-zA-Z])(?=.*[\\W_])(?=.*\\d)(?!.*\\s).{8,16}$")
        @Size(max = 17)
        private String password;

    }

}
