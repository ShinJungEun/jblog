package com.douzone.jblog.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import com.douzone.jblog.config.app.DBConfig;
import com.douzone.jblog.config.app.MyBatisConfig;

// 여러 설정들을 모으는 클래스

@Configuration
@EnableAspectJAutoProxy			// @Aspect 들을 proxy(AOP)
@ComponentScan({"com.douzone.jblog.service", "com.douzone.jblog.repository"})
@Import({DBConfig.class, MyBatisConfig.class})		// app config class들을 import함
public class AppConfig {

}
