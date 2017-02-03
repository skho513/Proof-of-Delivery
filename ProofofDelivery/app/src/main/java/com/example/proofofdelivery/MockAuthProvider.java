package com.example.proofofdelivery;

import com.lalamove.base.auth.AuthenticationResult;
import com.lalamove.base.auth.IAuthProvider;
import com.lalamove.base.user.UserProfile;

import javax.inject.Inject;

/**
 * Mock implementation of auth provider
 *
 * @author milan
 */
public class MockAuthProvider implements IAuthProvider {
    @Inject
    public MockAuthProvider() {
    }

    @Override
    public void setUserAvailable(String s, AuthenticationResult authenticationResult) {

    }

    @Override
    public void setUserAvailable(UserProfile userProfile) {

    }

    @Override
    public void clearData() {

    }

    @Override
    public boolean isSessionAvailable() {
        return true;
    }

    @Override
    public String getToken() {
        return "token";
    }

    @Override
    public void setToken(String s) {

    }

    @Override
    public String getAccessToken() {
        return "access_token";
    }

    @Override
    public void setAccessToken(String s) {

    }

    @Override
    public String getClientId() {
        return "1234";
    }
}
