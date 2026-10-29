package model;

public record NewBlogPost(
        String title,
        String slug,
        String excerpt,
        String content,
        String category,
        boolean published
) {
    public static NewBlogPost draft (String title) {
        return new NewBlogPost(
                title,
                title.toLowerCase().replace(' ', '-'),
                "autotest",
                "autotest",
                "article",
                false
        );
    }
}