package vn.luanvan.ktpm.service;

import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Item;
import vn.luanvan.ktpm.repository.ItemRepository;

import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(Item item) {
        return this.itemRepository.save(item);
    }

    public Item findById(long id) {
        Optional<Item> itemOptional = this.itemRepository.findById(id);
        return itemOptional.orElse(null);
    }


}
