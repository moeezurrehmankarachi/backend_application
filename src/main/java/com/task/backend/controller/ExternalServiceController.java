package com.task.backend.controller;

import com.task.backend.controller.mapper.ExternalServiceMapper;
import com.task.backend.datatransferobjects.ExternalServiceDTO;
import com.task.backend.service.ExternalServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("services")
public class ExternalServiceController {
    private Logger logger = LoggerFactory.getLogger(ExternalServiceController.class);

    @Autowired
    private final ExternalServiceService service;

    public ExternalServiceController(ExternalServiceService service) {
        this.service = service;
    }

    @PostMapping("/add")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ExternalServiceDTO> addService(@Valid @RequestBody ExternalServiceDTO externalServiceDTO){
        logger.info("Adding external service: " + externalServiceDTO);
        return service.add(ExternalServiceMapper.makeExternalService(externalServiceDTO))
                .map(externalServices -> ExternalServiceMapper.makeExternalServiceDTO(externalServices));
    }

    @PostMapping("/update")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Mono<ExternalServiceDTO> updateService(@RequestBody ExternalServiceDTO externalServiceDTO){
        logger.info("Updating external service: " + externalServiceDTO);
        return service.update(ExternalServiceMapper.makeExternalService(externalServiceDTO))
                .map(externalServices -> ExternalServiceMapper.makeExternalServiceDTO(externalServices));
    }

    @DeleteMapping("/removebyid/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Mono<Integer> removeService(@PathVariable Long id) {
        logger.info("Removing external service with id: " + id);
        return service.remove(id);
    }

    @GetMapping("/getAllServices")
    @ResponseBody
    public Flux<ExternalServiceDTO> getAllServices(){
        logger.info("Get All Services Called");
        return service.findAll()
                .collectList()
                .map(externalServices -> ExternalServiceMapper.makeExternalServiceDTOList(externalServices))
                .flatMapMany(externalServiceDTOS -> Flux.fromIterable(externalServiceDTOS));
    }
}
