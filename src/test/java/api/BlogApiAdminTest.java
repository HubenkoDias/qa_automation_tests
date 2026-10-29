package api;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SupabaseApi;
import utils.TestUser;

import java.util.List;

public class BlogApiAdminTest {
    private String adminToken;

    @BeforeClass
    public void SignInOnceAsAdmin() {
        adminToken = SupabaseApi.signIn(TestUser.ADMIN);
    }

    @Test
    public void adminSeesOwnDraft () {
        String title = "Autotest draft " + System.currentTimeMillis();
        String draftId = SupabaseApi.createDraft(adminToken, title);
        try {
            List<String> adminSees = SupabaseApi.readBlogPostIds(adminToken, false);
            Assert.assertTrue(adminSees.contains(draftId), "Admin should see own draft");
        }
        finally {
            SupabaseApi.deleteBlogPost(adminToken, draftId);
        }
    }

    }

