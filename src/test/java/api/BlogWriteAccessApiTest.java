package api;

import io.restassured.response.Response;
import model.NewBlogPost;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.Actor;
import utils.SupabaseApi;
import utils.SupabaseSpecs;

import java.util.List;
import java.util.Map;

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

    private static String readTitle(String bearer, String id) {
        return given().spec(SupabaseSpecs.forUser(bearer))
                .when()
                .get("/rest/v1/blog_posts?id=eq." + id + "&select=title")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract().jsonPath().getString("[0].title");
    }
    @Test (dataProvider = "deniedActors")
        public void actorCannotUpdateDraft (Actor actor) {
        String adminBearer = Actor.ADMIN.bearer();
        String title = "Autotest original" + System.currentTimeMillis();
        String id = SupabaseApi.createDraft(adminBearer, title);
        try {
            Response attempt = given().spec(SupabaseSpecs.forUser(actor.bearer()))
                    .header("Prefer", "return=representation")
                    .body(Map.of("title", "HACKED"))
                    .when().patch("/rest/v1/blog_posts?id=eq." + id);
            int status = attempt.statusCode();
            Assert.assertEquals(status, 200,
                    "Status code must be 200");
            List<Object> ids = attempt.jsonPath().getList("id");
            Assert.assertTrue(ids.isEmpty(),
                    "List should return empty");
            Assert.assertEquals(readTitle(adminBearer, id), title,
            "Title should match to the original title");
        }
        finally {
            SupabaseApi.deleteBlogPost(adminBearer, id);
        }
    }
    @Test (dataProvider = "deniedActors")
    public void actorCannotUpdatePublishedPost(Actor actor) {
        String token = Actor.ADMIN.bearer();
        String originalTitle = "Autotest draft " + System.currentTimeMillis();
        String id = SupabaseApi.createDraft(token, originalTitle);

        try {
            SupabaseApi.publishPost(token, id);
            int actorSees = SupabaseApi.visibleRows(SupabaseSpecs.forUser(actor.bearer()), id);
            Assert.assertEquals(actorSees, 1,
                    "Actor should see the published post");
            Response attempt = given().spec(SupabaseSpecs.forUser(actor.bearer()))
                    .header("Prefer", "return=representation")
                    .body(Map.of("title", "HACKED"))
                    .when().patch("/rest/v1/blog_posts?id=eq." + id);
            int status = attempt.statusCode();
            Assert.assertEquals(status, 200,
                    "Actor " + actor + ": status was " + status + ", body: " + attempt.asString());
            List<Object> ids = attempt.jsonPath().getList("id");
            Assert.assertTrue(ids.isEmpty(),
                    "Actor " + actor + " must not update anything, body: " + attempt.asString());
            Assert.assertEquals(readTitle(token, id), originalTitle,
                    "Title should match to the original title");
        }
        finally {
            SupabaseApi.deleteBlogPost(token, id);
        }
    }
}
