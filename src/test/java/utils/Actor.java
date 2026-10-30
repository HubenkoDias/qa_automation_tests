package utils;

public enum Actor {
    ANON, QA, COMPANY, ADMIN;

    public String bearer() {
        return switch (this) {
            case ANON -> SupabaseApi.anonKey();
            case QA -> SupabaseApi.signIn(TestUser.QA);
            case COMPANY -> SupabaseApi.signIn(TestUser.COMPANY);
            case ADMIN -> SupabaseApi.signIn(TestUser.ADMIN);
        };
    }
}
