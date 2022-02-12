package com.task.backend.domainobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("services")
public class ExternalService {
    @Id
    private Long id;
    private String name;
    private String url;
    private LocalDateTime creation_datetime;
    private LocalDateTime update_datetime;
    private LocalDateTime last_verified_datetime;
    private String status;

    public ExternalService(Long id, String name, String url, String status) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.status = status;
    }
    public ExternalService(Long id, String name, String url, String status, LocalDateTime creation_datetime) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.status = status;
        this.creation_datetime = creation_datetime;
    }
}