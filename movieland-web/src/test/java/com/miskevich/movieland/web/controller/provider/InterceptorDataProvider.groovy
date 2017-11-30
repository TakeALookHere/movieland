package com.miskevich.movieland.web.controller.provider

import com.miskevich.movieland.entity.User
import com.miskevich.movieland.model.Role
import com.miskevich.movieland.service.security.UserPrincipal
import com.miskevich.movieland.web.security.RoleRequired
import org.testng.annotations.DataProvider

import java.time.LocalDateTime


class InterceptorDataProvider {

    @DataProvider(name = 'userInvalidRoleInvalid')
    static Object[][] userInvalidRoleInvalid() {

        Role[] requiredRoles = [Role.ADMIN, Role.USER]
        def userRole = Role.MANAGER
        def principal = new UserPrincipal(new User(id: 1), LocalDateTime.now())

        def array = new Object[1][]
        array[0] = [requiredRoles, userRole, principal]
        return array
    }

    @DataProvider(name = 'userInvalidRoleValid')
    static Object[][] userInvalidRoleValid() {

        Role[] requiredRoles = [Role.ADMIN, Role.USER]
        def userRole = Role.USER
        def principal = new UserPrincipal(new User(id: 1), LocalDateTime.now())

        def array = new Object[1][]
        array[0] = [requiredRoles, userRole, principal]
        return array
    }
}
