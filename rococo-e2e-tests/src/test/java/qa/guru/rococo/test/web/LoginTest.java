package qa.guru.rococo.test.web;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import qa.guru.rococo.jupiter.annotation.User;
import qa.guru.rococo.jupiter.annotation.meta.WebTest;
import qa.guru.rococo.jupiter.extension.ApiLoginExtension;
import qa.guru.rococo.jupiter.extension.BrowserExtension;
import qa.guru.rococo.jupiter.extension.TestMethodContextExtension;
import qa.guru.rococo.jupiter.extension.UserExtension;
import qa.guru.rococo.model.rest.UserJson;
import qa.guru.rococo.page.MainPage;
import qa.guru.rococo.utils.SelenideUtils;

@WebTest
@ExtendWith(TestMethodContextExtension.class)
public class LoginTest {

    @RegisterExtension
    private final BrowserExtension browserExtension = new BrowserExtension();
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();
    private final SelenideDriver driver = new SelenideDriver(SelenideUtils.chromeConfig);
    private static final UserExtension userExtension = new UserExtension();

    @Test
    @User
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson userJson) {

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);

        mainPage.goToLogin();
    }
}
