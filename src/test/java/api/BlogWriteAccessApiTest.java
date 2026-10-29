package api;

import io.restassured.response.Response;
import model.NewBlogPost;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.SupabaseApi;
import utils.SupabaseSpecs;
import utils.TestUser;

import static io.restassured.RestAssured.given;

public class BlogWriteAccessApiTest {

    @Test
    public void anonymousCannotCreatePost() {
        Response response = given().spec(SupabaseSpecs.anon())
                .header("Prefer", "return=representation")
                .body(NewBlogPost.draft("Autotest draft " + System.currentTimeMillis()))
            .when()
                .post("/rest/v1/blog_posts");

        try {
            int status = response.statusCode();
            Assert.assertTrue(status == 401 || status == 403,
                    "Anonymous must be denied, but status was " + status);
            Assert.assertEquals(response.jsonPath().getString("code"), "42501",
                    "Denial should come from row-level security");
        }
        finally {
            if (response.statusCode() == 201) {
                String id = response.jsonPath().getString("[0].id");
                SupabaseApi.deleteBlogPost(SupabaseApi.signIn(TestUser.ADMIN), id);
            }
        }
    }
}
