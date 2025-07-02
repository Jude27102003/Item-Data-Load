package com.item.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.item.dto.ItemDto;
import com.item.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/laptops")
public class ItemController {

	//Injects the ItemService to handle business logic
	private ItemService itemService;

	//Constructor injection for ItemService
	public ItemController(ItemService itemService) {
		super();
		this.itemService = itemService;
	}

	//Retrieves all items
	@GetMapping
	public List<ItemDto> getAllItems() {

		return itemService.getAll();
	}

	//Retrieves an item by its ID
	@GetMapping("/{id}")
	public ItemDto getById(@PathVariable int id) {

		return itemService.getById(id);
	}

	//Adds a new item (Admin only)
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ItemDto> addItem(@Valid @RequestBody ItemDto itemDto) {

		ItemDto itemDto1 = itemService.addItem(itemDto);
		return new ResponseEntity<>(itemDto1, HttpStatus.CREATED);
	}

	//Updates an existing item by ID
	@PutMapping("/{id}")
	public ResponseEntity<ItemDto> updateById(@PathVariable int id, @Valid @RequestBody ItemDto itemDto) {

		ItemDto itemDto1 = itemService.updateItemById(id, itemDto);
		return new ResponseEntity<>(itemDto1, HttpStatus.CREATED);
	}

	//Deletes an item by ID (Admin only)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteById(@PathVariable int id) {

		itemService.deleteById(id);
		return ResponseEntity.accepted().build();

	}

}
