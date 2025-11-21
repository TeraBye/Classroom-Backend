package com.example.import_service.event;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResultEvent {
    Long jobId;
    String status;
    String errorMessage;
}
