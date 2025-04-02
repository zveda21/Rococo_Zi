package qa.guru.rococo.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl();
    private final SelenideElement navigationElement = $("#shell-header");
    private final SelenideElement loginButton = navigationElement.find(".btn");
    private final SelenideElement avatarIcon = $("button.btn-icon");
    private final SelenideElement registerButton = $("a[href='/register']");

    @Step("Go to Login page")
    @Nonnull
    public MainPage goToLogin() {
        loginButton.click();
        return this;
    }

    @Step("Check if avatar icon is visible")
    public MainPage checkIfAvatarIconIsVisible() {
        avatarIcon.shouldBe(visible);
        return this;
    }

    @Step("Go to Register page")
    @Nonnull
    public MainPage goToRegisterPage() {
        registerButton.click();
        return this;
    }

    @Override
    public MainPage checkThatPageLoaded() {
        return null;
    }
}
