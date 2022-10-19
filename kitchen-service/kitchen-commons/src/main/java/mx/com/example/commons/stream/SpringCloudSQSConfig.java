package mx.com.example.commons.stream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SpringCloudSQSConfig {
    @Value(value = "${cloud.aws.region.static}")
	private String region;

	@Value(value = "${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value(value = "${cloud.aws.credentials.secret-key}")
	private String secretKey;

	@Bean
	public QueueMessagingTemplate queueMessagingTemplate() {
		return new QueueMessagingTemplate(amazonSQSAsync());
	}

	public AmazonSQSAsync amazonSQSAsync() {

		AmazonSQSAsync amazonSQSAsync = null;

		AmazonSQSAsyncClientBuilder amazonSQSAsyncClientBuilder = AmazonSQSAsyncClientBuilder.standard();
		amazonSQSAsyncClientBuilder.withRegion("us-east-1");

		BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
		amazonSQSAsyncClientBuilder.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials));

		amazonSQSAsync = amazonSQSAsyncClientBuilder.build();

		return amazonSQSAsync;

	}

	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory() {
		SimpleMessageListenerContainerFactory msgListenerContainerFactory = new SimpleMessageListenerContainerFactory();
		msgListenerContainerFactory.setAmazonSqs(amazonSQSAsync());
		return msgListenerContainerFactory;
	}

	@Bean
	public QueueMessageHandler queueMessageHandler() {
		QueueMessageHandlerFactory queueMsgHandlerFactory = new QueueMessageHandlerFactory();
		queueMsgHandlerFactory.setAmazonSqs(amazonSQSAsync());
		QueueMessageHandler queueMessageHandler = queueMsgHandlerFactory.createQueueMessageHandler();
		List<HandlerMethodArgumentResolver> list = new ArrayList<>();
		HandlerMethodArgumentResolver resolver = new PayloadArgumentResolver(new MappingJackson2MessageConverter());
		list.add(resolver);
		queueMessageHandler.setArgumentResolvers(list);
		return queueMessageHandler;
	}
}
