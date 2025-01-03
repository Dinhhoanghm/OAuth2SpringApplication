package vn.aivhub.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import vn.aivhub.data.tables.pojos.AccountVerifycation;
import vn.aivhub.data.tables.pojos.OauthToken;
import vn.aivhub.data.tables.pojos.ResetPasswordToken;
import vn.aivhub.data.tables.pojos.User;
import vn.aivhub.oauth.config.authentication.JwtService;
import vn.aivhub.oauth.config.exception.ApiException;
import vn.aivhub.oauth.config.jackson.json.JsonObject;
import vn.aivhub.oauth.config.model.SimpleSecurityUser;
import vn.aivhub.oauth.data.dto.EmailDetails;
import vn.aivhub.oauth.data.response.LoginResponseDTO;
import vn.aivhub.oauth.repository.AccountVerificationRepository;
import vn.aivhub.oauth.repository.OauthTokenRepository;
import vn.aivhub.oauth.repository.ResetPasswordTokenRepository;
import vn.aivhub.oauth.repository.UserRepository;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

@Service
public class LoginService {
  @Autowired
  GoogleServiceImpl googleService;
  @Autowired
  JwtService jwtService;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  UserRepository userRepository;
  @Autowired
  AccountVerificationRepository accountVerificationRepository;
  @Autowired
  OauthTokenRepository oauthTokenRepository;
  @Autowired
  ResetPasswordTokenRepository resetPasswordTokenRepository;
  @Value("${spring.google.client-id}")
  String clientId;
  @Value("${spring.google.client-key}")
  String clientSecret;

  @Value("${spring.github.client-id}")
  String  githubClientId;
  @Value("${spring.github.client-key}")
  String githubClientKey;


  private void sendAccountVerificationMail(User user) throws MessagingException, UnsupportedEncodingException {
    AccountVerifycation verification = new AccountVerifycation();
    verification.setId(UUID.randomUUID().toString());
    verification.setUserId(user.getId());
    accountVerificationRepository.save(verification);
    String link = "http://localhost:8080/accountverification" +
      "?id=" + verification.getId();

    EmailDetails emailDetails = new EmailDetails();
    emailDetails.setRecipient(user.getEmail());
    emailDetails.setSubject("Account Verification");
    String body = """
      Thank you for signing up.
      Please verify your account by clicking the link : 
      """ + link;
    emailDetails.setBody(body);

    googleService.sendSimpleMail(emailDetails);
  }

  public LoginResponseDTO login(String username, String password) throws ApiException {
    User user = userRepository.findByEmail(username);
    if (user != null && passwordEncoder.matches(password, user.getPassword())) {
      return saveTokenForUser(user);
    } else {
      throw new ApiException("Invalid username or password");
    }
  }

  public LoginResponseDTO processGrantCode(String code) throws MessagingException, UnsupportedEncodingException {
    String accessToken = getOauthAccessTokenGoogle(code);

    User googleUser = getProfileDetailsGoogle(accessToken);
    User user = userRepository.findByEmail(googleUser.getEmail());

    if (user == null) {
      user = registerUser(googleUser.getFirstName(), googleUser.getLastName(), googleUser.getEmail(), googleUser.getPassword());
    }

    return saveTokenForUser(user);

  }

  public LoginResponseDTO processGithubGrantCode(String code) throws MessagingException, UnsupportedEncodingException {
    String accessToken = getOauthAccessTokenGithub(code);

    User githubUser = getProfileDetailsGithub(accessToken);
    User user = userRepository.findByEmail(githubUser.getEmail());

    if (user == null) {
      user = registerUser(githubUser.getFirstName(), githubUser.getLastName(), githubUser.getEmail(), githubUser.getPassword());
    }

    return saveTokenForUser(user);

  }

  public User registerUser(String firstName, String lastName, String email, String password) throws MessagingException, UnsupportedEncodingException {
    User user = new User();
    user.setEnabled(true);
    user.setEmailVerified(false);
    user.setRole("USER");
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));

    user = userRepository.save(user);
    if (email != null) {
      sendAccountVerificationMail(user);
    }
    return user;
  }


  private User getProfileDetailsGoogle(String accessToken) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setBearerAuth(accessToken);

    HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

    String url = "https://www.googleapis.com/oauth2/v2/userinfo";
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
    JsonObject jsonObject = new JsonObject(response.getBody());
    User user = new User();
    user.setEmail(jsonObject.getString("email").replace("\"", ""));
    user.setFirstName(jsonObject.getString("name").replace("\"", ""));
    user.setLastName(jsonObject.getString("given_name").replace("\"", ""));
    user.setPassword(UUID.randomUUID().toString());
    return user;
  }

  private User getProfileDetailsGithub(String accessToken) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setBearerAuth(accessToken);

    HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

    String url = "https://api.github.com/user";
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
    JsonObject jsonObject = new JsonObject(response.getBody());
    User user = new User();
    user.setEmail(jsonObject.getString("html_url"));
    user.setUsername(jsonObject.getString("login"));
    user.setFirstName(jsonObject.getString("name"));
    user.setPassword("123456789");
    return user;
  }


  private String getOauthAccessTokenGoogle(String code) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", code);
    params.add("redirect_uri", "http://localhost:8080/grantcode");
    params.add("client_id", clientId);
    params.add("client_secret", clientSecret);
    params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
    params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
    params.add("scope", "openid");
    params.add("grant_type", "authorization_code");

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

    String url = "https://oauth2.googleapis.com/token";
    String response = restTemplate.postForObject(url, requestEntity, String.class);
    JsonObject jsonObject = new JsonObject(response);

    return jsonObject.getString("access_token").replace("\"", "");
  }
  private String getOauthAccessTokenGithub(String code) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", code);
    params.add("redirect_uri", "http://localhost:8080/github/grantcode");
    params.add("client_id", githubClientId);
    params.add("client_secret", githubClientKey);
    params.add("scope", "user");
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

    String url = "https://github.com/login/oauth/access_token";
    String response = restTemplate.postForObject(url, requestEntity, String.class);
    JsonObject jsonObject = new JsonObject(response);

    return jsonObject.getString("access_token");
  }
  public String verifyAccount(String id) {
    AccountVerifycation verification = accountVerificationRepository.findById(id).orElse(null);

    if (verification == null) {
      return "Invalid verification token";
    }

    User user = userRepository.findUserById(verification.getUserId()).orElse(null);

    if (user == null) {
      return "User not found";
    }

    user.setEmailVerified(true);
    userRepository.save(user);
    accountVerificationRepository.deleteById(verification.getId());
    return "Account verified successfully!";
  }


  public String initiateResetPasswordLink(String email) throws MessagingException, UnsupportedEncodingException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      return "Email address not registered";
    }

    ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
    resetPasswordToken.setToken(generateSecureToken());
    resetPasswordToken.setUserId(user.getId());
    resetPasswordToken.setExpirationTime(LocalDateTime.now().plusHours(1));

    resetPasswordTokenRepository.save(resetPasswordToken);

    String link = "http://localhost:8080/changepassword?token=" + resetPasswordToken.getToken();

    EmailDetails emailDetails = new EmailDetails();
    emailDetails.setRecipient(user.getEmail());
    emailDetails.setSubject("Reset Password for your Account");
    emailDetails.setBody("Click the link to reset your account password " + link);

    googleService.sendSimpleMail(emailDetails);
    return "Password reset link sent to registered email address";
  }

  private String generateSecureToken() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] tokenBytes = new byte[24];
    secureRandom.nextBytes(tokenBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }

  public String changePasswordWithToken(String token, String newPassword) {
    ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken(token).orElse(null);
    if (resetPasswordToken == null) {
      return "Invalid Token";
    }

    if (resetPasswordToken.getExpirationTime().isBefore(LocalDateTime.now())) {
      return "Token expired";
    }

    User user = userRepository.findUserById(resetPasswordToken.getUserId()).orElse(null);
    if (user != null) {
      user.setPassword(passwordEncoder.encode(newPassword));
      userRepository.update(user);
    }
    return "Password changed successfully";
  }

  public String logout(String email) {
    User user = userRepository.findByEmail(email);
    oauthTokenRepository.deleteByUser(user);
    return "Signed out successfully!";
  }

  public LoginResponseDTO refreshAccessToken(String refreshToken) throws ApiException {
    OauthToken oauthToken = oauthTokenRepository.findByRefreshToken(refreshToken);
    if (oauthToken == null) {
      throw new ApiException("Invalid refresh token");
    }
    LoginResponseDTO res = saveTokenForUser(new User().setId(oauthToken.getUserId()));
    oauthTokenRepository.delete(oauthToken);
    return res;
  }

  private LoginResponseDTO saveTokenForUser(User user) {
    SimpleSecurityUser securityUser = convertToSecurityUser(user);
    LoginResponseDTO dto = generateToken(securityUser);
    OauthToken token = new OauthToken();
    token.setAccessToken(dto.getAccessToken());
    token.setRefreshToken(dto.getRefreshToken());
    token.setAccessExpirationTime(dto.getExpirationTime());
    token.setRefreshExpirationTime(LocalDateTime.now().plusDays(1));
    token.setUserId(user.getId());

    oauthTokenRepository.save(token);
    return dto;
  }

  private LoginResponseDTO generateToken(SimpleSecurityUser securityUser) {
    LoginResponseDTO res = new LoginResponseDTO();
    res.setAccessToken(jwtService.generateJwt(securityUser));
    res.setRefreshToken(UUID.randomUUID().toString());
    res.setExpirationTime(LocalDateTime.now().plusHours(1));
    return res;
  }

  public String getAccessTokenFromRefreshToken(String refreshToken) throws ApiException {
    OauthToken oauthToken = oauthTokenRepository.findByRefreshToken(refreshToken);
    if (oauthToken == null) {
      throw new ApiException("Not exist refreshToken");
    }
    OauthToken token = verifyExpiration(oauthToken);
    return token.getAccessToken();
  }


  public OauthToken verifyExpiration(OauthToken token) throws ApiException {
    if (token.getRefreshExpirationTime().compareTo(LocalDateTime.now()) < 0) {
      User user = userRepository.findUserById(token.getUserId()).orElse(null);
      if (user == null) {
        throw new ApiException("Not exist user");
      }
      SimpleSecurityUser securityUser = convertToSecurityUser(user);
      token.setAccessToken(jwtService.generateJwt(securityUser));
      oauthTokenRepository.updateAccessToken(token);
    } else {

      throw new ApiException("Refresh token was expired. Please make a new signin request");
    }
    return token;
  }

  private SimpleSecurityUser convertToSecurityUser(User user) {
    SimpleSecurityUser securityUser = new SimpleSecurityUser();
    securityUser.setId(user.getId().toString());
    securityUser.setUsername(user.getUsername());
    securityUser.setRoles(Arrays.asList(user.getRole()));
    return securityUser;
  }
}
