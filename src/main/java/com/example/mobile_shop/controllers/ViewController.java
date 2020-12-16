package com.example.mobile_shop.controllers;

import com.example.mobile_shop.services.PhoneService;
import com.example.mobile_shop.entities.Phone;
import com.example.mobile_shop.exceptions.EmptyInputException;
import com.example.mobile_shop.exceptions.NegativeInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;


@Controller
public class ViewController {
    private final PhoneService phoneService;

    @Autowired
    public ViewController(PhoneService phoneService){
        this.phoneService = phoneService;

    }

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("datetime",new Date());
        model.addAttribute("username","Pop Alexandru");

        return "index";

    }

    @RequestMapping("/new")
    public String newPhone(Model model){
        Phone phone = new Phone();
        model.addAttribute("phone",phone);
        model.addAttribute("datetime",new Date());
        model.addAttribute("username","Pop Alexandru");
        model.addAttribute("priceWarning", "invisible");
        return "new_phone";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addPhone(@ModelAttribute("phone") Phone phone) throws EmptyInputException, NegativeInputException {
        phoneService.add(phone);

        return "redirect:/";

    }

}
