/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedOrderActionException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class OrderController {

	// TODO authority annotation

    @Autowired
    private OrderService orderService;

    // TODO - getAllBorrowOrders (@Mappings, URI=/orders, and method) + filter by expiry and paginate
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllBorrowOrders(@RequestParam(required = false) LocalDate expiry,
                                                          Pageable paginate) {

        List<Order> orders;
        if (expiry != null) {
            orders = orderService.findByExpiryDate(expiry, paginate);
        } else {
            orders = orderService.findAllOrders(paginate);
        }
        return ResponseEntity.ok(orders);
    }

    // TODO - getBorrowOrder (@Mappings, URI=/orders/{id}, and method)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders/{id}")
    public ResponseEntity<EntityModel<Order>> getBorrowOrder(@PathVariable Long id) throws OrderNotFoundException{
        Order order = orderService.findOrder(id);

        EntityModel<Order> resource = EntityModel.of(order);
        resource.add(linkTo(methodOn(OrderController.class).getBorrowOrder(id)).withSelfRel());
        resource.add(linkTo(methodOn(OrderController.class).getAllBorrowOrders(null, Pageable.unpaged())).withRel("all-orders"));

        return ResponseEntity.ok(resource);
    }

    // TODO - updateOrder (@Mappings, URI=/orders/{id}, and method)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id,
                                             @RequestBody Order order) throws OrderNotFoundException {
        Order updatedOrder = orderService.updateOrder(order, id);
        return ResponseEntity.ok(updatedOrder);
    }

    // TODO - deleteBookOrder (@Mappings, URI=/orders/{id}, and method)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteBookOrder(@PathVariable Long id) throws OrderNotFoundException {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

}
