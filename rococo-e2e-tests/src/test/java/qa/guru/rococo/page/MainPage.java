package qa.guru.rococo.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage>{

    public static final String URL = CFG.frontUrl();
    private SelenideElement navigationElement = $("#shell-header");
    private SelenideElement loginButton = navigationElement.find(".btn");
    // a[href="/painting"]


    @Step("Go to login page")
    @Nonnull
    public MainPage goToLogin() {
        loginButton.click();
        return this;
    }

    @Override
    public MainPage checkThatPageLoaded() {
        return null;
    }
}
