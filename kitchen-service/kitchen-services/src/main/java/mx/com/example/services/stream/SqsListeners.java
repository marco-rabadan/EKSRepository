package mx.com.example.services.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.com.example.commons.to.OrderEventTO;
import mx.com.example.commons.to.PaymentEventTO;
import mx.com.example.services.facade.IKitchenFacade;
import mx.com.example.services.facade.impl.KitchenFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class SqsListeners {
    static final Logger LOG = LogManager.getLogger(SqsListeners.class);

    @Autowired
    private IKitchenFacade kitchenFacade;

    // @KafkaListener(topics = "order_events", groupId = "kitchen")
    @SqsListener(value = "${cloud.aws.endpoint.order.name}")
    public void orderEvents(String message) throws JsonProcessingException {

        OrderEventTO order = new ObjectMapper().readValue(message, OrderEventTO.class);

        LOG.info("Received Message Orders");
        LOG.info("UUID" + order.getUuid());
        LOG.info("Description" + order.getDescription());
        LOG.info("Quantity" + order.getQuantity());
        LOG.info("Time" + order.getDateTime());

        kitchenFacade.createTicket(order);
    }

    //@KafkaListener(topics = "payment_events", groupId = "kitchen")
    @SqsListener(value = "${cloud.aws.endpoint.payment.name}")
    public void paymentEvents(String message) throws JsonProcessingException {

        PaymentEventTO payment = new ObjectMapper().readValue(message, PaymentEventTO.class);

        LOG.info("Received Message Payment");
        LOG.info("UUID" + payment.getUuid());
        LOG.info("Description" + payment.getDescription());
        LOG.info("ConfirmCode" + payment.getComfirmCode());
        LOG.info("Time" + payment.getDateTime());
        LOG.info("Status" + payment.getStatus());

        kitchenFacade.approveTicket(payment);
    }
}
