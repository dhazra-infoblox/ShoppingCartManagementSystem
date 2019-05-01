package com.dibya.shoppingcart.models;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
public class ItemJPARepository {

	@Autowired
	private ItemRepository itemRepository;
	
	@ApiOperation(value = "view cart", nickname = "viewCart", notes = "", response = Item.class, responseContainer = "List", tags={ "Shopping Cart Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Item.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid status value") })
    @RequestMapping(value = "/shoppingcart/cart",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
	public List<Item> viewCart() {
		return itemRepository.findAll();
	}

	@ApiOperation(value = "view item by id", nickname = "viewItem", notes = "", response = Item.class, responseContainer = "List", tags={ "Shopping Cart Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Item.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid status value") })
    @RequestMapping(value = "/shoppingcart/items/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
	public Resource<Item> viewItem(@PathVariable int id) {
		Optional<Item> item = itemRepository.findById(id);
		
		if(!item.isPresent())
			throw new ItemNotFoundException("id-"+ id);
		
		Item item1 = item.get();
		Resource<Item> resource = new Resource<Item>(item1);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).viewCart());

		resource.add(linkTo.withRel("all-items"));

		// HATEOAS

		return resource;
		}

	@ApiOperation(value = "Deletes an Item", nickname = "deleteItem", notes = "", tags={ "Shopping Cart Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Item deleted"),
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Item not found") })
    @RequestMapping(value = "/shoppingcart/items/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
	public void deleteItem(@PathVariable int id) {
		itemRepository.deleteById(id);
			
	}
	
	@ApiOperation(value = "Add a new item to the cart", nickname = "addItem", notes = "", response = Item.class, tags={ "Shopping Cart Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Added to the Cart", response = Item.class),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/shoppingcart/items",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
	public ResponseEntity<Object> addItem(@Valid @RequestBody Item item) {
		Item savedItem = itemRepository.save(item);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedItem.getItemId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
	
	@ApiOperation(value = "Update item quantity", nickname = "updateItemQuantity", notes = "", response = Item.class, tags={ "Shopping Cart Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Item Quantity updated in the Cart", response = Item.class),
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Item not found"),
        @ApiResponse(code = 405, message = "Validation exception") })
    @RequestMapping(value = "/shoppingcart/items",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
	public Resource<Item> updateItemQuantity(@Valid @RequestBody Item item) {
		
		Optional<Item> item1 = itemRepository.findById(item.getItemId());
		if(!item1.isPresent())
			throw new ItemNotFoundException("id: "+item.getItemId());
		Item item2 = item1.get();
		item2.setQuantity(item.getQuantity());
		itemRepository.save(item2);
		Resource<Item> resource = new Resource<Item>(item2);
		return resource;
	}
	
	@ApiOperation(value = "Clear Cart", nickname = "clearCart", notes = "", tags={ "Shopping Cart Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Cart Cleared"),
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Item not found") })
    @RequestMapping(value = "/shoppingcart/cart",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
	public void clearCart() {
		itemRepository.deleteAll();	
	}
	
	@ApiOperation(value = "Place the order", nickname = "placeOrder", notes = "", response = Item.class, responseContainer = "List", tags={ "Shopping Cart Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Item.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Item not found"),
        @ApiResponse(code = 405, message = "Validation exception") })
    @RequestMapping(value = "/shoppingcart/cart",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
	public Resource<String> placeOrder() {
		List<Item> itemList = itemRepository.findAll();
		Iterator<Item> itemListIterator = itemList.iterator();
		double totalPrice = 0.0; 
		while (itemListIterator.hasNext()) {
			Item item = itemListIterator.next();
			totalPrice += (item.getPrice() * item.getQuantity());
		}
		itemRepository.deleteAll();
		String total = "Order is placed. Total Cart Value is "+totalPrice;
		Resource<String> resource = new Resource<String>(total);
		return resource;
	}
	
	
}
