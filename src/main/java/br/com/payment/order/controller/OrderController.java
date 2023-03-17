package br.com.payment.order.controller;

import br.com.payment.order.dto.OrderDto;
import br.com.payment.order.dto.StatusDto;
import br.com.payment.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

        @Autowired
        private OrderService orderService;

        @GetMapping()
        public List<OrderDto> listAll() {
            return orderService.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<OrderDto> getById(@PathVariable @NotNull Long id) {
            OrderDto dto = orderService.getById(id);

            return  ResponseEntity.ok(dto);
        }

        @PostMapping()
        public ResponseEntity<OrderDto> makeOrder(@RequestBody @Valid OrderDto dto, UriComponentsBuilder uriBuilder) {
            OrderDto orderPlaced = orderService.createOrder(dto);

            URI path = uriBuilder.path("/orders/{id}").buildAndExpand(orderPlaced.getId()).toUri();

            return ResponseEntity.created(path).body(orderPlaced);
        }

        @PutMapping("/{id}/status")
        public ResponseEntity<OrderDto> updateStatus(@PathVariable Long id, @RequestBody StatusDto status){
           OrderDto dto = orderService.updateStatus(id, status);

            return ResponseEntity.ok(dto);
        }

        @PutMapping("/{id}/paid-out")
        public ResponseEntity<Void> approvePayment(@PathVariable @NotNull Long id) {
            orderService.approveOrderPayment(id);

            return ResponseEntity.ok().build();
        }
}
