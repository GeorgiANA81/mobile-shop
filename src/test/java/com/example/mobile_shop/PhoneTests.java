package com.example.mobile_shop;

import com.example.mobile_shop.entities.Phone;
import com.example.mobile_shop.exceptions.EmptyInputException;
import com.example.mobile_shop.exceptions.ResourceNotFoundDeleteException;
import com.example.mobile_shop.exceptions.NegativeInputException;
import com.example.mobile_shop.repositories.PhoneRepository;
import com.example.mobile_shop.services.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PhoneTests {
    @Autowired
    private PhoneRepository phonesRepository;

    @Autowired
    private PhoneService phoneService ;

    @BeforeEach
    public void initDataBase(){
        phonesRepository.deleteAll();

    }

    @Test
    public void testListAllPhonesWhenDBIsEmpty(){
        List<Phone> phones = phoneService.listAll();
        assertEquals(0,phones.size());

    }

    @Test
    public void testListAllPhonesWhenDBIsNotEmpty(){
        List<Phone> phones = new ArrayList<>();

        phones.add(new Phone("Galaxy A101","Samsung",113.99));
        phones.add(new Phone("Galaxy S20","Samsung",699.99));
        phones.add(new Phone("Poco X3 NFC","Xiaomi",275.76));
        phones.add(new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        List<Phone> phonesList = phoneService.listAll();
        assertTrue((phones.containsAll(phonesList) && phonesList.containsAll(phones)));

    }


    @Test
    public void testAddPhoneWhenDBIsEmpty() throws NegativeInputException, EmptyInputException {
        Phone phone = new Phone("IPhone X","Apple",400.45);
        Phone addedPhone;
        addedPhone = phoneService.add(phone);
        List<Phone> phones = phoneService.listAll();
        assertNotNull(addedPhone);
        assertEquals(1,phones.size());
        assertEquals(addedPhone, phones.get(0));

    }

    @Test
    public void testAddPhoneWhenDBIsNotEmpty() throws NegativeInputException, EmptyInputException {
        List<Phone> phones = new ArrayList<>();

        phones.add(new Phone("Galaxy A101","Samsung",113.99));
        phones.add(new Phone("Galaxy S20","Samsung",699.99));
        phones.add(new Phone("Poco X3 NFC","Xiaomi",275.76));
        phones.add(new Phone("IPhone 12 Pro Max","Apple",1399.60));
        phonesRepository.saveAll(phones);

        Phone phone = new Phone("IPhone X","Apple",400.45);
        phones.add(phone);
        phoneService.add(phone);
        List<Phone> phonesList = phoneService.listAll();
        assertTrue((phones.containsAll(phonesList) && phonesList.containsAll(phones)));

    }


    @Test
    public void testAddPhoneWithNegativePrice(){
        Phone phone = new Phone("J7","Samsung",-700.0);
        assertThrows(NegativeInputException.class,() -> phoneService.add(phone));

    }

    @Test
    public void testAddPhoneWithEmptyName(){
        Phone phone = new Phone("","Huawei",-179.0);
        assertThrows(EmptyInputException.class,() -> phoneService.add(phone));

    }

    @Test
    public void testAddPhoneWithEmptyBrand(){
        Phone phone = new Phone("Mate 20 Pro","       ",-350.0);
        assertThrows(EmptyInputException.class,() -> phoneService.add(phone));

    }


    @Test
    public void testListFilterWhenPriceIsLessThanAll() throws NegativeInputException {
        List<Phone> phones = new ArrayList<>();

        phones.add(new Phone("Galaxy A101","Samsung",113.99));
        phones.add(new Phone("Galaxy S20","Samsung",699.99));
        phones.add(new Phone("Poco X3 NFC","Xiaomi",275.76));
        phones.add(new Phone("IPhone 12 Pro Max","Apple",1399.60));

        phonesRepository.saveAll(phones);

        double price = 100;
        List<Phone> phonesList;
        phonesList = phoneService.listFilter(price);

        assertEquals(0,phonesList.size());

    }

    @Test
    public void testListFilterWhenPriceIsBigThanAll() throws NegativeInputException {
        List<Phone> phones = new ArrayList<>();

        phones.add(new Phone("Galaxy A101","Samsung",113.99));
        phones.add(new Phone("Galaxy S20","Samsung",699.99));
        phones.add(new Phone("Poco X3 NFC","Xiaomi",275.76));
        phones.add(new Phone("IPhone 12 Pro Max","Apple",1399.60));

        phonesRepository.saveAll(phones);

        double price = 1500;
        List<Phone> phonesList;
        phonesList = phoneService.listFilter(price);

        assertTrue((phones.containsAll(phonesList) && phonesList.containsAll(phones)));

    }

    @Test
    public void testListFilterWhenPriceIsInDataBase() throws NegativeInputException {
        List<Phone> phones = new ArrayList<>();

        phones.add(new Phone("Galaxy A101","Samsung",113.99));
        phones.add(new Phone("Galaxy S20","Samsung",699.99));
        phones.add(new Phone("Poco X3 NFC","Xiaomi",275.76));
        phones.add(new Phone("IPhone 12 Pro Max","Apple",1399.60));

        phonesRepository.saveAll(phones);

        double price = 1200;
        List<Phone> phonesList;
        phonesList = phoneService.listFilter(price);

        phones.remove(3);
        assertTrue((phones.containsAll(phonesList) && phonesList.containsAll(phones)));

    }

    @Test
    public void testListFilterWithNegativePrice(){
        assertThrows(NegativeInputException.class,() -> phoneService.listFilter(-438.0));

    }

    @Test
    public void testRemovePhoneWhenIdExist() throws ResourceNotFoundDeleteException {
        List<Phone> phones = new ArrayList<>();

        phones.add(new Phone("Galaxy A101","Samsung",113.99));
        phones.add(new Phone("Galaxy S20","Samsung",699.99));
        phones.add(new Phone("Poco X3 NFC","Xiaomi",275.76));
        phones.add(new Phone("IPhone 12 Pro Max","Apple",1399.60));

        phonesRepository.saveAll(phones);

        Long id = phones.get(0).getId();
        phoneService.remove(id);
        phones.remove(0);

        List<Phone> phonesList = phonesRepository.findAll();

        assertTrue((phones.containsAll(phonesList) && phonesList.containsAll(phones)));

    }

    @Test
    public void testRemovePhoneWhenIdNotExist(){
        List<Phone> phones = new ArrayList<>();

        phones.add(new Phone("Galaxy A101","Samsung",113.99));
        phones.add(new Phone("Galaxy S20","Samsung",699.99));
        phones.add(new Phone("Poco X3 NFC","Xiaomi",275.76));
        phones.add(new Phone("IPhone 12 Pro Max","Apple",1399.60));

        phonesRepository.saveAll(phones);

        Long id = 90L;
        assertThrows(ResourceNotFoundDeleteException.class, () -> {phoneService.remove(id);});

    }

}

