package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalDateProviderTests {
    
    @Test
    void onlyReturnOneInstance() {
        assertEquals(LocalDateProvider.getInstance(), LocalDateProvider.getInstance());
    }
}
