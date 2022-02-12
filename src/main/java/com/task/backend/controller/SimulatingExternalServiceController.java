package com.task.backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("simulate/services")
public class SimulatingExternalServiceController {
    @GetMapping("/service1")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> service1(){
        return Mono.just("Helllo Service 1");
    }

    @GetMapping("/service2")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<String> service2(){
        return Mono.just("Hello Service 2");
    }

    @GetMapping("/service3")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> service3(){
        return Mono.just("Hello Service 3");
    }

    @GetMapping("/service4")
    public Mono<ResponseEntity<String>> service4(){
        if((int) ((Math.random() * (10 - 1)) + 1)%2 == 0){
            return Mono.just(ResponseEntity.ok().body("Hello Service 4"));
        }
        return Mono.just(ResponseEntity.internalServerError().body("Hello Service 4"));
    }

    @GetMapping("/service5")
    public Mono<ResponseEntity<String>> service5(){
        if((int) ((Math.random() * (10 - 1)) + 1)%2 == 0){
            return Mono.just(ResponseEntity.ok().body("Hello Service 5"));
        }
        return Mono.just(ResponseEntity.internalServerError().body("Hello Service 5"));
    }
}
