package com.eshop.library.service.impl;

import com.eshop.library.dto.CartItemDto;
import com.eshop.library.dto.ProductDto;
import com.eshop.library.dto.ShoppingCartDto;
import com.eshop.library.model.CartItem;
import com.eshop.library.model.Customer;
import com.eshop.library.model.Product;
import com.eshop.library.model.ShoppingCart;
import com.eshop.library.repository.CartItemRepository;
import com.eshop.library.repository.ShoppingCartRepository;
import com.eshop.library.service.CustomerService;
import com.eshop.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository; // Repository for shopping carts

    private final CartItemRepository itemRepository; // Repository for cart items

    private final CustomerService customerService; // Service handling customer-related operations

    @Override
    public ShoppingCart addItemToCart(ProductDto productDto, int quantity, String username) {
        // Retrieves the customer by username
        Customer customer = customerService.findByUsername(username);

        // Retrieves the shopping cart associated with the customer
        ShoppingCart shoppingCart = customer.getCart();

        // If the cart is empty, a new ShoppingCart instance is created
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
        }

        // Retrieves the list of items in the cart
        Set<CartItem> cartItemList = shoppingCart.getCartItems();

        // Finds a specific item in the cart based on the product ID
        CartItem cartItem = find(cartItemList, productDto.getId());

        // Transforms the product data transfer object into a Product entity
        Product product = transfer(productDto);

        // Retrieves the unit price and quantity of the product
        double unitPrice = productDto.getCostPrice();
        int itemQuantity = quantity;

        // If the item is found in the cart, the quantity is updated
        if (cartItem != null) {
            itemQuantity += cartItem.getQuantity();
        } else {
            // If the item is not found, a new CartItem is created and added to the cart
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(shoppingCart);
            cartItemList.add(cartItem);
        }

        // Updates the quantity and unit price of the item
        cartItem.setQuantity(itemQuantity);
        cartItem.setUnitPrice(unitPrice);

        // Saves the cart item to the repository
        itemRepository.save(cartItem);

        // Updates the list of items in the shopping cart, total price, total items, and the associated customer
        shoppingCart.setCartItems(cartItemList);
        shoppingCart.setTotalPrice(totalPrice(cartItemList));
        shoppingCart.setTotalItems(totalItem(cartItemList));
        shoppingCart.setCustomer(customer);

        // Saves and returns the updated shopping cart
        return cartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart updateCart(ProductDto productDto, int quantity, String username) {
        // Retrieve the customer using the provided username
        Customer customer = customerService.findByUsername(username);

        // Get the shopping cart associated with the customer
        ShoppingCart shoppingCart = customer.getCart();

        // Retrieve the list of items in the shopping cart
        Set<CartItem> cartItemList = shoppingCart.getCartItems();

        // Find the specific item in the cart based on the product ID
        CartItem item = find(cartItemList, productDto.getId());

        // Set the quantity of the item to the given quantity
        int itemQuantity = quantity;
        item.setQuantity(itemQuantity);

        // Save the changes made to the item in the cart
        itemRepository.save(item);

        // Update the shopping cart's list of cart items
        shoppingCart.setCartItems(cartItemList);

        // Calculate the total number of items and the total price in the cart
        int totalItem = totalItem(cartItemList);
        double totalPrice = totalPrice(cartItemList);

        // Set the total price and total items in the shopping cart
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);

        // Save and return the updated shopping cart
        return cartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeItemFromCart(ProductDto productDto, String username) {
        // Retrieve the customer using the provided username
        Customer customer = customerService.findByUsername(username);

        // Get the shopping cart associated with the customer
        ShoppingCart shoppingCart = customer.getCart();

        // Retrieve the list of items in the shopping cart
        Set<CartItem> cartItemList = shoppingCart.getCartItems();

        // Find the specific item in the cart based on the product ID
        CartItem item = find(cartItemList, productDto.getId());

        // Remove the item from the cart and delete it from the database
        cartItemList.remove(item);
        itemRepository.delete(item);

        // Recalculate the total price and total items after removal
        double totalPrice = totalPrice(cartItemList);
        int totalItem = totalItem(cartItemList);

        // Update the shopping cart's list of cart items, total price, and total items
        shoppingCart.setCartItems(cartItemList);
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);

        // Save and return the updated shopping cart
        return cartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto addItemToCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity) {
        // Find the cart item by product ID within the provided cart DTO
        CartItemDto cartItem = findInDTO(cartDto, productDto.getId());

        // If the cart DTO is null, create a new one
        if (cartDto == null) {
            cartDto = new ShoppingCartDto();
        }

        // Retrieve the list of cart items from the cart DTO
        Set<CartItemDto> cartItemList = cartDto.getCartItems();

        // Obtain the cost price of the product
        double unitPrice = productDto.getCostPrice();
        int itemQuantity = 0;

        // Check if the cart item list is null and perform actions accordingly
        if (cartItemList == null) {
            cartItemList = new HashSet<>();
            if (cartItem == null) {
                // If the cart item doesn't exist, create a new one and add it to the cart DTO
                cartItem = new CartItemDto();
                cartItem.setProduct(productDto);
                cartItem.setCart(cartDto);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItemList.add(cartItem);
                System.out.println("add");
            } else {
                // If the cart item exists, update its quantity
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
            }
        } else {
            if (cartItem == null) {
                // If the cart item doesn't exist, create a new one and add it to the cart DTO
                cartItem = new CartItemDto();
                cartItem.setProduct(productDto);
                cartItem.setCart(cartDto);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItemList.add(cartItem);
                System.out.println("add");
            } else {
                // If the cart item exists, update its quantity
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
            }
        }

        System.out.println("here");

        // Update the cart DTO with the modified cart items, total price, and total items
        cartDto.setCartItems(cartItemList);
        double totalPrice = totalPriceDto(cartItemList);
        int totalItem = totalItemDto(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);

        // Output the total items, total price, and a success message
        System.out.println(cartDto.getTotalItems());
        System.out.println(cartDto.getTotalPrice());
        System.out.println("success");

        return cartDto; // Return the updated cart DTO
    }

    private int totalItem(Set<CartItem> cartItemList) {
        int totalItem = 0;
        // Iterate through each CartItem in the cartItemList
        for (CartItem item : cartItemList) {
            // Increment the totalItem by the quantity of each CartItem
            totalItem += item.getQuantity();
        }
        return totalItem; // Return the accumulated total number of items
    }

    private double totalPrice(Set<CartItem> cartItemList) {
        double totalPrice = 0.0;
        // Iterate through each CartItem in the cartItemList
        for (CartItem item : cartItemList) {
            // Compute the total price by adding the product of unit price and quantity for each CartItem
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice; // Return the accumulated total price of all items
    }

    private int totalItemDto(Set<CartItemDto> cartItemList) {
        int totalItem = 0;
        for (CartItemDto item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private double totalPriceDto(Set<CartItemDto> cartItemList) {
        double totalPrice = 0;
        for (CartItemDto item : cartItemList) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    private CartItemDto findInDTO(ShoppingCartDto shoppingCart, long productId) {
        // Check if the ShoppingCartDto is null
        if (shoppingCart == null) {
            return null; // If null, return null as no search can be performed
        }

        CartItemDto cartItem = null; // Initialize the CartItemDto as null

        // Iterate through the CartItemDto list within the ShoppingCartDto
        for (CartItemDto item : shoppingCart.getCartItems()) {
            // Check if the product ID of the current item matches the provided product ID
            if (item.getProduct().getId() == productId) {
                cartItem = item; // If a match is found, assign the item to the cartItem variable
            }
        }

        return cartItem; // Return the found CartItemDto, or null if not found
    }

    @Override
    public ShoppingCartDto updateCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity) {
        // Retrieve the list of CartItemDtos from the provided cart DTO
        Set<CartItemDto> cartItemList = cartDto.getCartItems();

        // Find the specific CartItemDto in the cart DTO based on the product ID
        CartItemDto item = findInDTO(cartDto, productDto.getId());

        // Calculate the updated quantity for the specific item
        int itemQuantity = item.getQuantity() + quantity;

        // Calculate the total number of items and total price after update
        int totalItem = totalItemDto(cartItemList);
        double totalPrice = totalPriceDto(cartItemList);

        // Update the quantity of the specific item in the cart DTO
        item.setQuantity(itemQuantity);

        // Update the cart DTO with the modified cart items, total price, and total items
        cartDto.setCartItems(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);

        // Output the updated total items and return the modified cart DTO
        System.out.println(cartDto.getTotalItems());
        return cartDto;
    }


    @Override
    public ShoppingCartDto removeItemFromCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity) {
        // Retrieve the list of CartItemDtos from the provided cart DTO
        Set<CartItemDto> cartItemList = cartDto.getCartItems();

        // Find the specific CartItemDto in the cart DTO based on the product ID
        CartItemDto item = findInDTO(cartDto, productDto.getId());

        // Remove the specific item from the cart DTO
        cartItemList.remove(item);

        // Calculate the total price and total number of items after removal
        double totalPrice = totalPriceDto(cartItemList);
        int totalItem = totalItemDto(cartItemList);

        // Update the cart DTO with the modified cart items, total price, and total items
        cartDto.setCartItems(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);

        // Output the updated total items and return the modified cart DTO
        System.out.println(cartDto.getTotalItems());
        return cartDto;
    }

    @Override
    public ShoppingCart combineCart(ShoppingCartDto cartDto, ShoppingCart cart) {
        // Check if the provided cart is null, and if so, create a new ShoppingCart instance
        if (cart == null) {
            cart = new ShoppingCart();
        }

        // Retrieve the set of CartItems from the provided ShoppingCart instance
        Set<CartItem> cartItems = cart.getCartItems();

        // If the cartItems set is null, create a new empty HashSet
        if (cartItems == null) {
            cartItems = new HashSet<>();
        }

        // Convert the CartItemDtos from the ShoppingCartDto into CartItems and add them to the existing cartItems set
        Set<CartItem> cartItemsDto = convertCartItem(cartDto.getCartItems(), cart);
        for (CartItem cartItem : cartItemsDto) {
            cartItems.add(cartItem);
        }

        // Calculate the total price and total number of items in the combined cart
        double totalPrice = totalPrice(cartItems);
        int totalItems = totalItem(cartItems);

        // Update the ShoppingCart object with the combined cart details
        cart.setTotalItems(totalItems);
        cart.setCartItems(cartItems);
        cart.setTotalPrice(totalPrice);

        return cart; // Return the combined ShoppingCart object
    }

    @Override
    public void deleteCartById(Long id) {
        // Retrieve the ShoppingCart associated with the provided ID
        ShoppingCart shoppingCart = cartRepository.getById(id);

        // Delete each CartItem linked to the ShoppingCart
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            itemRepository.deleteById(cartItem.getId());
        }

        // Clear the ShoppingCart attributes and disassociate it from the customer
        shoppingCart.setCustomer(null);
        shoppingCart.getCartItems().clear();
        shoppingCart.setTotalPrice(0);
        shoppingCart.setTotalItems(0);

        // Save the changes to the ShoppingCart in the repository
        cartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart getCart(String username) {
        // Fetch the customer using the provided username
        Customer customer = customerService.findByUsername(username);

        // Get the ShoppingCart associated with the retrieved customer
        ShoppingCart cart = customer.getCart();

        // Return the ShoppingCart related to the specified user
        return cart;
    }

    private CartItem find(Set<CartItem> cartItems, long productId) {
        // Checks if the cartItems set is null, returns null if so
        if (cartItems == null) {
            return null;
        }

        // Iterates through each CartItem in the set
        for (CartItem item : cartItems) {
            // Compares the ID of the product in the CartItem with the provided productId
            if (item.getProduct().getId() == productId) {
                // Returns the CartItem if the product ID matches
                return item;
            }
        }

        // Returns null if no CartItem with the provided product ID is found in the set
        return null;
    }

    private Product transfer(ProductDto productDto) {
        // Create a new Product instance
        Product product = new Product();

        // Set various attributes of the Product object using details from the ProductDto
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        product.setCostPrice(productDto.getCostPrice());
        product.setSalePrice(productDto.getSalePrice());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImage());
        product.set_activated(productDto.isActivated());
        product.set_deleted(productDto.isDeleted());
        product.setCategory(productDto.getCategory());

        // Return the Product object with details transferred from the ProductDto
        return product;
    }

    private Set<CartItem> convertCartItem(Set<CartItemDto> cartItemDtos, ShoppingCart cart) {
        // Create a new Set to hold the converted CartItem instances
        Set<CartItem> cartItems = new HashSet<>();

        // Iterate over each CartItemDto in the provided Set
        for (CartItemDto cartItemDto : cartItemDtos) {
            // Create a new CartItem and populate its fields from the CartItemDto
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setProduct(transfer(cartItemDto.getProduct()));
            cartItem.setUnitPrice(cartItemDto.getUnitPrice());
            cartItem.setId(cartItemDto.getId());
            cartItem.setCart(cart);

            // Add the new CartItem to the Set
            cartItems.add(cartItem);
        }
        // Return the Set of converted CartItem instances
        return cartItems;
    }
}
