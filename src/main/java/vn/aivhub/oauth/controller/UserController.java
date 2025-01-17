package vn.aivhub.oauth.controller;

import io.reactivex.rxjava3.core.Single;
import org.mapstruct.Context;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.aivhub.data.tables.pojos.Organization;
import vn.aivhub.data.tables.pojos.User;
import vn.aivhub.oauth.data.request.UserRequest;
import vn.aivhub.oauth.data.response.UserResponse;
import vn.aivhub.oauth.repository.OrganizationRepository;
import vn.aivhub.oauth.repository.UserRepository;
import vn.aivhub.oauth.service.UserService;

import java.util.List;

import static vn.aivhub.data.Tables.USER;
import static vn.aivhub.oauth.util.SecurityUtils.userId;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private final UserService userService;
  private final UserRepository userRepository;
  private final OrganizationRepository organizationRepository;

  public UserController(UserService userService, UserRepository userRepository, OrganizationRepository organizationRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
    this.organizationRepository = organizationRepository;
  }

  @GetMapping("/getAll")
  public Single<List<UserResponse>> getAllUsers(@Context Authentication authentication) {
    Integer userId = userId(authentication);
    User user = userRepository.findUserById(userId)
      .get();
    return userService.findAllByCondition(USER.ORG_ID.eq(user.getOrgId()));
  }

  @GetMapping("/get")
  public Single<UserResponse> getUserInformation(@Context Authentication authentication) {
    Integer userId = userId(authentication);
    return userService.getById(userId);
  }

  @PostMapping("/insert")
  public User saveUser(@RequestBody User user, @Context Authentication authentication) {
    Integer userId = userId(authentication);
    User admin = userRepository.findUserById(userId).get();
    user.setOrgId(admin.getOrgId());
    return userService.addUser(user);
  }


  @PostMapping("/update")
  public User update(@RequestBody UserRequest user) {
    if (user.getIsAdmin()!= null && user.getIsAdmin().equals(true) || user.getRole().equalsIgnoreCase("admin")) {
      String company = user.getCompany();
      Organization organization = new Organization();
      organization.setId(user.getOrgId());
      organization.setName(company);
      organizationRepository.update(organization);
    }
    return userService.updateUser(user);
  }

  @GetMapping("/changepassword")
  public Single<String> changePassword(@RequestParam(value = "new_password", defaultValue = "123456789") String newPassword,
                                       @RequestParam(value = "old_password", defaultValue = "123456789") String oldPassword,
                                       @Context Authentication authentication) {
    Integer userId = userId(authentication);
    return userService.changePassword(userId, newPassword, oldPassword);
  }

  @DeleteMapping("/delete")
  public String delete(@RequestBody User user) {
    userService.deleteUser(user);
    return "success";
  }
}
