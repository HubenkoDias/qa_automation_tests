package utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class SupabaseSpecs {

    private SupabaseSpecs() {
    }

    private static RequestSpecBuilder common () {
        return new RequestSpecBuilder()
                .setBaseUri(TestConfig.requireEnv("HHUB_SUPABASE_URL"))
                .addHeader("apikey", TestConfig.requireEnv("HHUB_SUPABASE_ANON_KEY"))
                .setContentType(ContentType.JSON);
    }

    public static RequestSpecification anon() {
        String key = TestConfig.requireEnv("HHUB_SUPABASE_ANON_KEY");
        return common().addHeader("Authorization", "Bearer " + key).build();
    }

    public static RequestSpecification forUser (String token) {
        return common().addHeader("Authorization", "Bearer " + token).build();
    }

    public static RequestSpecification noUser () {
        return common().build();
    }

 }
