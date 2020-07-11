package ru.logisticplatform.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.modelmapper.ModelMapper;
import ru.logisticplatform.model.User;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO class for user requests by ROLE_USER
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public User toUser(UserDto userDto){
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(userDto, User.class);
    }

    public static UserDto fromUser(User user) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(user, UserDto.class);
    }

    public static List<UserDto> fromUser(List<User> users){
        return users.stream().map(entity -> fromUser(entity)).collect(Collectors.toList());
    }
}