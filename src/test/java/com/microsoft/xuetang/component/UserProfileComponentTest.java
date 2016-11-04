package com.microsoft.xuetang.component;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.testng.Assert.*;

public class UserProfileComponentTest {

    @Test
    public void testGetUserProfile() throws Exception {
        System.out.println(UserProfileComponent.getUserProfile());
        System.out.println(UserProfileComponent.getUserProfile());
        List<Integer> arr = new ArrayList<>();
        arr.add(3);
        arr.add(2);
        arr.add(1);
        arr.sort(new Comparator<Integer>() {
            @Override public int compare(Integer o1, Integer o2) {
                return o2.intValue() - o1.intValue();
            }
        });

        System.out.println(arr);
    }
}