package vn.aivhub.oauth.controller;

import org.springframework.web.bind.annotation.*;
import vn.aivhub.data.tables.pojos.ChargePlan;
import vn.aivhub.oauth.service.ChargePlanService;

import java.util.List;

@RestController
@RequestMapping("/api/planCharge")
public class ChargePlanController {
  private final ChargePlanService planChargePlanService;

  public ChargePlanController(ChargePlanService planChargePlanService) {
    this.planChargePlanService = planChargePlanService;
  }

  @GetMapping("/getAll")
  public List<ChargePlan> getAllChargePlans() {
    return planChargePlanService.findAll();
  }

  @PostMapping("/insert")
  public ChargePlan saveChargePlan(@RequestBody ChargePlan planChargePlan) {
    return planChargePlanService.addChargePlan(planChargePlan);
  }


  @PostMapping("/update")
  public ChargePlan update(@RequestBody ChargePlan planChargePlan) {
    return planChargePlanService.updateChargePlan(planChargePlan);
  }


  @DeleteMapping("/delete")
  public String delete(@RequestBody ChargePlan planChargePlan) {
    planChargePlanService.deleteChargePlan(planChargePlan);
    return "success";
  }
}
