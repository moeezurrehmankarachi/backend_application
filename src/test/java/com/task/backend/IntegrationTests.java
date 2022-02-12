package com.task.backend;

import com.task.backend.datatransferobjects.ExternalServiceDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BackendApplication.class)
public class IntegrationTests {
    private Logger logger = LoggerFactory.getLogger(IntegrationTests.class);

    @Autowired
    private WebTestClient testClient;

    @Test
    public void createService_Add_Remove() throws Exception {
        ExternalServiceDTO externalServiceDTO = ExternalServiceDTO.newBuilder()
                .setName("Test added ADD-REMOVE")
                .setUrl("http://www.moeezmoeezmoeez.com")
                .setCreation_datetime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toOffsetDateTime())
                .createExternalServiceDTO();

        WebTestClient.ListBodySpec<ExternalServiceDTO> listBodySpec = testClient.post()
                .uri("/services/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(externalServiceDTO))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBodyList(ExternalServiceDTO.class)
                .hasSize(1);

        logger.info("Response received" + listBodySpec.returnResult().getResponseBody());

        //Removing added Test Services
        for(ExternalServiceDTO externalServiceDTOAdded : listBodySpec.returnResult().getResponseBody()) {
            logger.info("Deleting service: " + externalServiceDTOAdded);

            WebTestClient.ListBodySpec listBodySpecRemove = testClient.delete()
                    .uri("/services/removebyid/{id}", externalServiceDTOAdded.getId())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBodyList(Integer.class)
                    .hasSize(1);
        }

    }

    @Test
    public void createService_ConstraintsCheck() {
        ExternalServiceDTO externalServiceDTO = ExternalServiceDTO.newBuilder()
//                .setName("test added")
                .setUrl("http://www.moeezmoeezmoeez.com")
                .setCreation_datetime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toOffsetDateTime())
                .createExternalServiceDTO();

        WebTestClient.ListBodySpec listBodySpec = testClient.post()
                .uri("/services/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(externalServiceDTO))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBodyList(ExternalServiceDTO.class)
                .hasSize(1);

        logger.info("Response received" + listBodySpec.returnResult().getResponseBody());
    }

    @Test
    public void updateServiceTest_Add_Update_Remove() throws Exception {
        ExternalServiceDTO externalServiceDTO = ExternalServiceDTO.newBuilder()
                .setName("Test added Add")
                .setUrl("http://www.moeezmoeezmoeezAdd_Update_Remove.com")
                .setCreation_datetime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toOffsetDateTime())
                .createExternalServiceDTO();

        WebTestClient.ListBodySpec<ExternalServiceDTO> listBodySpec = testClient.post()
                .uri("/services/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(externalServiceDTO))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBodyList(ExternalServiceDTO.class)
                .hasSize(1);
        logger.info("Add: Response received" + listBodySpec.returnResult().getResponseBody()+ "\n\n");

        ExternalServiceDTO externalServiceDTOUpdate = ExternalServiceDTO.newBuilder()
                .setId(listBodySpec.returnResult().getResponseBody().get(0).getId())
                .setName("Test added Update")
                .setUrl("http://www.moeezmoeezmoeezUpdate.com")
                .setCreation_datetime(listBodySpec.returnResult().getResponseBody().get(0).getCreation_datetime())
                .setUpdate_datetime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toOffsetDateTime())
                .createExternalServiceDTO();

        WebTestClient.ListBodySpec<ExternalServiceDTO> UpdatelistBodySpec = testClient.post()
                .uri("/services/update")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(externalServiceDTOUpdate))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ExternalServiceDTO.class)
                .hasSize(1);
        logger.info("Update: Response received" + UpdatelistBodySpec.returnResult().getResponseBody() + "\n\n");


        //Removing added Test Services
        for(ExternalServiceDTO externalServiceDTOAdded : listBodySpec.returnResult().getResponseBody()) {
            logger.info("Deleting service: " + externalServiceDTOAdded);

            WebTestClient.ListBodySpec listBodySpecRemove = testClient.delete()
                    .uri("/services/removebyid/{id}", externalServiceDTOAdded.getId())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBodyList(Integer.class)
                    .hasSize(1);
        }

    }

    @Test
    public void updateServiceTest_NO_ADD_Update_Remove_404() throws Exception {
        ExternalServiceDTO externalServiceDTOUpdate = ExternalServiceDTO.newBuilder()
                .setId(400L)
                .setName("Test added Update")
                .setUrl("http://www.moeezmoeezmoeezUpdate.com")
                .setCreation_datetime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toOffsetDateTime())
                .setUpdate_datetime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toOffsetDateTime())
                .createExternalServiceDTO();

        WebTestClient.ListBodySpec<ExternalServiceDTO> UpdatelistBodySpec = testClient.post()
                .uri("/services/update")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(externalServiceDTOUpdate))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBodyList(ExternalServiceDTO.class);
//                .hasSize(1);
        logger.info("Update: Response received" + UpdatelistBodySpec.returnResult().getResponseBody() + "\n\n");

        assertEquals(HttpStatus.NOT_FOUND, UpdatelistBodySpec.returnResult().getStatus());
    }

//    @Test
//    public void getAllServicesTest_onDefaultDataScript() {
//        WebTestClient.ListBodySpec<ExternalServiceDTO> listBodySpec = testClient.get()
//                .uri("/services/getAllServices")
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(ExternalServiceDTO.class);
//
//        logger.info("Response received");
//        listBodySpec.returnResult().getResponseBody().forEach(System.out::println);
//
//        assertTrue("Size of returned list" + listBodySpec.returnResult().getResponseBody().size() + " is greater than original script(" + 6 + ")",
//                6<= listBodySpec.returnResult().getResponseBody().size());
//    }
}