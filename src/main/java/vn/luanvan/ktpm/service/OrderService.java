package vn.luanvan.ktpm.service;

import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Item;
import vn.luanvan.ktpm.domain.Order;
import vn.luanvan.ktpm.repository.ItemRepository;
import vn.luanvan.ktpm.repository.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;


    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    public Order create(Order order){
        Order orderDB = this.orderRepository.save(order);
        if (order.getItems() != null) {
            List<Long> itemList = order.getItems().stream().map(
                    Item::getId
            ).collect(Collectors.toList());

            List<Item> itemListDB = this.itemRepository.findByIdIn(itemList);

            itemListDB.forEach(item -> item.setOrder(order));
            this.itemRepository.saveAll(itemListDB);

            orderDB.setItems(itemListDB);
            this.orderRepository.save(order);
        }
        return orderDB;
    }

    public Order findById(long id) {
        Optional<Order> orderOptional = this.orderRepository.findById(id);
        return orderOptional.orElse(null);
    }
}
