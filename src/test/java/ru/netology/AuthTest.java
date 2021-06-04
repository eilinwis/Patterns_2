package ru.netology;

import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.getUser;
import static ru.netology.DataGenerator.generateLogin;
import static ru.netology.DataGenerator.generatePassword;

public class AuthTest {

    DataGenerator dataGenerator = new DataGenerator();

    @BeforeEach
    public void shouldOpenForm() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser(){
        val registeredUser = getRegisteredUser("active");
        SelenideElement form = $("[class=\"form form_size_m form_theme_alfa-on-white\"]");
        form.$("[data-test-id=\"login\"] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=\"password\"] input").setValue(registeredUser.getPassword());
        form.$("[data-test-id=\"action-login\"]").click();
        $(withText("Личный кабинет")).shouldBe(visible);
    }
    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        val notRegisteredUser = getUser("active");
        SelenideElement form = $("[class=\"form form_size_m form_theme_alfa-on-white\"]");
        form.$("[data-test-id=\"login\"] input").setValue(notRegisteredUser.getLogin());
        form.$("[data-test-id=\"password\"] input").setValue(notRegisteredUser.getPassword());
        form.$("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        val blockedUser = getRegisteredUser("blocked");
        SelenideElement form = $("[class=\"form form_size_m form_theme_alfa-on-white\"]");
        form.$("[data-test-id=\"login\"] input").setValue(blockedUser.getLogin());
        form.$("[data-test-id=\"password\"] input").setValue(blockedUser.getPassword());
        form.$("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        val registeredUser = getRegisteredUser("active");
        val wrongLogin = generateLogin();
        SelenideElement form = $("[class=\"form form_size_m form_theme_alfa-on-white\"]");
        form.$("[data-test-id=\"login\"] input").setValue(generateLogin());
        form.$("[data-test-id=\"password\"] input").setValue(registeredUser.getPassword());
        form.$("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        val registeredUser = getRegisteredUser("active");
        val wrongPassword = generatePassword();
        SelenideElement form = $("[class=\"form form_size_m form_theme_alfa-on-white\"]");
        form.$("[data-test-id=\"login\"] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=\"password\"] input").setValue(generatePassword());
        form.$("[data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}
