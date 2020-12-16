package com.example.mobile_shop.controllers;

import com.example.mobile_shop.exceptions.ResourceNotFoundDeleteException;
import com.example.mobile_shop.services.PhoneService;
import com.example.mobile_shop.entities.Phone;
import com.example.mobile_shop.exceptions.NegativeInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PhoneController {
    private final PhoneService phoneService;

    @Autowired
    public PhoneController(PhoneService phoneService){
        this.phoneService = phoneService;

    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Phone> listAllphones(){
        return phoneService.listAll();

    }

    @RequestMapping(value = "/lessThan/{price}", method = RequestMethod.GET)
    public List<Phone> listFilterphones(@PathVariable Double price) throws NegativeInputException {
        return phoneService.listFilter(price);

    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    public List<Phone> removephone(@PathVariable long id) throws ResourceNotFoundDeleteException {
        phoneService.remove(id);

        return phoneService.listAll();

    }

}
