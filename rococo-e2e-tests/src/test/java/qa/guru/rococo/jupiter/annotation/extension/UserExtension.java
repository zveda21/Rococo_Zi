package qa.guru.rococo.jupiter.annotation.extension;

import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.rococo.api.UsersClient;
import qa.guru.rococo.jupiter.annotation.User;
import qa.guru.rococo.model.rest.UserJson;
import qa.guru.rococo.utils.RandomDataUtils;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final String defaultPassword = "12345";

    private final UsersClient usersClient = null; // todo fix new UsersApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if ("".equals(userAnno.username())) {
                        final String username = RandomDataUtils.randomUsername();
                        UserJson testUser = usersClient.createUser(username, defaultPassword);

                        // TODO not needed. replace with useful data
//            usersClient.addIncomeInvitation(testUser, userAnno.incomeInvitations());
//            usersClient.addOutcomeInvitation(testUser, userAnno.outcomeInvitations());
//            usersClient.addFriend(testUser, userAnno.friends());
                        setUser(testUser);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
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
