package com.bank.accountservice.common.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bank.accountservice.common.Constants;

/**
 * Created By Shameera.A on 11/2/2022
 */

@Configuration
public class MessageConfig {

	@Bean
	public Queue accountQueue() {
		return new Queue(Constants.ACCOUNT_QUEUE);
	}

	@Bean
	public Queue transactionQueue() {
		return new Queue(Constants.TRANSACTION_QUEUE);
	}
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(Constants.EXCHANGE);
	}

	@Bean
	public Binding bindingAccount(@Qualifier("accountQueue") Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(Constants.ACCOUNT_ROUTING_KEY);
	}

	@Bean
	public Binding bindingTransaction(@Qualifier("transactionQueue") Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(Constants.TRANSACTION_ROUTING_KEY);
	}

	@Bean
	public MessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public AmqpTemplate template(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(converter());
		return rabbitTemplate;
	}
}
