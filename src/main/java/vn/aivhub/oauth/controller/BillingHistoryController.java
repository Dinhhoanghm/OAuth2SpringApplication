package vn.aivhub.oauth.controller;

import org.springframework.web.bind.annotation.*;
import vn.aivhub.data.tables.pojos.BillingHistory;
import vn.aivhub.oauth.service.BillingHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/billingHistory")
public class BillingHistoryController {
  private final BillingHistoryService planBillingHistoryService;

  public BillingHistoryController(BillingHistoryService planBillingHistoryService) {
    this.planBillingHistoryService = planBillingHistoryService;
  }

  @GetMapping("/getAll")
  public List<BillingHistory> getAllBillingHistory() {
    return planBillingHistoryService.findAll();
  }
  @PostMapping("/insert")
  public BillingHistory saveBillingHistory(@RequestBody BillingHistory planBillingHistory) {
    return planBillingHistoryService.addBillingHistory(planBillingHistory);
  }


  @PostMapping("/update")
  public BillingHistory update(@RequestBody BillingHistory planBillingHistory) {
    return planBillingHistoryService.updateBillingHistory(planBillingHistory);
  }


  @DeleteMapping("/delete")
  public String delete(@RequestBody BillingHistory planBillingHistory) {
    planBillingHistoryService.deleteBillingHistory(planBillingHistory);
    return "success";
  }
}
