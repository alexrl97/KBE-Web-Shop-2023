package com.kbe.web_shop.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.kbe.web_shop.model.Order;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.OrderRepo;
import com.kbe.web_shop.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class OrderRepoIntegrationTest {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void testFindAllByUserOrderByCreatedDateDesc() {
        User user = new User();
        userRepo.save(user);

        Order order1 = new Order();
        order1.setUser(user);
        order1.setCreatedDate(new Date(System.currentTimeMillis() - 10000));
        orderRepo.save(order1);

        Order order2 = new Order();
        order2.setUser(user);
        order2.setCreatedDate(new Date(System.currentTimeMillis() - 5000));
        orderRepo.save(order2);

        Order order3 = new Order();
        order3.setUser(user);
        order3.setCreatedDate(new Date());
        orderRepo.save(order3);

        List<Order> orders = orderRepo.findAllByUserOrderByCreatedDateDesc(user);

        assertNotNull(orders);
        assertEquals(3, orders.size());
        assertEquals(order3, orders.get(0));
        assertEquals(order2, orders.get(1));
        assertEquals(order1, orders.get(2));
    }

    @Test
    public void testFindAllByOrderByCreatedDateDesc() {
        User user1 = new User();
        userRepo.save(user1);

        User user2 = new User();
        userRepo.save(user2);

        Order order1 = new Order();
        order1.setUser(user1);
        order1.setCreatedDate(new Date(System.currentTimeMillis() - 10000));
        orderRepo.save(order1);

        Order order2 = new Order();
        order2.setUser(user2);
        order2.setCreatedDate(new Date(System.currentTimeMillis() - 5000));
        orderRepo.save(order2);

        Order order3 = new Order();
        order3.setUser(user1);
        order3.setCreatedDate(new Date());
        orderRepo.save(order3);

        List<Order> orders = orderRepo.findAllByOrderByCreatedDateDesc();

        assertNotNull(orders);
        assertEquals(order3, orders.get(0));
        assertEquals(order2, orders.get(1));
        assertEquals(order1, orders.get(2));
    }
}
