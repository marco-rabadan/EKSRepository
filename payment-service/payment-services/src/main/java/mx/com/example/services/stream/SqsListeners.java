package mx.com.example.services.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.com.example.commons.to.TicketEventTO;
import mx.com.example.services.facade.IPaymentFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class SqsListeners {

    static final Logger LOG = LogManager.getLogger(SqsListeners.class);

    @Autowired
    private IPaymentFacade paymentFacade;

    @SqsListener("ticket_events.fifo")
    public void ticketEvents(String message) throws JsonProcessingException {

        TicketEventTO ticket = new ObjectMapper().readValue(message, TicketEventTO.class);

        LOG.info("Received Message Ticket");
        LOG.info("UUID" + ticket.getUuid());
        LOG.info("Description" + ticket.getDescription());
        LOG.info("Quantity" + ticket.getQuantity());
        LOG.info("Time" + ticket.getDateTime());
        LOG.info("Price" + ticket.getPrice());
        LOG.info("Status" + ticket.getStatus());

        paymentFacade.authorizeCard(ticket);
    }
}
