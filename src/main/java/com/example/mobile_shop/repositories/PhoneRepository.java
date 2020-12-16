package com.example.mobile_shop.repositories;

import com.example.mobile_shop.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    List<Phone> findByPriceIsLessThanEqual(double price);


}
