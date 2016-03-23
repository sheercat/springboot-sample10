package net.vg4;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.val;
import net.vg4.domain.Customer;
import net.vg4.repository.CustomerRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest({ "server.port:0", "spring.datasource.url:jdbc:h2:mem:bookmark;DB_CLOSE_ON_EXIT=FALSE" }) // (1)
@val
public class AppTests {
	@Autowired
	CustomerRepository customerRepository; // (2)
	@Value("${local.server.port}")
	int port;
	String apiEndpoint;
	RestTemplate restTemplate = new TestRestTemplate();
	Customer customer1;
	Customer customer2;

	// (3)
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class Page<T> {
		private List<T> content;
		private int numberOfElements;
	}

	// (4)
	@Before
	public void setUp() {
		customerRepository.deleteAll();
		customer1 = new Customer();
		customer1.setFirstName("Taro");
		customer1.setLastName("Yamada");
		customer2 = new Customer();
		customer2.setFirstName("Ichiro");
		customer2.setLastName("Suzuki");

		customerRepository.save(Arrays.asList(customer1, customer2));
		apiEndpoint = "http://localhost:" + port + "/api/customers";
	}

	// (5)
	@Test
	public void testGetCustomers() throws Exception {
		val response = restTemplate.exchange(apiEndpoint, HttpMethod.GET,
				null /* body,header */, new ParameterizedTypeReference<Page<Customer>>() {
				}); // (6)
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getNumberOfElements(), is(2));

		val c1 = response.getBody().getContent().get(0);
		assertThat(c1.getId(), is(customer2.getId()));
		assertThat(c1.getFirstName(), is(customer2.getFirstName()));
		assertThat(c1.getLastName(), is(customer2.getLastName()));

		val c2 = response.getBody().getContent().get(1);
		assertThat(c2.getId(), is(customer1.getId()));
		assertThat(c2.getFirstName(), is(customer1.getFirstName()));
		assertThat(c2.getLastName(), is(customer1.getLastName()));
	}

	// (7)
	@Test
	public void testPostCustomers() throws Exception {
		val customer3 = new Customer();
		customer3.setFirstName("Nobita");
		customer3.setLastName("Nobi");

		val response = restTemplate.exchange(apiEndpoint, HttpMethod.POST, new HttpEntity<>(customer3) /* (8) */,
				Customer.class);
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		val customer = response.getBody();
		assertThat(customer.getId(), is(notNullValue()));
		assertThat(customer.getFirstName(), is(customer3.getFirstName()));
		assertThat(customer.getLastName(), is(customer3.getLastName()));

		assertThat(restTemplate
				.exchange(apiEndpoint, HttpMethod.GET, null, new ParameterizedTypeReference<Page<Customer>>() {
				}).getBody().getNumberOfElements(), is(3));
	}

	// (9)
	@Test
	public void testDeleteCustomers() throws Exception {
		val response = restTemplate.exchange(apiEndpoint + "/{id}" /* (9) */, HttpMethod.DELETE,
				null /* body,header */, Void.class, Collections.singletonMap("id", customer1.getId()));

		assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
		assertThat(restTemplate
				.exchange(apiEndpoint, HttpMethod.GET, null, new ParameterizedTypeReference<Page<Customer>>() {
				}).getBody().getNumberOfElements(), is(1));
	}
}
