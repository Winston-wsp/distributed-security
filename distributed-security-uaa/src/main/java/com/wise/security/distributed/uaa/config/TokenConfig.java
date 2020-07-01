package com.wise.security.distributed.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author: Winston
 * @createTime: 2020/7/1
 */
@Configuration
public class TokenConfig {

    @Bean
    public TokenStore tokenStore(){
        // 使用内存存储令牌（普通令牌）
        return new InMemoryTokenStore();
    }

}
