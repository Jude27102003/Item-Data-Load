package com.item.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Item name is mandatory")
    private String itemName;

    @NotBlank(message = "Item cost is mandatory")
    private String itemCost;

    @NotNull(message = "Item quantity is mandatory")
    private Integer itemQuantity;

    @NotBlank(message = "Item pack is mandatory")
    @Pattern(regexp = "Y|N", message = "Item pack must be Y or N")
    private String itemPack;

    private Integer itemContents;

    @NotNull(message = "Item dimensions are mandatory")
    private Double itemDimensions;

    @NotBlank(message = "Item origin location is mandatory")
    private String itemOriginLocation;

    @NotNull(message = "Item ship is mandatory")
    private Boolean itemShip;

    @NotBlank(message = "Item company is mandatory")
    private String itemCompany;

    @NotNull(message = "Manufacturing date is mandatory")
    private LocalDateTime itemManufacturingDateTime;

    @NotNull(message = "Expiry date is mandatory")
    private LocalDate itemExpiryDate;

    // Getters and Setters
    // (Omitted for brevity)
}

