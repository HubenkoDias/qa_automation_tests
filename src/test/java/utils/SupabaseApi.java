package utils;

import io.restassured.specification.RequestSpecification;
import model.NewBlogPost;

import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public final class SupabaseApi {

    private SupabaseApi () {
    }
    public static String anonKey() {
        return TestConfig.requireEnv("HHUB_SUPABASE_ANON_KEY");
    }

    public static String signIn(TestUser user) {
        return given().spec(SupabaseSpecs.noUser())
                .body(Map.of("email", user.email(), "password", user.password()))
                .when()
                    .post("/auth/v1/token?grant_type=password")
                .then()
                    .statusCode(200)
                    .extract().path("access_token");
    }

    public static String createDraft (String token, String title) {
        return given().spec(SupabaseSpecs.forUser(token))
                    .header("Prefer", "return=representation")
                    .body(NewBlogPost.draft(title))
                .when()
                    .post("/rest/v1/blog_posts")
                .then()
                    .log().ifValidationFails()
                    .statusCode(201)
                    .extract().path("[0].id");
    }

    public static void deleteBlogPost (String token, String id) {
            given().spec(SupabaseSpecs.forUser(token))
                    .header("Prefer", "return=representation")
            .when()
                    .delete("/rest/v1/blog_posts?id=eq." + id)
            .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("size()", is(1));
    }

    public static List<String> readBlogPostIds (String bearer, boolean published) {
        return given().spec(SupabaseSpecs.forUser(bearer))
        .when()
                .get("/rest/v1/blog_posts?select=id&published=eq." + published)
        .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract().jsonPath().getList("id");
    }
    public static void tryCreateDraft (String token, String title) {
        given().spec(SupabaseSpecs.forUser(token))
                .header("Prefer", "return=representation")
                .body(NewBlogPost.draft(title))
        .when()
                .post("/rest/v1/blog_posts")
        .then()
                .log().ifValidationFails()
                .statusCode(anyOf(is(401), is(403)))
                .body("code", equalTo("42501"));
    }

    public static int visibleRows(RequestSpecification spec, String id) {
        return given().spec(spec)
                .when()
                    .get("/rest/v1/blog_posts?id=eq." + id + "&select=id")
                .then()
                    .statusCode(200)
                    .log().ifValidationFails()
                    .extract().jsonPath().getList("id").size();
    }
    public static void publishPost(String bearer, String id) {
        given().spec(SupabaseSpecs.forUser(bearer))
                .header("Prefer", "return=representation")
                .body(Map.of("published", true))
                .when().patch("/rest/v1/blog_posts?id=eq." + id)
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("size()", is(1));
    }
}
