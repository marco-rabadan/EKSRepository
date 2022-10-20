package mx.com.example.services.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.com.example.commons.to.PaymentEventTO;
import mx.com.example.services.facade.impl.OrderFacade;
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
    private OrderFacade orderFacade;

    @SqsListener(value = "${cloud.aws.endpoint.payment.name}")
    public void listenGroupFoo(String message) throws JsonProcessingException {

        PaymentEventTO payment = new ObjectMapper().readValue(message, PaymentEventTO.class);

        LOG.info("Received Message Payment");
        LOG.info("UUID" + payment.getUuid());
        LOG.info("Description" + payment.getDescription());
        LOG.info("ConfirmCode" + payment.getComfirmCode());
        LOG.info("Time" + payment.getDateTime());
        LOG.info("Status" + payment.getStatus());

        orderFacade.approveOrder(payment);
    }
}
