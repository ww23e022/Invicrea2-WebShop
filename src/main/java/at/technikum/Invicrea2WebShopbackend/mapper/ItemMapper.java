package at.technikum.Invicrea2WebShopbackend.mapper;

import at.technikum.Invicrea2WebShopbackend.dto.ItemDto;
import at.technikum.Invicrea2WebShopbackend.model.File;
import at.technikum.Invicrea2WebShopbackend.model.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setPrice(item.getPrice());
        itemDto.setDescription(item.getDescription());
        if (item.getFile() != null) {
            itemDto.setFileId(item.getFile().getId());
        }
        itemDto.setCategory(item.getCategory());
        return itemDto;
    }

    public Item toItem(ItemDto itemDto, File file) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());
        item.setDescription(itemDto.getDescription());
        item.setFile(file);
        item.setCategory(itemDto.getCategory());
        return item;
    }
}
