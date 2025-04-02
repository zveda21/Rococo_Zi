package qa.guru.rococo.test.web;

import com.codeborne.selenide.Selenide;
import lombok.SneakyThrows;
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

    @SneakyThrows
    @Test
    @User
    void mainPageShouldBeDisplayedAfterSuccessLogin() {

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        UserJson userJson = UserExtension.getUserJson();
        mainPage.goToLogin();
        new LoginPage().login(userJson.username(),"123");
        Thread.sleep(2000);
    }
}
