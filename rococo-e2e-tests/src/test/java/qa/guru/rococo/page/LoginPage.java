package qa.guru.rococo.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    public static final String URL = CFG.authUrl();

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement loginButton = $("button.form__submit");
    private final SelenideElement errorForm = $("p.form__error");

    @Override
    public LoginPage checkThatPageLoaded() {
        return null;
    }

    @Step("Login Rococo app with ${username}")
    public LoginPage login(String username, String password) {
        fillUsername(username);
        fillPassword(password);
        clickOnLoginButton();
        return this;
    }

    @Step("Fill username filed")
    public LoginPage fillUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Fill password filed")
    public LoginPage fillPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Click on Login button")
    public LoginPage clickOnLoginButton() {
        loginButton.click();
        return this;
    }

    @Step("Check error on page: {error}")
    @Nonnull
    public LoginPage checkError(String error) {
        errorForm.shouldHave(text(error));
        return this;
    }

}
