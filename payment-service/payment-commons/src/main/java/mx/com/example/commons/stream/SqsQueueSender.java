package mx.com.example.commons.stream;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.com.example.commons.to.PaymentEventTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.SqsMessageHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

@Component
public class SqsQueueSender {

    static final Logger LOG = LogManager.getLogger(SqsQueueSender.class);
    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Value(value = "${cloud.aws.endpoint.paymentorder.url}")
    private String endPointOrder;

    @Value(value = "${cloud.aws.endpoint.paymentkitchen.url}")
    private String endPointKitchen;

    public void putMessagedToQueue(PaymentEventTO payment){
        // Message for FIFO Queue
        Map<String, Object> headers = new HashMap<>();
        // Message Group ID being set
        headers.put(SqsMessageHeaders.SQS_GROUP_ID_HEADER, "1");
        // Below is optional, since Content based de-duplication is enabled
        ObjectMapper mapper = new ObjectMapper();
        String sendPayment;
        try {
            sendPayment = mapper.writeValueAsString(payment);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        LOG.info("SENDORDER " + sendPayment);
        queueMessagingTemplate.send(endPointOrder,
                MessageBuilder.withPayload(sendPayment).copyHeaders(headers).build());
        LOG.info("SENDKITCHEN " + sendPayment);
        queueMessagingTemplate.send(endPointKitchen,
                MessageBuilder.withPayload(sendPayment).copyHeaders(headers).build());
    }

}
