package qa.guru.rococo.jupiter.extension;

import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.rococo.api.AuthApiClient;
import qa.guru.rococo.jupiter.annotation.User;
import qa.guru.rococo.model.rest.UserJson;
import qa.guru.rococo.utils.RandomDataUtils;

import java.util.Optional;

public class UserExtension implements BeforeTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    public static final String defaultPassword = "123";
    private static final AuthApiClient authApiClient = new AuthApiClient();

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        Optional<User> optional = AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class);

        if (optional.isEmpty()) {
            return;
        }

        User userAnnotation = optional.get();
        final String username = "".equals(userAnnotation.username()) ?
                RandomDataUtils.randomUsername() :
                userAnnotation.username();
        authApiClient.register(username, defaultPassword);
        final UserJson testUser = new UserJson(null, username, defaultPassword, null, null);
        setUser(testUser);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(UserJson.class) &&
                parameterContext.isAnnotated(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getUserJson();
    }

    public static void setUser(UserJson testUser) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                testUser
        );
    }

    public static UserJson getUserJson() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class);
    }
}
