package ru.logisticplatform.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.modelmapper.ModelMapper;
import ru.logisticplatform.model.user.UserType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO class for user requests by USER_USERTYPE
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTypeDto {

    private Long id;
    private String name;

    public static UserType toUserType(UserTypeDto userTypeDto){
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(userTypeDto, UserType.class);
    }

    public static UserTypeDto fromUserType(UserType userType) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(userType, UserTypeDto.class);
    }

    public static List<UserTypeDto> fromUserType(List<UserType> userTypes){
        return userTypes.stream().map(entity -> fromUserType(entity)).collect(Collectors.toList());
    }

}
