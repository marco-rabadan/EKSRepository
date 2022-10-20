package mx.com.example.commons.stream;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import mx.com.example.commons.to.OrderEventTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.SqsMessageHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import java.util.HashMap;
import java.util.Map;

@Component
public class SqsQueueSender {
    static final Logger LOG = LogManager.getLogger(SqsQueueSender.class);
    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Value(value = "${cloud.aws.endpoint.order.url}")
    private String endPoint;

    public void putMessagedToQueue(OrderEventTO order){
        // Message for FIFO Queue
        Map<String, Object> headers = new HashMap<>();
        // Message Group ID being set
        headers.put(SqsMessageHeaders.SQS_GROUP_ID_HEADER, "1");
        headers.put("contentType",  MimeType.valueOf("application/json"));
        // Below is optional, since Content based de-duplication is enabled
        //headers.put(SqsMessageHeaders.SQS_DEDUPLICATION_ID_HEADER, "2");
        LOG.info("PUTMESSAGEJSON " + order);
        LOG.info("JSONTYPEMIME " + order.toString());

        queueMessagingTemplate.send(endPoint,
                MessageBuilder.withPayload(order).copyHeaders(headers).build());
    }

}
