//package com.gachi.gachipay.common.configuration;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//
//@Configuration
//@EnableCaching //캐싱 기능 사용 등록
//public class RedisConfig {
//    @Value("${spring.data.redis.host}")
//    private String host;
//
//    @Value("${spring.data.redis.port}")
//    private int port;
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(host, port);
//    }
//
////    @Bean
////    public CacheManager cacheManager() {
////        RedisCacheManager.RedisCacheManagerBuilder builder =
////                RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());
////
////        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
////                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
////                .disableCachingNullValues() //데이터가 null 이면 캐싱하지 않음
////                .entryTtl(Duration.ofMinutes(30L)); //유효기간
////
////        builder.cacheDefaults(configuration);
////
////        return builder.build();
////    }
//}
