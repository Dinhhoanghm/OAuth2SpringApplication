package vn.aivhub.oauth.controller;

import io.reactivex.rxjava3.core.Single;
import org.mapstruct.Context;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.aivhub.data.tables.pojos.User;
import vn.aivhub.oauth.service.UserService;

import java.util.List;

import static vn.aivhub.oauth.util.SecurityUtils.userId;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/getAll")
  public List<User> getAllUsers() {
    return userService.findAll();
  }

  @GetMapping("/get")
  public Single<User> getUserInformation(@Context Authentication authentication) {
    Integer userId = userId(authentication);
    return userService.getById(userId);
  }

  @PostMapping("/insert")
  public User saveUser(@RequestBody User user) {
    return userService.addUser(user);
  }


  @PostMapping("/update")
  public User update(@RequestBody User user) {
    return userService.updateUser(user);
  }

  @GetMapping("/changepassword")
  public Single<String> changePassword(@RequestParam(value = "new_password", defaultValue = "123456789") String newPassword,
                                     @RequestParam(value = "old_password", defaultValue = "123456789") String oldPassword,
                                     @Context Authentication authentication) {
    Integer userId = userId(authentication);
    return userService.changePassword(userId, newPassword,oldPassword);
  }

  @DeleteMapping("/delete")
  public String delete(@RequestBody User user) {
    userService.deleteUser(user);
    return "success";
  }
}
