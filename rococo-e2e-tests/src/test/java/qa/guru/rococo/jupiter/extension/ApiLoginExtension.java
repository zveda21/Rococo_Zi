package qa.guru.rococo.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;
import qa.guru.rococo.api.AuthApiClient;
import qa.guru.rococo.api.LoginAttemptException;
import qa.guru.rococo.api.ThreadSafeCookieStore;
import qa.guru.rococo.config.Config;
import qa.guru.rococo.jupiter.annotation.ApiLogin;
import qa.guru.rococo.jupiter.annotation.Token;
import qa.guru.rococo.model.rest.UserJson;

import java.util.Optional;

public class ApiLoginExtension implements BeforeTestExecutionCallback, ParameterResolver {

    private static final Config CONFIG = Config.getInstance();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);
    private static final AuthApiClient authApiClient = new AuthApiClient();

    private static final int LOGIN_MAX_RETRY = CONFIG.loginRetry();
    private static final long LOGIN_BACKOFF_MS = Config.getInstance().loginBackoffMs();

    private final boolean setupBrowser;

    private ApiLoginExtension(boolean setupBrowser) {
        this.setupBrowser = setupBrowser;
    }

    public ApiLoginExtension() {
        this.setupBrowser = true;
    }

    public static ApiLoginExtension rest() {
        return new ApiLoginExtension(false);
    }

    /**
     * If username and password are empty, use default values from environment variables ROCOCO_DEFAULT_USERNAME and ROCOCO_DEFAULT_PASSWORD
     */
    private static String[] getCredentials(ApiLogin annotation, UserJson testUser) {
        if (testUser != null) {
            if (!"".equals(annotation.username()) || !"".equals(annotation.password())) {
                throw new IllegalArgumentException("@User and @ApiLogin annotations cannot both provide login credentials");
            }
            return new String[]{
                    testUser.username(),
                    (UserExtension.defaultPassword)
            };
        }

        final String username = "".equals(annotation.username()) ? CONFIG.defaultUsername() : annotation.username();
        final String password = "".equals(annotation.password()) ? CONFIG.defaultPassword() : annotation.password();
        return new String[]{username, password};
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        Optional<ApiLogin> optional = AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class);

        if (optional.isEmpty()) {
            return;
        }

        ApiLogin apiLoginAnnotation = optional.get();

        // Validate
        String[] credentials = getCredentials(apiLoginAnnotation, UserExtension.getUserJson());
        final String username = credentials[0];
        final String password = credentials[1];
        System.out.println(getClass().getName() + " Test login using username and password " + username + ", " + password);

        final String token = getTokenWithRetry(username, password, new int[1]);
        setToken(token);

        if (setupBrowser) {
            Selenide.open(CONFIG.frontUrl());
            Selenide.localStorage().setItem("id_token", getToken());
            WebDriverRunner.getWebDriver().manage().addCookie(
                    getJsessionIdCookie()
            );
            // todo fix
            //Selenide.open(MainPage.URL, MainPage.class).checkThatPageLoaded();
        }
    }

    private String getTokenWithRetry(String username, String password, int[] retry) throws Exception {
        try {
            return authApiClient.login(username, password);
        } catch (LoginAttemptException e) {
            retry[0]++;
            if (retry[0] < LOGIN_MAX_RETRY) {
                Thread.sleep(LOGIN_BACKOFF_MS);
                return getTokenWithRetry(username, password, retry);
            } else {
                throw new Exception(e);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(String.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), Token.class);
    }

    @Override
    public String resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return "Bearer " + getToken();
    }

    public static void setToken(String token) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("token", token);
    }

    public static String getToken() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("token", String.class);
    }

    public static void setCode(String code) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("code", code);
    }

    public static String getCode() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("code", String.class);
    }

    public static Cookie getJsessionIdCookie() {
        return new Cookie(
                "JSESSIONID",
                ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID").orElseThrow()
        );
    }
}
