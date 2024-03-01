package at.technikum.Invicrea2WebShopbackend.model;

import jakarta.persistence.*;

/** Entität, die ein Artikel im Webshop repräsentiert. */
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;


    private String name;


    private int price;


    private String description;


    private String imageUrl;

    private ItemCategory category;

    public Item(Long id, String name, int price, String description,
                String imageUrl, ItemCategory category) {
        this.itemId = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Item() {
        this.category = ItemCategory.VIEW_ALL;
    }

    public Long getId () {
        return itemId;
    }

    public void setId ( Long id ) {
        this.itemId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }
}
