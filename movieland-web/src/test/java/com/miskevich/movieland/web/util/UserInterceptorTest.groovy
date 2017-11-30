package com.miskevich.movieland.web.util

import com.miskevich.movieland.service.IUserService
import com.miskevich.movieland.service.impl.UserService
import com.miskevich.movieland.service.security.UserPrincipal
import com.miskevich.movieland.web.controller.provider.InterceptorDataProvider
import com.miskevich.movieland.web.exception.InvalidAccessException
import com.miskevich.movieland.web.security.RoleRequired
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.mockito.Mockito.*

class UserInterceptorTest {

    @InjectMocks
    private UserInterceptor userInterceptor
    @Mock
    private IUserService mockUserService

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test(dataProvider = 'userInvalidRole', dataProviderClass = InterceptorDataProvider.class,
    expectedExceptionsMessageRegExp = ".*Validation of user role access type failed, required role:.*",
    expectedExceptions = InvalidAccessException.class)
    void testValidateRole(requiredRoles, userRole, principal) {
        when(mockUserService.getRole(anyInt())).thenReturn(userRole)
        userInterceptor.validateRole(requiredRoles, principal)
    }
}
