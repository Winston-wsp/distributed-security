package com.wise.security.distributed.uaa.config;

import javafx.scene.input.ScrollEvent;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author: Winston
 * @createTime: 2020/7/1
 */
@EnableAuthorizationServer
@Configuration
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Override
    // 客户端的详情服务
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() //使用in-memory存储
        .withClient("c1") // client_id
        .secret(new BCryptPasswordEncoder().encode("secret")) // 客户端秘钥
        .resourceIds("res1")//资源列表
                //该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
        .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")
        .scopes("all")// 允许的授权范围
        .autoApprove(false)
        // 加上验证回调地址
        .redirectUris(("http:www.baidu.com"));
    }

    // 令牌管理服务
    @Bean
    public AuthorizationServerTokenServices tokenService(){
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService); // 客户端详情服务
        services.setSupportRefreshToken(true); // 支持刷新令牌
        services.setTokenStore(tokenStore); // 令牌存储策略
        services.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        services.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return services;
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        // 设置授权码模式的授权码
        return new InMemoryAuthorizationCodeServices();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager) // 认证管理器
                .authorizationCodeServices(authorizationCodeServices) // 授权码服务
                .tokenServices(tokenService()) // 令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()") // oauth/token_key是公开
                .checkTokenAccess("permitAll()")// oauth/check_key是公开
                .allowFormAuthenticationForClients(); // 表单认证（申请令牌）
    }
}
