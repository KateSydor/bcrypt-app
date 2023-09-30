package com.example.webapplication.dto.mapper;

import com.example.webapplication.dto.request.BCryptRequest;
import com.example.webapplication.dto.response.BCryptResponse;
import com.example.webapplication.entity.BCryptEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BCryptMapper {
    BCryptMapper MAPPER = Mappers.getMapper(BCryptMapper.class);

    BCryptEntity fromRequestToEntity(BCryptRequest bcryptRequest);

    BCryptResponse fromEntityToResponse(BCryptEntity bcryptEntity);
}
