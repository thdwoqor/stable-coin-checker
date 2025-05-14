package org.example.stablecoinchecker.global.config;

import java.math.BigDecimal;
import org.example.stablecoinchecker.chart.domain.Identifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<Identifier, BigDecimal> priceRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Identifier, BigDecimal> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer<Identifier> keySerializer =
                new Jackson2JsonRedisSerializer<>(Identifier.class);

        template.setKeySerializer(keySerializer);

        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, Identifier> indexRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Identifier> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        template.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<Identifier> valueSerializer =
                new Jackson2JsonRedisSerializer<>(Identifier.class);

        template.setValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }

}
