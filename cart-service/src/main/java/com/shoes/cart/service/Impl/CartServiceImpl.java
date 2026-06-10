package com.shoes.cart.service.Impl;

import com.shoes.cart.client.ProductClient;
import com.shoes.cart.config.SecurityUtils;
import com.shoes.cart.dto.ApiResponse;
import com.shoes.cart.dto.Request.CartItemRequest;
import com.shoes.cart.dto.Response.CartResponse;
import com.shoes.cart.dto.Response.ProductResponse;
import com.shoes.cart.dto.Response.ProductSizeResponse;
import com.shoes.cart.entity.Cart;
import com.shoes.cart.entity.CartItem;
import com.shoes.cart.exception.ConflictException;
import com.shoes.cart.exception.ResourceNotFoundException;
import com.shoes.cart.mapper.CartItemMapper;
import com.shoes.cart.repository.CartRepository;
import com.shoes.cart.service.CartService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Override
    public Cart getCartById(Long id) {
        return null;
    }

    @Override
    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart", "user id", userId));
        return cartItemMapper.toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addCart(CartItemRequest cartItemRequest) {
        Cart cart = getCartEntityByUserId();
        ResponseEntity<ApiResponse<ProductSizeResponse>> productSizeResponse =
                productClient.getProductSize(cartItemRequest.getSizeVn(), cartItemRequest.getProductId());

        if (productSizeResponse.getBody() == null || !productSizeResponse.getBody().getSuccess()) {
            throw new ConflictException("There are no products in this size.");
        }
        ProductSizeResponse sizeData = productSizeResponse.getBody().getData();

        int requestQuantity = 1;

        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(cartItemRequest.getProductId())
                        && item.getSizeVn().equals(cartItemRequest.getSizeVn()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + requestQuantity;
            if (newQuantity > sizeData.getQuantity()) {
                throw new ConflictException("The product is out of stock");
            }
            cartItem.setQuantity(newQuantity);
        } else {
            if (requestQuantity > sizeData.getQuantity()) {
                throw new ConflictException("The product is out of stock");
            }
            ResponseEntity<ApiResponse<ProductResponse>> productResponse =
                    productClient.getProductById(cartItemRequest.getProductId());

            if (productResponse.getBody() == null || !productResponse.getBody().getSuccess()) {
                throw new ConflictException("Product not found");
            }

            ProductResponse productData = productResponse.getBody().getData();

            CartItem newItem = cartItemMapper.toEntity(cartItemRequest, productData);
            newItem.setQuantity(requestQuantity);

            cart.addItem(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return cartItemMapper.toResponse(savedCart);
    }

    private Cart getCartEntityByUserId() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return cartRepository.findByUserId(currentUserId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(currentUserId);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional
    public void removeProductsFromCart(Long userId, List<Long> purchasedProductIds) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Not found cart for userId: " + userId));
        cart.getItems().removeIf(item -> purchasedProductIds.contains(item.getProductId()));
        cartRepository.saveAndFlush(cart);
    }
}
