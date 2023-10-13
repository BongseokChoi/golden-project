package com.iandna.project.views.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainViewController {
  @GetMapping("")
  public String mainView() {
    return "main/index";
  }
}
