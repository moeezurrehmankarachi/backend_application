package com.task.backend;

import com.task.backend.domainobject.ExternalService;
import com.task.backend.service.ExternalServiceService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UnitTests {
	@Autowired
	private ExternalServiceService externalServiceService;

	@ParameterizedTest
	@MethodSource("sendGetRequestTestParameters")
	void sendGetRequestTest(ExternalService externalService, int expectedResponse) {
		HttpStatus httpStatus = externalServiceService.sendGetRequest(externalService.getUrl()).block().getStatusCode();
		System.out.println(httpStatus.value());
		assertEquals(expectedResponse, httpStatus.value());
	}

	private static Stream sendGetRequestTestParameters() {
		return Stream.of(
				Arguments.of(new ExternalService(1L, "Google","https://www.google.com",  "1"), HttpStatus.OK.value()),
//				Arguments.of(new ExternalService(1L, "Facebook","www.facebook.com",  "1"), HttpStatus.MOVED_PERMANENTLY.value()),
////				Arguments.of(new ExternalService(1L, "bing","www.bing.com",  "1"), HttpStatus.OK.value()),
//				Arguments.of(new ExternalService(1L, "Service1","http://127.0.0.1:8081/simulate/services/service1", "1"), HttpStatus.OK.value()),
//				Arguments.of(new ExternalService(1L, "Service2","http://127.0.0.1:8081/simulate/services/service2",  "1"), HttpStatus.ACCEPTED.value()),
//				Arguments.of(new ExternalService(1L, "Service3","http://127.0.0.1:8081/simulate/services/service3",  "1"), HttpStatus.CREATED.value()),
				Arguments.of(new ExternalService(1L, "Service4","",  "1"), HttpStatus.INTERNAL_SERVER_ERROR.value()),
				Arguments.of(new ExternalService(1L, "Service5",null,  "1"), HttpStatus.INTERNAL_SERVER_ERROR.value())

		);
	}

	@ParameterizedTest
	@MethodSource("processResponseParameters")
	void processResponseTest(ExternalService externalService, ResponseEntity response, String expectedResponse) {
		externalServiceService.processResponse(externalService, response);
		System.out.println(externalService + "\n");
		assertEquals(expectedResponse, externalService.getStatus());
	}

	private static Stream processResponseParameters() {
		return Stream.of(
				Arguments.of(new ExternalService(1L, "bing","www.bing.com", "1"), ResponseEntity.status(HttpStatus.OK).build(), "1"),
				Arguments.of(new ExternalService(1L, "facebook","www.facebook.com",  "1"), ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).build(), "0"),
				Arguments.of(new ExternalService(1L, "example","http://192.168.1.113:8088",  "1"), ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(), "0")

		);
	}
}
