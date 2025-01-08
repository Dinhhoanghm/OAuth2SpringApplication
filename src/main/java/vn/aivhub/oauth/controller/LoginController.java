package vn.aivhub.oauth.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.aivhub.oauth.config.exception.ApiException;
import vn.aivhub.oauth.data.request.UserRequest;
import vn.aivhub.oauth.data.response.LoginResponseDTO;
import vn.aivhub.oauth.service.LoginService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
public class LoginController {
  private final LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/register")
  public String register(@RequestBody UserRequest userRequest) throws MessagingException, UnsupportedEncodingException {
    loginService.registerUser(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getEmail(), userRequest.getPassword());
    return "User registered successfully! You need activate your email address";
  }

  @PostMapping("/login")
  public LoginResponseDTO login(@RequestBody UserRequest userRequest) throws ApiException {
    return loginService.login(userRequest.getUsername(), userRequest.getPassword());
  }

  @GetMapping("/grantcode")
  public LoginResponseDTO grantCode(@RequestParam("code") String code,
                                    @RequestParam("scope") String scope,
                                    @RequestParam("authuser") String authUser,
                                    @RequestParam("prompt") String prompt) throws MessagingException, UnsupportedEncodingException {
    return loginService.processGrantCode(code);
  }

  @GetMapping("/github/grantcode")
  public LoginResponseDTO githubGrantCode(@RequestParam("code") String code) throws MessagingException, UnsupportedEncodingException {
    return loginService.processGithubGrantCode(code);
  }

  @GetMapping("/accountverification")
  public String verifyAccount(@RequestParam("id") String id) {
    return loginService.verifyAccount(id);
  }

  @PostMapping("/generateresetpasswordlink")
  public String generateResetPasswordLink(@RequestParam String email) throws MessagingException, UnsupportedEncodingException {
    return loginService.initiateResetPasswordLink(email);
  }

  @GetMapping("/refreshtoken")
  public String refreshtoken(@RequestParam("refreshToken") String requestRefreshToken) throws ApiException {
    return loginService.getAccessTokenFromRefreshToken(requestRefreshToken);
  }

  @GetMapping("/changepassword")
  public String changePassword(@RequestParam("token") String token,
                               @RequestParam(value = "new_password", defaultValue = "123456789") String newPassword) {
    return loginService.changePasswordWithToken(token, newPassword);
  }

  @PutMapping("/logoutuser")
  public String logoutUser() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return loginService.logout(email);
  }

  @PostMapping("/refreshaccesstoken")
  public LoginResponseDTO refreshAccessToken(@RequestParam("refresh_token") String refreshToken) throws ApiException {
    return loginService.refreshAccessToken(refreshToken);
  }
}
