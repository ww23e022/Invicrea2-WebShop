package at.technikum.Invicrea2WebShopbackend.mapper;

import at.technikum.Invicrea2WebShopbackend.dto.ItemDto;
import at.technikum.Invicrea2WebShopbackend.model.Item;
import org.springframework.stereotype.Component;

/**
 * Mapper class responsible for mapping between Item and ItemDto objects.
 */
@Component
public class ItemMapper {

    /**
     * Maps an Item object to an ItemDto object.
     *
     * @param item The Item object to map.
     * @return The corresponding ItemDto object.
     */
    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setPrice(item.getPrice());
        itemDto.setDescription(item.getDescription());
        itemDto.setImageUrl(item.getImageUrl());
        itemDto.setCategory(item.getCategory());
        return itemDto;
    }

    /**
     * Maps an ItemDto object to an Item object.
     *
     * @param itemDto The ItemDto object to map.
     * @return The corresponding Item object.
     */
    public Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());
        item.setDescription(itemDto.getDescription());
        item.setImageUrl(itemDto.getImageUrl());
        item.setCategory(itemDto.getCategory());
        return item;
    }
}

