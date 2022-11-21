package wind.common.config;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wind.common.config.properties.ExcludeUrlProperties;
import wind.common.config.properties.SecurityProperties;
import wind.common.utils.spring.SpringUtils;

/**
 * sa-token 配置
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;

    /**
     * 注册sa-token的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        securityProperties.setExcludes(new String[]{"/business/member/login", "/business/member/getRedirectUrl"});
        // 注册路由拦截器，自定义验证规则
        registry.addInterceptor(new SaRouteInterceptor((request, response, handler) -> {
            ExcludeUrlProperties excludeUrlProperties = SpringUtils.getBean(ExcludeUrlProperties.class);
            // 登录验证 -- 排除多个路径
            // 检查是否登录 是否有token
            SaRouter
                // 获取所有的
                .match("/**")
                // 排除下不需要拦截的
                .notMatch(securityProperties.getExcludes())
                .notMatch(excludeUrlProperties.getExcludes())
                // 对未排除的路径进行检查
                .check(StpUtil::checkLogin);
        })).addPathPatterns("/**");
        registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public StpLogic getStpLogicJwt() {
        // Sa-Token 整合 jwt (简单模式)
        return new StpLogicJwtForSimple();
    }

}
