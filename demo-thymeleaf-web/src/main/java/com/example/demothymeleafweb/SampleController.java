package com.example.demothymeleafweb;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.servlet.http.PushBuilder;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class SampleController {

    @NonNull MessageSource messageSource;

    @GetMapping("/foo")
    @ResponseBody
    public String foo() {
        return "foo";
    }

    @GetMapping("/bar")
    @ResponseBody
    public String bar() {
        return "bar";
    }

    @GetMapping("/redirect")
    public String redirect(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("name", "keesun");
        return "redirect:/receive";
    }

    @GetMapping("/receive")
    @ResponseBody
    public String receive(Model model, HttpSession httpSession) {
        Object name = httpSession.getAttribute("name");
        System.out.println(name);
        return name.toString();
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Page Title");
        model.addAttribute("username", "Whiteship");
        model.addAttribute("now", LocalDateTime.now());
        return "index";
    }

    @RequestMapping("/push")
    public String push(Model model, PushBuilder pushBuilder) {
        model.addAttribute("title", "Page Title");
        model.addAttribute("username", "Whiteship");
        model.addAttribute("now", LocalDateTime.now());
        if (pushBuilder != null) {
            pushBuilder.path("/webjars/jquery/3.3.1/dist/jquery.min.js").push();
        }
        return "index";
    }

    @RequestMapping("/no-push")
    public String push(Model model) {
        model.addAttribute("title", "Page Title");
        model.addAttribute("username", "Whiteship");
        model.addAttribute("now", LocalDateTime.now());
        return "index";
    }

    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "My message is <b>hm..</b>");
        return "hello";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/s")
    @ResponseBody
    public String person(@RequestParam String q) {
        return q;
    }

    @PostMapping("/json")
    @ResponseBody
    public Person json(@Valid @RequestBody Person person, BindingResult bindingResult) {
        System.out.println(bindingResult);
        return person;
    }

}
