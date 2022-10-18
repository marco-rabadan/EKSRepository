package mx.com.example.commons.stream;

import mx.com.example.commons.to.OrderEventTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.SqsMessageHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;

public class SqsQueueSender {

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    private String endPoint="https://sqs.us-east-1.amazonaws.com/262583979852/ticket_events.fifo";
    public void putMessagedToQueue(OrderEventTO order){
        // Message for FIFO Queue
        Map<String, Object> headers = new HashMap<>();
        // Message Group ID being set
        headers.put(SqsMessageHeaders.SQS_GROUP_ID_HEADER, "1");
        // Below is optional, since Content based de-duplication is enabled
        //headers.put(SqsMessageHeaders.SQS_DEDUPLICATION_ID_HEADER, "2");
        queueMessagingTemplate.send(endPoint,
                MessageBuilder.withPayload(order).copyHeaders(headers).build());
    }

}
