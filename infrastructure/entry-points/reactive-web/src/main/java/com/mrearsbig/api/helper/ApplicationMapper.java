package com.mrearsbig.api.helper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.mrearsbig.api.dto.ApplicationRequest;
import com.mrearsbig.api.dto.ApplicationResponse;
import com.mrearsbig.model.application.Application;
import com.mrearsbig.model.loantype.LoanType;
import com.mrearsbig.model.status.Status;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    // Request → Domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true) // se setea en el use case
    Application toDomain(ApplicationRequest request);

    // Domain → Response
    ApplicationResponse toResponse(Application application);

    // Helpers Request → Domain
    default LoanType toLoanType(Integer value) {
        return value != null ? LoanType.builder().id(value).build() : null;
    }

    // Conversión manual LoanType -> Integer
    default Integer map(LoanType loanType) {
        return loanType != null ? loanType.getId() : null;
        // O loanType.ordinal()
    }

    // Conversión manual Status -> Integer
    default Integer map(Status status) {
        return status != null ? status.getId() : null;
    }
}
