package fr.sushi.app.data.model.food_menu;

public class TopMenuItem {
    private String imageUrl;
    private String name;

    public TopMenuItem(String name, String imageUrl) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}
