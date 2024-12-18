package vn.luanvan.ktpm.service;

import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.parser.node.FilterNode;
import com.turkraft.springfilter.parser.node.InputNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.luanvan.ktpm.domain.Item;
import vn.luanvan.ktpm.domain.Order;
import vn.luanvan.ktpm.domain.Product;
import vn.luanvan.ktpm.domain.response.ResultPaginationDTO;
import vn.luanvan.ktpm.domain.response.reviews.ResReviewsDTO;
import vn.luanvan.ktpm.repository.ItemRepository;
import vn.luanvan.ktpm.repository.OrderRepository;
import vn.luanvan.ktpm.repository.ProductRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ProductRepository productRepository;


    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
    }

    public Order create(Order order){
        Order orderDB = this.orderRepository.save(order);
        if (order.getItems() != null) {
            orderDB.setCode(String.valueOf("COD"+System.currentTimeMillis()));

            List<Long> itemList = order.getItems().stream().map(
                    Item::getId
            ).collect(Collectors.toList());

            List<Item> itemListDB = this.itemRepository.findByIdIn(itemList);

            List<Product> productList = itemListDB.stream().map(item -> {
                Product product = item.getProduct();
                product.setSold(item.getQuantity() + product.getSold());
                return product; 
            }).toList();

            this.productRepository.saveAll(productList);

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

    public long countByStatus(String status) {
        return this.orderRepository.countByStatus(status);
    }

    public Page<Order> convertListToPage(List<Order> list, Pageable pageable) {
        int total = list.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);

        List<Order> pagedList = list.subList(start, end);
        return new PageImpl<>(pagedList, pageable, total);
    }


    public ResultPaginationDTO findAll(Specification<Order> spec, Pageable pageable) {
        Page<Order> orderPage  = this.orderRepository.findAll(spec, pageable);
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

    public List<Order> findAllByMonth(String month, String year) {
        return orderRepository.findAllByCreatedAtMonthAndYear(Integer.parseInt(month), Integer.parseInt(year));
    }

    public List<Order> getOrdersByDate(int day, int month, int year, int minus) {
        LocalDate date = LocalDate.of(year, month, day);
        LocalDate newDate = date.minusDays(minus);
        List<Order> orderList = orderRepository.findAllByCreatedAt(newDate);
        orderList = orderList.stream().filter(item -> Objects.equals(item.getStatus(), "Hoàn thành")).toList();
        return orderList;
    }

    public Order updateStatus(Order order) {
        Order orderDB = this.findById(order.getId());
        orderDB.setStatus(order.getStatus());
        return this.orderRepository.save(orderDB);
    }
}
