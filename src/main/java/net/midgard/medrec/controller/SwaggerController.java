package net.midgard.medrec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author thor
 */
@Controller
@RequestMapping(value = "/")
public class SwaggerController {

  @RequestMapping(value = "/")
  public String index() {
    return "redirect:swagger-ui.html";
  }
}
