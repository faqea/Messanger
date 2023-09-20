package messenger.proj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import messenger.proj.models.User;
import messenger.proj.models.message;

@Configuration
public class RedisConfig {

	 @Bean
	    public RedisConnectionFactory redisConnectionFactory() {
	        return new LettuceConnectionFactory("localhost", 6379); // Измените хост и порт на соответствующие параметры вашего Redis сервера
	    }

	    @Bean
	    public RedisTemplate<String, message> redisTemplate() {
	        RedisTemplate<String, message> template = new RedisTemplate<>();
	        template.setConnectionFactory(redisConnectionFactory());
	        template.setKeySerializer(new StringRedisSerializer());
	        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(message.class)); // Используйте сериализатор для вашего класса Message
	        return template;
	    }
	    
	    @Bean
	    public RedisTemplate<String, User> redisTemplate2() {
	        RedisTemplate<String, User> template = new RedisTemplate<>();
	        template.setConnectionFactory(redisConnectionFactory());
	        template.setKeySerializer(new StringRedisSerializer());
	        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class)); // Используйте сериализатор для вашего класса Message
	        return template;
	    }
}

