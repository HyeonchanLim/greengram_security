package com.green.greengram.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

//@Component
@Configuration // WebMvcConfigurer 이걸 통해서 bean 되는데 없다면 메소드에 @Bean 을 써서 bean 등록 해줘야함
// 메소드에서 bean 사용 -> 리턴 받은걸 싱글톤으로 만들어줌
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final String uploadPath;
    // final 에 값 넣을려면 생성자 or 명시적 ( = ?? ) 으로 값 입력

    public WebMvcConfiguration(@Value("${file.directory}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

//    @Bean
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/pic/**")
                .addResourceLocations("file:" + uploadPath + "/");


        // frontend 새로고침 -> 화면이 나타날 수 있도록 작업
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/**") // classpath 가 리소스임
                .resourceChain(true)
                // 익명 클래스 -> 바로 객체화 (클래스 생성 - 상속 절차 전부 패스)
                .addResolver(new PathResourceResolver()  {
                    @Override
                    protected Resource getResource(String resourcePath , Resource location) throws IOException {
                        Resource resource = location.createRelative(resourcePath);

                        if (resource.exists() && resource.isReadable()){
                        return resource;
                    }
                        // /feed 요청이 false 발생 -> 아래의 리소스 경로 참고
                        // 캐시 기능이 있어서 한번 더 요청한다고 또 실행되지는 않음

                        return new ClassPathResource("/static/index.html");
                    }
        })
        ;

    }
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer){
        configurer.addPathPrefix("api", HandlerTypePredicate.forAnnotation(RestController.class));
    }


}