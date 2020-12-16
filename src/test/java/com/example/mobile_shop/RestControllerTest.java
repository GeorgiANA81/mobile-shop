package com.example.mobile_shop;

import com.example.mobile_shop.entities.Phone;
import com.example.mobile_shop.repositories.PhoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestControllerTest {
    @Autowired
    private PhoneRepository phonesRepository;

    @LocalServerPort
    private int port;

    private String serverUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    public void initServerURL() {
        phonesRepository.deleteAll();
        this.serverUrl = "http://localhost:" + port;
    }

    @Test
    public void whenGetAllPhonesWithEmptyDB_thenReturn200AndCorrectResponse() {
        ResponseEntity<List<Phone>> response = executePhoneRequest("/all", HttpMethod.GET);
        assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
        assertEquals(0, response.getBody().size());

    }


    @Test
    public void whenGetAllPhones_thenReturn200AndCorrectResponse() {
        List<Phone> phones = Arrays.asList(new Phone("Motor electric", "Ford", 500.25),
                new Phone("Motor Diesel", "Bosch", 550.35),
                new Phone("Motor Otto", "General Motors", 450));
        phonesRepository.saveAll(phones);

        ResponseEntity<List<Phone>> response = executePhoneRequest("/all", HttpMethod.GET);
        assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
        List<Phone> responsePhonesList = response.getBody();
        assertTrue((responsePhonesList.containsAll(phones) && phones.containsAll(responsePhonesList)));

    }

    @Test
    public void whenGetFilterPhonesWithPriceLessThanAll_thenReturn200And0Elements() {
        List<Phone> phones = Arrays.asList(new Phone("Motor electric", "Ford", 500.25),
                new Phone("Motor Diesel", "Bosch", 550.35),
                new Phone("Motor Otto", "General Motors", 450));
        phonesRepository.saveAll(phones);

        ResponseEntity<List<Phone>> response = executePhoneRequest("/lessThan/200", HttpMethod.GET);
        assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
        List<Phone> responsePhonesList = response.getBody();
        assertEquals(0,responsePhonesList.size());

    }

    @Test
    public void whenGetFilterPhonesWithPriceInDataBase_thenReturn200AndCorrectResponseBody() {
        List<Phone> phones = Arrays.asList(new Phone("Motor electric", "Ford", 500.25),
                new Phone("Motor Diesel", "Bosch", 550.35),
                new Phone("Motor Otto", "General Motors", 450));
        phonesRepository.saveAll(phones);

        ResponseEntity<List<Phone>> response = executePhoneRequest("/lessThan/500.25", HttpMethod.GET);
        assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
        List<Phone> responsePhonesList = response.getBody();

        List<Phone> expectedList = new ArrayList<>();
        expectedList.add(phones.get(0));
        expectedList.add(phones.get(2));
        assertTrue((responsePhonesList.containsAll(expectedList) && expectedList.containsAll(responsePhonesList)));


    }

    @Test
    public void whenGetFilterPhonesWithPriceBigThanAll_thenReturn200AndCorrectResponseBody() {
        List<Phone> phones = Arrays.asList(new Phone("Motor electric", "Ford", 500.25),
                new Phone("Motor Diesel", "Bosch", 550.35),
                new Phone("Motor Otto", "General Motors", 450));
        phonesRepository.saveAll(phones);

        ResponseEntity<List<Phone>> response = executePhoneRequest("/lessThan/700", HttpMethod.GET);
        assertEquals(HttpStatus.valueOf(200), response.getStatusCode());

        List<Phone> responsePhonesList = response.getBody();
        assertTrue((responsePhonesList.containsAll(phones) && phones.containsAll(responsePhonesList)));


    }

    @Test
    public void whenGetFilterPhonesWithNegativePrice_then400(){
        HttpClientErrorException response = assertThrows(HttpClientErrorException.class, () -> executePhoneRequest("/lessThan/-500", HttpMethod.GET));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void whenGetFilterPhonesWithStringPrice_then400(){
        HttpClientErrorException response = assertThrows(HttpClientErrorException.class, () -> executePhoneRequest("/lessThan/aaa", HttpMethod.GET));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void whenGetFilterPhonesWithoutPathVariable_thenReturn404() {
        HttpClientErrorException response = assertThrows(HttpClientErrorException.class, () -> executePhoneRequest("/lessThan/", HttpMethod.GET));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void whenRemovePhoneWithIdInDB_thenReturn202AndCorrectResponseBody() {
        List<Phone> phones = Arrays.asList(new Phone("Motor electric", "Ford", 500),
                new Phone("Motor Diesel", "Bosch", 550),
                new Phone("Motor Otto", "General Motors", 450));
        phonesRepository.saveAll(phones);

        ResponseEntity<List<Phone>> response = executePhoneRequest("/remove/" + phones.get(0).getId(), HttpMethod.DELETE);
        List<Phone> expectedList = new ArrayList<>();
        expectedList.add(phones.get(1));
        expectedList.add(phones.get(2));

        List<Phone> responsePhonesList = response.getBody();
        assertTrue((responsePhonesList.containsAll(expectedList) && expectedList.containsAll(responsePhonesList)));

    }

    @Test
    public void whenRemovePhoneWithoutPathVariable_thenReturn404(){
        HttpClientErrorException response = assertThrows(HttpClientErrorException.class, () -> executePhoneRequest("/remove", HttpMethod.DELETE));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void whenRemovePhoneWithIdNotInDB_then404(){
        List<Phone> phones = Arrays.asList(new Phone("Motor electric", "Ford", 500),
                new Phone("Motor Diesel", "Bosch", 550),
                new Phone("Motor Otto", "General Motors", 450));
        phonesRepository.saveAll(phones);

        HttpClientErrorException response = assertThrows(HttpClientErrorException.class, () -> executePhoneRequest("/remove/500", HttpMethod.DELETE));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void whenRemovePhoneWithIdInDBGetMethod_then405(){
        List<Phone> phones = Arrays.asList(new Phone("Motor electric", "Ford", 500),
                new Phone("Motor Diesel", "Bosch", 550),
                new Phone("Motor Otto", "General Motors", 450));
        phonesRepository.saveAll(phones);

        HttpClientErrorException response = assertThrows(HttpClientErrorException.class, () -> executePhoneRequest("/remove/"+phones.get(0).getId(), HttpMethod.GET));
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());

    }

    private ResponseEntity<List<Phone>> executePhoneRequest(String url, HttpMethod method) {
        return restTemplate.exchange(serverUrl + url, method, null, new ParameterizedTypeReference<List<Phone>>() {
        });

    }

}
