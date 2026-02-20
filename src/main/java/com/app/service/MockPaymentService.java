package com.app.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.dto.MockPaymentRecordRequest;
import com.app.exceptions.UserException;
import com.app.pojos.*;
import com.app.repository.MockPaymentTransactionRepository;
import com.app.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MockPaymentService {

    private final MockPaymentTransactionRepository txnRepo;
    private final OrderRepository orderRepo;
    private final UserService userService; // must provide findUserProfileByJwt(jwt)

    @Transactional
    public MockPaymentTransaction record(String jwt, MockPaymentRecordRequest req) throws UserException{
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderRepo.findById(req.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Order does not belong to user");
        }

        MockPaymentTransaction txn = new MockPaymentTransaction();
        txn.setUser(user);
        txn.setOrder(order);
        txn.setPaymentMethod(req.getPaymentMethod());
        txn.setStatus(req.getStatus());
        txn.setPaymentId(req.getPaymentId());
        txn.setAmount(req.getAmount());
        txn.setCurrency(req.getCurrency());
        txn.setGatewayOrderId(req.getGatewayOrderId());
        txn.setGatewayPaymentId(req.getGatewayPaymentId());
        txn.setGatewaySignature(req.getGatewaySignature());
        txn.setMessage(req.getMessage());

        // update embedded payment details on order
        PaymentDetails pd = order.getPaymentDetails() == null ? new PaymentDetails() : order.getPaymentDetails();
        pd.setPaymentMethod(req.getPaymentMethod());
        pd.setStatus(req.getStatus());
        pd.setPaymentId(req.getPaymentId());
        order.setPaymentDetails(pd);

        // optional: order status mapping
        if (req.getStatus() == PaymentStatus.SUCCESS) {
            order.setOrderStatus(OrderStatus.CONFIRMED);
        } else if (req.getStatus() == PaymentStatus.FAILED || req.getStatus() == PaymentStatus.CANCELLED) {
            order.setOrderStatus(OrderStatus.CANCELLED);
        }

        orderRepo.save(order);
        return txnRepo.save(txn);
    }

    @Transactional(readOnly = true)
    public List<MockPaymentTransaction> myPayments(String jwt) throws UserException{
        User user = userService.findUserProfileByJwt(jwt);
        return txnRepo.findByUserIdOrderByCreatedAtDesc(user.getId());
    }
}
