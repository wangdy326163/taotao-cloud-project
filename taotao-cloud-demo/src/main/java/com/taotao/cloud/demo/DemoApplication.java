package com.taotao.cloud.demo;

import com.taotao.cloud.dingtalk.annatations.DingerScan;
import com.taotao.cloud.dingtalk.annatations.EnableMultiDinger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMultiDinger
@DingerScan(basePackages = "com.taotao.cloud.sys.biz.dingtalk")
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
