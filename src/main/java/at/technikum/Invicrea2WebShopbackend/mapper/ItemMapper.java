package at.technikum.Invicrea2WebShopbackend.mapper;

import at.technikum.Invicrea2WebShopbackend.dto.ItemDto;
import at.technikum.Invicrea2WebShopbackend.model.Item;
import org.springframework.stereotype.Component;

// Klasse, die für die Abbildung von Objekten der Klasse Item
// auf Objekte der Klasse ItemDto und umgekehrt verantwortlich ist.
@Component
public class ItemMapper {
    // Sie konvertiert ein Objekt der Klasse Item in ein Objekt der Klasse ItemDto.
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

    //Sie konvertiert ein Objekt der Klasse ItemDto in ein Objekt der Klasse Item.

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
