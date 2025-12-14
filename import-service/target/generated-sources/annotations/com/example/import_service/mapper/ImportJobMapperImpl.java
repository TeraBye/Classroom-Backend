package com.example.import_service.mapper;

import com.example.import_service.dto.response.ImportJobResponse;
import com.example.import_service.entity.ImportJob;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-30T18:20:36+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Oracle Corporation)"
)
@Component
public class ImportJobMapperImpl implements ImportJobMapper {

    @Override
    public ImportJobResponse toJobResponse(ImportJob importJob) {
        if ( importJob == null ) {
            return null;
        }

        ImportJobResponse.ImportJobResponseBuilder importJobResponse = ImportJobResponse.builder();

        importJobResponse.id( importJob.getId() );
        if ( importJob.getType() != null ) {
            importJobResponse.type( importJob.getType().name() );
        }
        if ( importJob.getStatus() != null ) {
            importJobResponse.status( importJob.getStatus().name() );
        }
        importJobResponse.message( importJob.getMessage() );
        importJobResponse.username( importJob.getUsername() );
        importJobResponse.fileName( importJob.getFileName() );
        importJobResponse.createdAt( importJob.getCreatedAt() );
        importJobResponse.updatedAt( importJob.getUpdatedAt() );

        return importJobResponse.build();
    }
}
