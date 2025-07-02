package com.item.service.implementation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.item.dto.ItemDto;
import com.item.entity.Item;
import com.item.exception.ItemNotFoundException;
import com.item.exception.ValidationException;
import com.item.repository.ItemRepository;

//Unit tests for ItemServiceImpl using Mockito and JUnit.
@SpringBootTest
class ItemServiceImplTest {

	@Mock
	ItemRepository itemRepository;

	@InjectMocks
	ItemServiceImpl itemServiceImpl;

	Item item1;
	Item item2;
	Item item3;
	Item item4;
	Item item5;

	List<Item> items;

	@BeforeEach
	void init() {
		//Initialize test data
		item1 = new Item(1, "Laptop", "999.99", 2, true, 5, 15, "Trichy", false, "Lenovo",
				LocalDateTime.of(2021, 10, 12, 05, 45, 30), LocalDate.of(2026, 03, 03));
		item2 = new Item(2, "Soap", "999.99$", 12, true, 5, 15, "Mumbai", true, "Hamam",
				LocalDateTime.of(2023, 8, 23, 05, 45, 30), LocalDate.of(2026, 02, 23));
		item3 = new Item(3, "Shoe", "9.09$", 1, false, 5, 15, "America", false, "Nike",
				LocalDateTime.of(2021, 10, 12, 05, 45, 30), LocalDate.of(2026, 03, 03));
		item4 = new Item(4, "Laptop", "10000.99$", 12, false, null, 25, "California", true, "HP",
				LocalDateTime.of(2020, 6, 12, 05, 45, 30), LocalDate.of(2032, 8, 22));
		item5 = new Item(1, "Laptop", "102.99$", 2, true, null, 15, "Trichy", false, "Lenovo",
				LocalDateTime.of(2021, 10, 12, 05, 45, 30), LocalDate.of(2026, 03, 03));

		items = new ArrayList<Item>();

		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		items.add(item5);
	}

	//Tests retrieval of all items.
	@Test
	void testGetAll() {
		when(itemRepository.findAll()).thenReturn(items);

		var result = itemServiceImpl.getAll();

		assertEquals(5, result.size());
		assertEquals("Shoe", result.get(2).getItemName());
	}

	//Tests retrieval of an item by ID.
	@Test
	void testGetById() {

		when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item3));

		var result = itemServiceImpl.getById(3);

		assertNotNull(result);
		assertEquals("Shoe", result.getItemName());

		verify(itemRepository, times(1)).findById(3);
	}

	//Tests adding a valid item.
	@Test
	void testAddItem() {

		when(itemRepository.save(any(Item.class))).thenReturn(item1);

		ItemDto itemDto = itemServiceImpl.convertToDto(item1);
		var result = itemServiceImpl.addItem(itemDto);

		assertNotNull(result);
		assertEquals("Trichy", result.getItemOriginLocation());

		verify(itemRepository, times(1)).save(item1);

	}

	//Tests updating an existing item by ID.
	@Test
	void testUpdateItemById() {

		when(itemRepository.existsById(anyInt())).thenReturn(true);

		when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item2));

		when(itemRepository.save(any(Item.class))).thenReturn(item2);

		ItemDto itemDto = itemServiceImpl.convertToDto(item1);
		var result = itemServiceImpl.updateItemById(2, itemDto);

		assertNotNull(result);
		assertEquals("Laptop", result.getItemName());

		verify(itemRepository, times(1)).existsById(2);
		verify(itemRepository, times(1)).findById(2);
		verify(itemRepository, times(1)).save(item2);
	}

	//Tests update operation when item ID is not found.
	@Test
	void testUpdateWhenItemIdNotFound() {

		when(itemRepository.existsById(anyInt())).thenReturn(false);
		when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item4));

		ItemDto invalidItem = itemServiceImpl.convertToDto(item4);

		assertThrows(ItemNotFoundException.class, () -> itemServiceImpl.updateItemById(1, invalidItem));

		verify(itemRepository, never()).save(any(Item.class));
	}

	//Tests deletion of an item by ID.
	@Test
	void testDeleteById() {

		when(itemRepository.existsById(anyInt())).thenReturn(true);

		itemServiceImpl.deleteById(1);

		verify(itemRepository, times(1)).deleteById(1);
	}

	//Tests deletion when item is not found
	@Test
	void testDeleteWhenItemNotFound() {

		when(itemRepository.existsById(anyInt())).thenReturn(false);

		assertThrows(ItemNotFoundException.class, () -> itemServiceImpl.deleteById(1));

		verify(itemRepository, times(1)).existsById(1);

		verify(itemRepository, never()).deleteById(1);
	}

	//Tests validation failure when itemPack is false but contents are present.
	@Test
	void testAddItemWhenItemPackIsFalse() {
		// item3 has itemPack = false and itemContents != null → should throw
		when(itemRepository.save(any(Item.class))).thenReturn(item3);

		ItemDto itemDto = itemServiceImpl.convertToDto(item3);
		assertThrows(ValidationException.class, () -> itemServiceImpl.addItem(itemDto));

		verify(itemRepository, never()).save(item3);
	}

	//Tests validation failure when itemPack is true but contents are null.
	@Test
	void testAddItemWhenItemPackIsTrueButContentsNull() {
		item1.setItemContents(null); // invalid case

		ItemDto itemDto = itemServiceImpl.convertToDto(item5);
		assertThrows(ValidationException.class, () -> itemServiceImpl.addItem(itemDto));

		verify(itemRepository, never()).save(any(Item.class));
	}

	//Tests valid case when itemPack is false and contents are null.
	@Test
	void testAddItemWhenItemPackIsFalseAndContentsNull() {
		item1.setItemPack(false);
		item1.setItemContents(null);

		when(itemRepository.save(any(Item.class))).thenReturn(item4);

		ItemDto itemDto = itemServiceImpl.convertToDto(item4);

		var result = itemServiceImpl.addItem(itemDto);

		assertNotNull(result);
		assertEquals("Laptop", result.getItemName());

		verify(itemRepository, times(1)).save(any(Item.class));
	}

	//Tests valid case when itemPack is true and contents are present.
	@Test
	void testAddItemWhenItemPackIsTrueAndContentsPresent() {
		item1.setItemPack(true);
		item1.setItemContents(5); // valid

		when(itemRepository.save(any(Item.class))).thenReturn(item1);

		ItemDto itemDto = itemServiceImpl.convertToDto(item1);

		var result = itemServiceImpl.addItem(itemDto);

		assertNotNull(result);
		assertEquals("Laptop", result.getItemName());

		verify(itemRepository, times(1)).save(any(Item.class));
	}

}
