package api;

import io.restassured.response.Response;
import model.NewBlogPost;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.Actor;
import utils.SupabaseApi;
import utils.SupabaseSpecs;

import static io.restassured.RestAssured.given;

public class BlogWriteAccessApiTest {

    @DataProvider(name = "deniedActors")
    public Object[][] deniedActors() {
        return new Object[][]{{Actor.ANON}, {Actor.QA}, {Actor.COMPANY}};
    }

    @DataProvider(name = "allowedActors")
    public Object[][] allowedActors() {
        return new Object[][]{{Actor.ADMIN}};
    }

    private static void deleteIfCreated(Response response) {
        if (response.statusCode() == 201) {
            String id = response.jsonPath().getString("[0].id");
            SupabaseApi.deleteBlogPost(Actor.ADMIN.bearer(), id);
        }
    }

    @Test(dataProvider = "deniedActors")
    public void actorCannotCreatePost(Actor actor) {
        Response response = tryCreatePost(actor);
        try {
            int status = response.statusCode();
            Assert.assertTrue(status == 401 || status == 403,
                    "Actor must be denied, but status was " + status + " Actor was " + actor);
            Assert.assertEquals(response.jsonPath().getString("code"), "42501",
                    "Denial should come from row-level security");
        } finally {
            deleteIfCreated(response);
        }
    }

    @Test(dataProvider = "allowedActors")
    public void actorCanCreatePost(Actor actor) {
        Response response = tryCreatePost(actor);
        try {
            int status = response.statusCode();
            Assert.assertEquals(status, 201,
                    "Admin can create but get  " + status + " Actor was " + actor);
        } finally {
            deleteIfCreated(response);
        }
    }

    private static Response tryCreatePost(Actor actor) {
        String bearer = actor.bearer();
        return given()
                .spec(SupabaseSpecs.forUser(bearer))
                .header("Prefer", "return=representation")
                .body(NewBlogPost.draft("Autotest Draft " + System.currentTimeMillis()))
                .when()
                .post("/rest/v1/blog_posts");
    }
}
