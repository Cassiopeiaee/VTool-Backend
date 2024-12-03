package com.VTool;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AngularController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        // Leitet alle nicht-API-Anfragen auf die index.html weiter
        return "forward:/index.html";
    }
}
