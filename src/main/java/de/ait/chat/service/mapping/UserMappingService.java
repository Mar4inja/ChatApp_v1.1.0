package de.ait.chat.service.mapping;

import de.ait.chat.entity.User;
import de.ait.chat.entity.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMappingService {


    @Mapping(target = "password", ignore = true)
    UserDTO mapEntityToDto(User entity);

    User mapDtoToEntity(UserDTO dto);

}
