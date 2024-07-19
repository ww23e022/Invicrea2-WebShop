package at.technikum.Invicrea2WebShopbackend.dto;

import at.technikum.Invicrea2WebShopbackend.model.Empire;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Dto representing player-related information. */
public class PlayerDto {
    private Long id;
    @NotNull
    @Size(min = 1, max = 100) // name length between 1 and 100 characters
    private String name;
    private Empire empire;
    @NotNull
    @Min(value = 1) // level must be at least 1
    private int level;
    private Long userId;

    // Constructors, getters, and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Empire getEmpire() {
        return empire;
    }

    public void setEmpire(Empire empire) {
        this.empire = empire;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Long getUserId () {
        return userId;
    }

    public void setUserId ( Long userId ) {
        this.userId = userId;
    }
}
