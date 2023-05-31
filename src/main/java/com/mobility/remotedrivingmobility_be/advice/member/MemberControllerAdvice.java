//package com.mobility.remotedrivingmobility_be.advice.member;
//
//import com.mobility.remotedrivingmobility_be.common.ResponseError;
//import com.mobility.remotedrivingmobility_be.controller.member.MemberApiController;
//import com.mobility.remotedrivingmobility_be.controller.member.MemberAuthApiController;
//import com.mobility.remotedrivingmobility_be.controller.member.SignupApiController;
//import com.mobility.remotedrivingmobility_be.exception.member.*;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.util.StringUtils;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import static com.mobility.remotedrivingmobility_be.common.ResConditionCode.*;
//
//@RestControllerAdvice(assignableTypes = {MemberAuthApiController.class, SignupApiController.class, MemberApiController.class})
//@Slf4j
//public class MemberControllerAdvice {
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler
//    public ResponseError emptyArgument(MethodArgumentNotValidException ex) {
//        ResponseError responseError = makeErrorResponse(ex.getBindingResult());
//        return responseError;
//    }
//
//    private ResponseError makeErrorResponse(BindingResult bindingResult) {
//        ResponseError responseError = null;
//
//        if (bindingResult.hasErrors()) {
//            String bindResultField = bindingResult.getFieldError().getField();
//            boolean isNotBlank = StringUtils.hasText(bindingResult.getFieldError().getRejectedValue().toString());
//            log.error("bindResultField: {}", bindResultField);
//
//            if(isNotBlank) {
//                switch(bindResultField) {
//                    case "loginId":
//                        responseError = new ResponseError(LOGIN_ID_WRONG_TYPE);
//                        break;
//                    case "password":
//                        responseError = new ResponseError(PASSWORD_WRONG_TYPE);
//                        break;
//                    case "name":
//                        responseError = new ResponseError(NAME_WRONG_TYPE);
//                        break;
//                }
//            } else {
//                switch (bindResultField) {
//                    case "loginId":
//                        responseError = new ResponseError(LOGIN_ID_NULL);
//                        break;
//                    case "password":
//                        responseError = new ResponseError(PASSWORD_NULL);
//                        break;
//                    case "name":
//                        responseError = new ResponseError(NAME_NULL);
//                        break;
//                }
//            }
//        }
//        return responseError;
//    }
//
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler
//    public ResponseError notExistIdExHandler(UsernameNotFoundException ex) {
//        log.error("[exceptionHandler] UsernameNotFoundException", ex);
//        return new ResponseError(LOGIN_ID_NOT_EXIST);
//    }
//
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler
//    public ResponseError invalidPWExHandler(InvalidPasswordException ex) {
//        log.error("[exceptionHandler] InvalidPasswordException", ex);
//        return new ResponseError(PASSWORD_INVALID);
//    }
//
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ExceptionHandler
//    public ResponseError noAuthorityExHandler(NoAuthorityToLoginException ex) {
//        log.error("[exceptionHandler] NoAuthorityToLoginException", ex);
//        return new ResponseError(ACCESS_DENIED);
//    }
//
//    @ResponseStatus(HttpStatus.CONFLICT)
//    @ExceptionHandler
//    public ResponseError duplicatedLoginIdExHandler(LoginIdDuplicatedException ex) {
//        log.error("[exceptionHandler] LoginIdDuplicatedException", ex);
//        return new ResponseError(LOGIN_ID_DUPLICATED);
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler
//    public ResponseError notExistEqualRefreshTokenExHandler(NotExistsEqualRefreshTokenException ex) {
//        log.error("[exceptionHandler] NotExistsEqualRefreshTokenException", ex);
//        return new ResponseError(REFRESH_TOKEN_NOT_EXIST_IN_STORE);
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler
//    public ResponseError notBearerTokenExHandler(TokenNotBearerTypeException ex) {
//        log.error("[exceptionHandler] TokenNotBearerTypeException", ex);
//        return new ResponseError(TOKEN_NOT_BEARER);
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler
//    public ResponseError refreshTokenIsExpiredExHandler(RefreshTokenIsExpiredException ex) {
//        log.error("[exceptionHandler] RefreshTokenIsExpiredException", ex);
//        return new ResponseError(REFRESH_TOKEN_EXPIRED);
//    }
//
//}
