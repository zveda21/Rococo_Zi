package qa.guru.rococo.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import qa.guru.rococo.jupiter.annotation.User;
import qa.guru.rococo.jupiter.annotation.meta.WebTest;
import qa.guru.rococo.jupiter.extension.TestMethodContextExtension;
import qa.guru.rococo.jupiter.extension.UserExtension;
import qa.guru.rococo.model.rest.UserJson;
import qa.guru.rococo.page.LoginPage;
import qa.guru.rococo.page.MainPage;

@WebTest
@ExtendWith(TestMethodContextExtension.class)
public class LoginTest {

    @RegisterExtension
    private static final UserExtension userExtension = new UserExtension();
    private static final String PASSWORD = "123";

    @DisplayName("Check success login")
    @Test
    @User
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        UserJson userJson = UserExtension.getUserJson();
        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.goToLogin();
        new LoginPage().login(userJson.username(), PASSWORD);
        new MainPage().checkIfAvatarIconIsVisible();
    }

    @DisplayName("Check login functionality with bad credentials")
    @Test
    @User
    void checkLoginFunctionalityWithBadCredentials() {
        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        UserJson userJson = UserExtension.getUserJson();
        mainPage.goToLogin();
        new LoginPage().login(userJson.username(), "PASSWORD")
                .checkError("Bad credentials");

    }
}
