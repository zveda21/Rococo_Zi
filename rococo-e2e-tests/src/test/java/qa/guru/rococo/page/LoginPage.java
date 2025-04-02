package qa.guru.rococo.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage>{

    private SelenideElement usernameInput = $("input[name='username']");
    private SelenideElement passwordInput = $("input[name='password']");
    private SelenideElement loginButton = $("button.form__submit");


    @Override
    public LoginPage checkThatPageLoaded() {
        return null;
    }

    @Step("Login Rococo app with ${username}")
    public LoginPage login(String username, String password) throws InterruptedException {
        usernameInput.setValue(username);
        Thread.sleep(3000);
        passwordInput.setValue(password);
        loginButton.click();
        return this;
    }

}
