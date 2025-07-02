
package com.item.service;

import com.item.dto.ItemDto;
import java.util.Optional;
import java.util.List;

public interface ItemService {
    ItemDto saveItem(ItemDto itemDto);
    Optional<ItemDto> getItemById(int id);
    ItemDto updateItem(int id, ItemDto itemDto);
    void deleteItem(int id);
    List<ItemDto> getAllItems();
}
