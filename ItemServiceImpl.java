package com.item.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.item.ItemDataLoadApplication;
import com.item.dto.ItemDto;
import com.item.entity.Item;
import com.item.exception.ItemNotFoundException;
import com.item.repository.ItemRepository;
import com.item.service.ItemService;

import jakarta.validation.ValidationException;

@Service
public class ItemServiceImpl implements ItemService {
	
	private ItemRepository itemRepository;

	public ItemServiceImpl(ItemRepository itemRepository) {
		super();
		this.itemRepository = itemRepository;
	}

	@Override
	public ItemDto saveItem(ItemDto itemDto) {
		validateItemPack(itemDto);
		Item item=convertToEntity(itemDto);
		Item item1=itemRepository.save(item);
		ItemDto itemDto2=convertToDto(item1);
        return itemDto2;
	}

	@Override
	public Optional<ItemDto> getItemById(int id) {
		
		Item item=itemRepository.findById(id).orElseThrow(()->new ItemNotFoundException("Item with id "+id+" not found"));
		ItemDto itemDto=convertToDto(item);
		return Optional.of(itemDto);
	}

	@Override
	public ItemDto updateItem(int id, ItemDto itemDto) {
		
		if(!itemRepository.existsById(id)) {
			throw new ItemNotFoundException("Laptop with id "+id+" not found");
		}
		validateItemPack(itemDto);
		Item item1=itemRepository.findById(id).get();
		
		if(itemDto.getItemName()!=null) {
			item1.setItemName(itemDto.getItemName());
		}
		
		if(itemDto.getItemCost()!=null) {
			item1.setItemCost(itemDto.getItemCost());
		}
		
		if(itemDto.getItemQuantity()>0) {
			item1.setItemQuantity(itemDto.getItemQuantity());
		}
		
		if (itemDto.getItemPack() != item1.getItemPack()) {
			item1.setItemPack(itemDto.getItemPack());
		}
		
		if (itemDto.getItemContents() != null) {
			item1.setItemContents(itemDto.getItemContents());
		}
		
		if (itemDto.getItemDimensions() > 0) {
			item1.setItemDimensions(itemDto.getItemDimensions());
		}
		
		if (itemDto.getItemOriginLocation() != null) {
			item1.setItemOriginLocation(itemDto.getItemOriginLocation());
		}
		
		if (itemDto.getItemShip() != item1.getItemShip()) {
			item1.setItemShip(itemDto.getItemShip());
		}
		
		if (itemDto.getItemCompany() != null) {
			item1.setItemCompany(itemDto.getItemCompany());
		}
		
		if (itemDto.getItemManufacturingDateTime() != null) {
			item1.setItemManufacturingDateTime(itemDto.getItemManufacturingDateTime());
		}
		
		if (itemDto.getItemExpiryDate() != null) {
			item1.setItemExpiryDate(itemDto.getItemExpiryDate());
			
		}
		
		Item item=itemRepository.save(item1);
		ItemDto itemDto1=convertToDto(item);
        return itemDto1;
        
	}

	@Override
	public void deleteItem(int id) {
		if(!itemRepository.existsById(id)) {
			throw new ItemNotFoundException("Item with id "+id+" not found");
		}
		
		itemRepository.deleteById(id);
	}

	@Override
	public List<ItemDto> getAllItems() {
		return itemRepository.findAll().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}
	
	private void validateItemPack(ItemDto itemDto) {
		if("Y".equalsIgnoreCase(itemDto.getItemPack())) {
            if (itemDto.getItemContents() == null) {
                throw new ValidationException("Item contents must be populated when Item Pack is 'Y'");
            }
        } else if ("N".equalsIgnoreCase(itemDto.getItemPack())) {
            if (itemDto.getItemContents() != null) {
                throw new ValidationException("Item contents cannot not be populated when item Pack is 'N'");
            }
        }
  }
	
public ItemDto convertToDto(Item item) {
		
		if(item==null)
			return null;
		
		ItemDto itemDto=new ItemDto();
		itemDto.setId(item.getId());
		itemDto.setItemName(item.getItemName());
		itemDto.setItemCost(item.getItemCost());
		itemDto.setItemQuantity(item.getItemQuantity());
		itemDto.setItemPack(item.getItemPack());
		itemDto.setItemContents(item.getItemContents());
		itemDto.setItemDimensions(item.getItemDimensions());
		itemDto.setItemOriginLocation(item.getItemOriginLocation());
		itemDto.setItemShip(item.getItemShip());
		itemDto.setItemCompany(item.getItemCompany());
		itemDto.setItemManufacturingDateTime(item.getItemManufacturingDateTime());
		itemDto.setItemExpiryDate(item.getItemExpiryDate());
		
		return itemDto;
	}
	
	public Item convertToEntity(ItemDto itemDto) {
		
		if(itemDto==null)
			return null;
		
		Item item=new Item();
		item.setId(item.getId());
		item.setItemName(itemDto.getItemName());
		item.setItemCost(itemDto.getItemCost());
		item.setItemQuantity(itemDto.getItemQuantity());
		item.setItemPack(itemDto.getItemPack());
		item.setItemContents(itemDto.getItemContents());
		item.setItemDimensions(itemDto.getItemDimensions());
		item.setItemOriginLocation(itemDto.getItemOriginLocation());
		item.setItemShip(itemDto.getItemShip());
		item.setItemCompany(itemDto.getItemCompany());
		item.setItemManufacturingDateTime(itemDto.getItemManufacturingDateTime());
		item.setItemExpiryDate(itemDto.getItemExpiryDate());
		
		return item;
		
	}
}


