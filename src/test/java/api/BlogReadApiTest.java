package api;

import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.SupabaseApi;
import utils.SupabaseSpecs;
import utils.TestUser;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class BlogReadApiTest {
    @Test
    public void adminReadsOwnDraftWithExpectedFields () {
        String adminToken = SupabaseApi.signIn(TestUser.ADMIN);
        String title = "Autotest draft " + System.currentTimeMillis();
        String id = SupabaseApi.createDraft(adminToken, title);
        try {
            given().spec(SupabaseSpecs.forUser(adminToken))
            .when()
                    .get("/rest/v1/blog_posts?id=eq." + id + "&select=title, published")
            .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("size()", is(1))
                    .body("[0].title", equalTo(title))
                    .body("[0].published", is(false));
        }
        finally {
            SupabaseApi.deleteBlogPost(adminToken, id);
        }
    }

    private static int visibleRows(RequestSpecification spec, String id) {
        return given().spec(spec)
                .when()
                    .get("/rest/v1/blog_posts?id=eq." + id + "&select=id")
                .then()
                    .statusCode(200)
                    .log().ifValidationFails()
                    .extract().jsonPath().getList("id").size();
    }

    @Test
    public void draftIsVisibleToAdminButHiddenFromAnonymous() {
        String adminToken = SupabaseApi.signIn(TestUser.ADMIN);
        String title = "Autotest draft " + System.currentTimeMillis();
        String id = SupabaseApi.createDraft(adminToken, title);
        try {
            int adminSees = visibleRows(SupabaseSpecs.forUser(adminToken), id);
            int anonSees = visibleRows(SupabaseSpecs.anon(), id);
            Assert.assertEquals(adminSees, 1, "Admin should see own draft");
            Assert.assertEquals(anonSees, 0, "Anonymous must not see a draft");
        }
        finally {
            SupabaseApi.deleteBlogPost(adminToken, id);
        }
    }
}
