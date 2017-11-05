package com.miskevich.movieland.web.json;

import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.web.dto.UserDto;

public abstract class UserDtoConverter {

    public static UserDto mapObject(User user) {
        UserDto userDto = new UserDto();
        userDto.setNickname(user.getNickname());
        userDto.setUuid(user.getUuid());
        return userDto;
    }

}
