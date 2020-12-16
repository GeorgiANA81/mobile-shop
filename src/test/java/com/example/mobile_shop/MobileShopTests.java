package com.example.mobile_shop;

import com.example.mobile_shop.controllers.PhoneController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MobileShopTests {
    @Autowired
    private PhoneController controller;


    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();

    }


}
