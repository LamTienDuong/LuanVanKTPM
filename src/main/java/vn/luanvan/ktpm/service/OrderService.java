package vn.luanvan.ktpm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Item;
import vn.luanvan.ktpm.domain.Order;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.domain.response.reviews.ResReviewsDTO;
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
            orderDB.setCode(String.valueOf("COD"+System.currentTimeMillis()));

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

    public Page<Order> convertListToPage(List<Order> list, Pageable pageable) {
        int total = list.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);

        List<Order> pagedList = list.subList(start, end);
        return new PageImpl<>(pagedList, pageable, total);
    }

    public ResultPaginationDTO findOrderByUserId(long id, Pageable pageable) {
        List<Order> orderList = this.orderRepository.findByUserId(id);

        Page<Order> orderPage = this.convertListToPage(orderList, pageable);

        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(orderPage.getTotalPages());
        mt.setTotal(orderPage.getTotalElements());

        res.setMeta(mt);
        res.setResult(orderPage.getContent());

        return res;
    }
}
