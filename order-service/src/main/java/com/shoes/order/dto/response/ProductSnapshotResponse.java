package com.shoes.order.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO snapshot cua san pham — order-service deserialize tu Feign call den product-service.
 *
 * product-service tra ve field "id" → ta map sang "productId" o day cho ro nghia.
 * Cac field khac tu chuoi JSON cua product-service phai khop ten.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSnapshotResponse {

    @JsonProperty("id")            // map JSON "id" → Java field productId
    private Long productId;

    private String name;
    private String slug;
    private String sku;
    private Long price;

    // product-service chua tra ve field "image" → se null cho den khi them
    private String image;
}
