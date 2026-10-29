package api;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.SupabaseApi;
import utils.TestUser;

public class AuthApiTest {

    @Test
    public void adminCanSignIn() {
        String token = SupabaseApi.signIn(TestUser.ADMIN);

        Assert.assertFalse(token == null || token.isBlank(),
                "Access token should not be empty");
    }

}
