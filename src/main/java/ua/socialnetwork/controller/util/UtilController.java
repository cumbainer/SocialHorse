package ua.socialnetwork.controller.util;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/util")
@Controller
public class UtilController {

    @GetMapping("/more")
    public String getMoreInfo(){

        return "/util/more";
    }
}
