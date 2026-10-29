package api;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SupabaseApi;
import utils.TestConfig;
import utils.TestUser;

public class BlogApiUnauthorizedTest {

    @Test
    public void unauthorizedUserCreateDraft () {
        String title = "Autotest draft " + System.currentTimeMillis();
        SupabaseApi.tryCreateDraft(SupabaseApi.anonKey(), title);
    }
}
