package com.microsoft.xuetang.component;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class UserProfileComponentTest {

    @Test
    public void testGetUserProfile() throws Exception {
        System.out.println(UserProfileComponent.getUserProfile());
        System.out.println(UserProfileComponent.getUserProfile());
    }
}