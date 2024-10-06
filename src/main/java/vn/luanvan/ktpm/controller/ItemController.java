package vn.luanvan.ktpm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.luanvan.ktpm.domain.Item;
import vn.luanvan.ktpm.service.ItemService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.CustomizeException;

@RestController
@RequestMapping("/api/v1")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/items")
    @ApiMessage("Create a item")
    public ResponseEntity<Item> create(@RequestBody Item item) {
        Item itemDB = this.itemService.create(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemDB);
    }

    @GetMapping("/items/{id}")
    @ApiMessage("Get item by id")
    public ResponseEntity<Item> findById(@PathVariable long id) throws CustomizeException {
        Item item = this.itemService.findById(id);
        if (item == null) {
            throw new CustomizeException("Item voi id = " + id + " khong ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(item);
    }
}
