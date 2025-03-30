package qa.guru.rococo.api;


import qa.guru.rococo.model.rest.UserJson;

import java.util.List;

public interface UsersClient {

    List<UserJson> all();

    UserJson createUser(String username, String password);

    void addIncomeInvitation(UserJson targetUser, int count);

    void addOutcomeInvitation(UserJson targetUser, int count);

    void addFriend(UserJson targetUser, int count);
}
