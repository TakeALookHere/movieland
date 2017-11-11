package com.miskevich.movieland.web.json;

import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.web.dto.UserDto;

import java.util.UUID;

public abstract class UserDtoConverter {

    public static UserDto mapObject(User user, UUID uuid) {
        UserDto userDto = new UserDto();
        userDto.setNickname(user.getNickname());
        userDto.setUuid(uuid);
        return userDto;
    }

}
