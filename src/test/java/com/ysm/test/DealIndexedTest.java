package com.ysm.test;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ysm.AppConfig;
import com.ysm.service.DealService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
public class DealIndexedTest {
	
	@Autowired
	private DealService dealService;
	
	
	@Test
	public void indexedTest() {
		int numIndexed = dealService.indexedDeals();
		
		assertEquals(numIndexed, 4000);
	}

	@Test
	public void searchTest() {
		Set<String> hashtags = dealService.searchHashtag("뷰티스테이션 방송상품");

		hashtags.forEach(System.out::println);
		line();
		
		Set<String> hashtags2 = dealService.searchHashtag("No.611 메리 체크셔츠 #02");
		
		hashtags2.forEach(System.out::println);
		line();
		
		Set<String> hashtags3 = dealService.searchHashtag("Super Definition Crayon");
		
		hashtags3.forEach(System.out::println);
		line();
		
	}

	private void line() {
		System.out.println("=============================================");
	}
	
}