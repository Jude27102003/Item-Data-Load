package com.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
