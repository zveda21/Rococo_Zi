package qa.guru.rococo.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.rococo.jupiter.annotation.meta.WebTest;
import qa.guru.rococo.jupiter.extension.TestMethodContextExtension;
import qa.guru.rococo.page.MainPage;
import qa.guru.rococo.page.RegisterPage;

import static qa.guru.rococo.utils.RandomDataUtils.randomUsername;

@WebTest
@ExtendWith(TestMethodContextExtension.class)
public class RegisterTest {

    private static final String PASSWORD = "123";

    @Test
    @DisplayName("Check success register")
    void checkSuccessRegistration() {
        String newUsername = randomUsername();
        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class)
                .goToLogin();
        mainPage.goToRegisterPage();
        new RegisterPage().register(newUsername, PASSWORD)
                .checkSuccessMessage("Добро пожаловать в Ro");
    }

    @Test
    @DisplayName("Check should not register user with existing username")
    void checkShouldNotRegisterUserWithExistingUsername() {
        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class)
                .goToLogin();
        mainPage.goToRegisterPage();
        String existingUserName = "admin";
        new RegisterPage().register(existingUserName, PASSWORD)
                .checkError(String.format("Имя пользователя `%s` уже существует", existingUserName));
    }

    @Test
    @DisplayName("Check error when passwords not match")
    void checkPasswordsNotMatch() {
        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class)
                .goToLogin();
        mainPage.goToRegisterPage();
        String userName = randomUsername();
        new RegisterPage().fillUsername(userName)
                .fillPassword(PASSWORD)
                .fillSubmitPassword("Test" + PASSWORD)
                .clickOnRegisterButton()
                .checkError("Passwords should be equal");
    }
}
