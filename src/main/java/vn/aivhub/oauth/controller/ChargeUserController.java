package vn.aivhub.oauth.controller;

import org.mapstruct.Context;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.aivhub.data.tables.pojos.PlanUser;
import vn.aivhub.oauth.data.response.ChargePlanResponse;
import vn.aivhub.oauth.data.response.PlanUserResponse;
import vn.aivhub.oauth.service.PlanUserService;

import java.util.List;

import static vn.aivhub.oauth.util.SecurityUtils.userId;

@RestController
@RequestMapping("/api/planUser")
public class ChargeUserController {
  private final PlanUserService planPlanUserService;

  public ChargeUserController(PlanUserService planPlanUserService) {
    this.planPlanUserService = planPlanUserService;
  }

  @GetMapping("/getAll")
  public List<PlanUser> getAllPlanUsers() {
    return planPlanUserService.findAll();
  }

  @GetMapping("/getAllByUser")
  public List<PlanUserResponse> getAllPlanByUser(@Context Authentication authentication) {
    Integer userId = userId(authentication);
    return planPlanUserService.findAllPlanByUserId(userId);
  }

  @PostMapping("/insert")
  public PlanUser savePlanUser(@RequestBody PlanUser planPlanUser, @Context Authentication authentication) {
    planPlanUser.setStatus("INACTIVE");
    Integer userId = userId(authentication);
    planPlanUser.setUserId(userId);
    return planPlanUserService.addPlanUser(planPlanUser);
  }


  @PostMapping("/update")
  public PlanUser update(@RequestBody PlanUser planPlanUser) {
    return planPlanUserService.updatePlanUser(planPlanUser);
  }


  @DeleteMapping("/delete")
  public String delete(@RequestBody PlanUser planPlanUser) {
    planPlanUserService.deletePlanUser(planPlanUser);
    return "success";
  }
}
