package com.example.mobile_shop;

import com.example.mobile_shop.entities.Phone;
import com.example.mobile_shop.repositories.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSeeder  implements CommandLineRunner {
    private final PhoneRepository phonesRepository;

    @Autowired
    public DatabaseSeeder(PhoneRepository phonesRepository){
        this.phonesRepository = phonesRepository;

    }

    @Override
    public void run(String... strings) throws Exception {
        List<Phone> phones = new ArrayList<>();

        phones.add(new Phone("Galaxy A101","Samsung",113.99));
        phones.add(new Phone("Galaxy S20","Samsung",699.99));
        phones.add(new Phone("Poco X3 NFC","Xiaomi",275.76));
        phones.add(new Phone("IPhone 12 Pro Max","Apple",1399.60));

        phonesRepository.saveAll(phones);
    }

}
