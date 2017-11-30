package com.miskevich.movieland.web.util

import com.miskevich.movieland.service.IUserService
import com.miskevich.movieland.web.controller.provider.InterceptorDataProvider
import com.miskevich.movieland.web.exception.InvalidAccessException
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import static org.mockito.Matchers.anyInt
import static org.mockito.Mockito.when
import static org.testng.AssertJUnit.assertTrue

class UserInterceptorTest {

    @InjectMocks
    private UserInterceptor userInterceptor
    @Mock
    private IUserService mockUserService

    @BeforeTest
    private void setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test(dataProvider = 'userInvalidRoleInvalid', dataProviderClass = InterceptorDataProvider.class,
            expectedExceptionsMessageRegExp = ".*Validation of user\'s role access type failed, required role:.*",
            expectedExceptions = InvalidAccessException.class)
    void testValidateRoleInvalid(requiredRoles, userRole, principal) {
        when(mockUserService.getRole(anyInt())).thenReturn(userRole)
        userInterceptor.validateRole(requiredRoles, principal)
    }

    @Test(dataProvider = 'userInvalidRoleValid', dataProviderClass = InterceptorDataProvider.class)
    void testValidateRoleValid(requiredRoles, userRole, principal) {
        when(mockUserService.getRole(anyInt())).thenReturn(userRole)
        assertTrue(userInterceptor.validateRole(requiredRoles, principal))
    }
}
