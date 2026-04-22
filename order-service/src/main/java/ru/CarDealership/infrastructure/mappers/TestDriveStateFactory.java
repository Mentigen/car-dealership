package ru.CarDealership.infrastructure.mappers;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.order.testDriveState.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class TestDriveStateFactory {
    private final Map<String, Supplier<TestDriveState>> states = new HashMap<>();

    public TestDriveStateFactory() {
        states.put("PENDING", PendingState::new);
        states.put("APPROVED", ApprovedState::new);
        states.put("DONE", DoneState::new);
        states.put("CANCELLED", CancelledState::new);
    }

    public TestDriveState fromString(String state) {
        return states.getOrDefault(state, () -> {
            throw new IllegalArgumentException("Unknown test drive status: " + state);
        }).get();
    }
}
