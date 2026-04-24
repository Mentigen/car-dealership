package ru.CarDealership.domain.car;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineVolumeTest {

    @Test
    void testValidVolume() {
        EngineVolume volume = new EngineVolume(2.0);
        assertEquals(2.0, volume.getValue(), 0.001);
    }

    @Test
    void testZeroVolume() {
        EngineVolume volume = new EngineVolume(0.0);
        assertEquals(0.0, volume.getValue(), 0.001);
    }

    @Test
    void testNegativeVolume() {
        assertThrows(IllegalArgumentException.class, () -> new EngineVolume(-1.5));
    }
}
