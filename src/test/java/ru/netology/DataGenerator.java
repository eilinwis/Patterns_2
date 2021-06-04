package ru.netology;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    public DataGenerator() {
    }

    private static void sendRequest(RegistrationDto user) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(new Gson().toJson(user)) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String generateLogin() {
        Faker faker = new Faker(new Locale("en"));
        String login = faker.name().username();
        return login;
    }

    public static String generatePassword() {
        Faker faker = new Faker(new Locale("en"));
        String password = faker.internet().password();
        return password;
    }

        public static class Registration {
            private Registration() {
            }

            public static RegistrationDto getUser(String status) {
                return new RegistrationDto (generateLogin(), generatePassword(), status);
            }

            public static RegistrationDto getRegisteredUser(String status) {
                RegistrationDto registeredUser = getUser(status);
                sendRequest(registeredUser);
                return registeredUser;
            }
        }

    @Value
    @RequiredArgsConstructor
public static class RegistrationDto  {
        String login;
        String password;
        String status;
}
}
