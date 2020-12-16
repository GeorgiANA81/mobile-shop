package com.example.mobile_shop.services;

import com.example.mobile_shop.exceptions.ResourceNotFoundDeleteException;
import com.example.mobile_shop.repositories.PhoneRepository;
import com.example.mobile_shop.entities.Phone;
import com.example.mobile_shop.exceptions.EmptyInputException;
import com.example.mobile_shop.exceptions.NegativeInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneService {
    private final PhoneRepository phonesRepository;

    @Autowired
    public  PhoneService(PhoneRepository phonesRepository){
        this.phonesRepository = phonesRepository;

    }

    public List<Phone> listAll(){
        return phonesRepository.findAll();

    }

    public List<Phone> listFilter(Double price) throws NegativeInputException {
        if (price <= 0) {
            throw new NegativeInputException("Price can not be negative or zero!");
        }

        return phonesRepository.findByPriceIsLessThanEqual(price);

    }

    public Phone add(Phone entity) throws NegativeInputException, EmptyInputException {
        if(entity.getPhoneName().matches("^( )*$")){
            throw new EmptyInputException("phone input is empty!");

        }

        if(entity.getBrand().matches("^( )*$")){
            throw new EmptyInputException("Brand input is empty!");

        }

        if(entity.getPrice() == null){
            throw new NegativeInputException("Price input is empty!");

        }

        if (entity.getPrice() <= 0) {
            throw new NegativeInputException("Price can not be negative or zero!");

        }

        return phonesRepository.save(entity);

    }


    public void remove(Long id) throws ResourceNotFoundDeleteException {
        if(!phonesRepository.findById(id).isPresent()){
            throw new ResourceNotFoundDeleteException("Resource not found for {" + id + "}!");

        }
        
        phonesRepository.deleteById(id);

    }

}
