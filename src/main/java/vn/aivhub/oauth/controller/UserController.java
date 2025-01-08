package vn.aivhub.oauth.controller;

import org.springframework.web.bind.annotation.*;
import vn.aivhub.data.tables.pojos.User;
import vn.aivhub.oauth.service.UserService;

import java.util.List;

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

  @PostMapping("/insert")
  public User saveUser(@RequestBody User user) {
    return userService.addUser(user);
  }


  @PostMapping("/update")
  public User update(@RequestBody User user) {
    return userService.updateUser(user);
  }


  @DeleteMapping("/delete")
  public String delete(@RequestBody User user) {
    userService.deleteUser(user);
    return "success";
  }
}
