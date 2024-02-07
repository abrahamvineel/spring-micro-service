package com.pro.productservice;

import com.pro.productservice.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import java.math.BigDecimal;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductserviceApplicationTests {

	@Container
	private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@DynamicPropertySource
	private static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws JsonProcessingException {
		ProductRequest productRequest = getProductRequest();
		objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
							.name("iPhone 13")
							.description("iPhone 13")
							.price(BigDecimal.valueOf(1200))
							.build();
	}
}
