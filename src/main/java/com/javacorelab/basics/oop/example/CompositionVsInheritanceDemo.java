package com.javacorelab.basics.oop.example;

/**
 * Demonstrates why README section "Composition over inheritance" recommends
 * has-a over extends for behavior assembly.
 */
public final class CompositionVsInheritanceDemo {

    public static void main(String[] args) {
        System.out.println("=== BAD: inheritance (Car extends Engine) ===");
        LegacyCar bad = new LegacyCar("BAD-001", 6800);
        bad.start();      // Engine API is now part of Car's public surface.
        bad.accelerate(); // Car directly mutates inherited engine internals.
        bad.stop();

        System.out.println();
        System.out.println("=== GOOD: composition (Car has-a Engine) ===");
        Car good = new Car("GOOD-001", new Engine(6800));
        good.drive();
        good.park();
    }
}

class LegacyEngine {
    protected int rpm;
    protected final int redlineRpm;
    private boolean running;

    LegacyEngine(int redlineRpm) {
        this.redlineRpm = requirePositive("redlineRpm", redlineRpm);
    }

    public void start() {
        running = true;
        rpm = 900;
        System.out.println("LegacyEngine started at idle rpm=" + rpm);
    }

    public void stop() {
        running = false;
        rpm = 0;
        System.out.println("LegacyEngine stopped");
    }

    public boolean isRunning() {
        return running;
    }

    protected int requirePositive(String field, int value) {
        if (value <= 0) {
            throw new IllegalArgumentException(field + " must be > 0");
        }
        return value;
    }
}

final class LegacyCar extends LegacyEngine {
    private final String vin;
    
    LegacyCar(String vin, int redlineRpm) {
        super(redlineRpm);
        if (vin == null || vin.isBlank()) {
            throw new IllegalArgumentException("vin must not be blank");
        }
        this.vin = vin;
    }

    public void accelerate() {
        if (!isRunning()) {
            throw new IllegalStateException("Engine must be running before accelerate");
        }
        // Fragile base class symptom:
        // this subclass writes inherited engine state directly and depends on its representation.
        // If LegacyEngine changes rpm semantics (unit, access, lifecycle), subclass behavior breaks.
        rpm += 1500;
        System.out.println("LegacyCar " + vin + " accelerates to rpm=" + rpm);
    }
}

final class Engine {
    private final int redlineRpm;
    private int rpm;
    private boolean running;

    Engine(int redlineRpm) {
        if (redlineRpm <= 0) {
            throw new IllegalArgumentException("redlineRpm must be > 0");
        }
        this.redlineRpm = redlineRpm;
    }

    void start() {
        running = true;
        rpm = 900;
        System.out.println("Engine started at idle rpm=" + rpm);
    }

    void stop() {
        running = false;
        rpm = 0;
        System.out.println("Engine stopped");
    }

    void increaseRpm(int delta) {
        if (!running) {
            throw new IllegalStateException("Cannot increase RPM while engine is stopped");
        }
        int next = rpm + delta;
        if (next > redlineRpm) {
            throw new IllegalStateException("RPM exceeds redline (" + redlineRpm + ")");
        }
        rpm = next;
    }

    int rpm() {
        return rpm;
    }
}

final class Car {
    private final String vin;
    private final Engine engine;

    Car(String vin, Engine engine) {
        if (vin == null || vin.isBlank()) {
            throw new IllegalArgumentException("vin must not be blank");
        }
        if (engine == null) {
            throw new IllegalArgumentException("engine must not be null");
        }
        this.vin = vin;
        this.engine = engine;
    }

    void drive() {
        engine.start();
        engine.increaseRpm(1500);
        System.out.println("Car " + vin + " drives at rpm=" + engine.rpm());
    }

    void park() {
        engine.stop();
        System.out.println("Car " + vin + " parked");
    }
}
