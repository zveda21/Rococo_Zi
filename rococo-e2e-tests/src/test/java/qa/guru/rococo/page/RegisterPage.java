package qa.guru.rococo.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage {

    public static final String URL = CFG.authUrl() + "register";
    private final SelenideElement registerForm = $("#register-form");
    private final SelenideElement usernameInput = registerForm.find("#username");
    private final SelenideElement passwordInput = registerForm.find("#password");
    private final SelenideElement submitPasswordInput = registerForm.find("#passwordSubmit");
    private final SelenideElement registerButton = registerForm.find("button.form__submit");
    private final SelenideElement errorMessage = registerForm.find(".form__error");
    private final SelenideElement successRegisterMessage = $(".form__subheader");

    @Step("Register Rococo app with ${username}")
    public RegisterPage register(String username, String password) {
        fillUsername(username);
        fillPassword(password);
        fillSubmitPassword(password);
        clickOnRegisterButton();
        return this;
    }

    @Step("Fill username filed")
    public RegisterPage fillUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Fill password filed")
    public RegisterPage fillPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Fill submit password filed")
    public RegisterPage fillSubmitPassword(String password) {
        submitPasswordInput.setValue(password);
        return this;
    }


    @Step("Click on Register button")
    public RegisterPage clickOnRegisterButton() {
        registerButton.click();
        return this;
    }

    @Step("Check error on page: {error}")
    @Nonnull
    public RegisterPage checkError(String error) {
        errorMessage.shouldHave(text(error));
        return this;
    }

    @Step("Check success message  on page: {message}")
    @Nonnull
    public RegisterPage checkSuccessMessage(String message) {
        successRegisterMessage.shouldHave(text(message));
        return this;
    }

    @Override
    public BasePage<?> checkThatPageLoaded() {
        return null;
    }
}
