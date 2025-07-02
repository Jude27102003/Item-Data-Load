package com.item.controller.test;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.item.dto.ItemDto;
import com.item.service.ItemService;

//Unit tests for ItemController endpoints using MockMvc.
@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

	
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ItemService itemService;

	ItemDto item1;
	ItemDto item2;
	ItemDto item3;

	List<ItemDto> items;

	@Autowired
	private ObjectMapper mapper;

	@BeforeEach
	void init() {

		item1 = new ItemDto(1, "Laptop", "999.99", 2, true, 5, 15, "India", false, "Lenovo",
				LocalDateTime.of(2021, 10, 12, 05, 45, 30), LocalDate.of(2026, 03, 03));
		item2 = new ItemDto(2, "Laptop", "125.06", 6, true, 10, 15, "Mumbai", true, "Asus",
				LocalDateTime.of(2023, 8, 23, 05, 45, 30), LocalDate.of(2026, 02, 23));
		item3 = new ItemDto(3, "Laptop", "570.99", 3, true, 6, 15, "America", false, "Dell",
				LocalDateTime.of(2021, 10, 12, 05, 45, 30), LocalDate.of(2026, 03, 03));

		items = new ArrayList<ItemDto>();
		items.add(item1);
		items.add(item2);
		items.add(item3);
	}

	//Tests retrieval of all items.
	@Test
	@WithMockUser
	void testGetAllItems() throws Exception {

		when(itemService.getAll()).thenReturn(items);

		mockMvc.perform(get("/api/laptops")).andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(3)))
				.andExpect(jsonPath("$[0].itemId", is(1)));
	}

	//Tests retrieval of a single item by ID.
	@Test
	@WithMockUser
	void testGetById() throws Exception {

		when(itemService.getById(anyInt())).thenReturn(item1);

		mockMvc.perform(get("/api/laptops/1")).andExpect(status().isOk()).andExpect(jsonPath("$.itemId", is(1)));

	}

	// Tests adding a new item (ADMIN role required).
	@Test
	@WithMockUser(roles = "ADMIN")
	void testAddItem() throws Exception {

		when(itemService.addItem(any(ItemDto.class))).thenReturn(item2);

		var result = mapper.writeValueAsString(item2);

		mockMvc.perform(post("/api/laptops").content(result).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.itemOriginLocation").value("Mumbai"));
	}

	//Tests updating an existing item by ID.
	@Test
	@WithMockUser
	void testUpdateById() throws Exception {
		when(itemService.updateItemById(anyInt(), any(ItemDto.class))).thenReturn(item1);

		var result = mapper.writeValueAsString(item1);

		mockMvc.perform(put("/api/laptops/1").content(result).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated()).andExpect(jsonPath("$.itemName").value("Laptop"));
	}

	//Tests deleting an item by ID (ADMIN role required).
	@Test
	@WithMockUser(roles = "ADMIN")
	void testDeleteById() throws Exception {

		doNothing().when(itemService).deleteById(anyInt());

		mockMvc.perform(delete("/api/laptops/1")).andExpect(status().isAccepted());
	}


}
