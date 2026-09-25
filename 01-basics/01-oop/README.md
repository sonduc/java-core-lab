# 01 OOP

Focus on Java object modeling, contracts, extension points, and design trade-offs.

- `example/`: sample code for object-oriented design basics
- `exercise/`: practice tasks to implement and test yourself

## 1. Four OOP principles in Java

- **Encapsulation**
  - Keep state behind a stable API and enforce invariants at the boundary.
  - In Java, use `private` fields and expose behavior-oriented methods instead of raw state mutation.

```java
public final class BankAccount {
    private BigDecimal balance = BigDecimal.ZERO;

    public void deposit(BigDecimal amount) {
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        balance = balance.add(amount);
    }

    public BigDecimal balance() {
        return balance;
    }
}
```

- **Inheritance**
  - Reuse state/behavior only when there is a real subtype relationship and the subclass preserves the parent contract.
  - Inheritance couples implementations, so extending a class is stronger than sharing utility code.

```java
public class Animal {
    public String move() {
        return "moving";
    }
}

public final class Bird extends Animal {
    @Override
    public String move() {
        return "flying";
    }
}
```

- **Polymorphism**
  - Code against a supertype so callers depend on behavior, not concrete implementations.
  - Java resolves the overridden method at runtime, enabling substitution without `if/else` on types.

```java
public interface PaymentProcessor {
    void process(BigDecimal amount);
}

public final class CheckoutService {
    public void checkout(PaymentProcessor processor, BigDecimal amount) {
        processor.process(amount);
    }
}
```

- **Abstraction**
  - Expose essential operations and hide implementation details that should not leak into callers.
  - Interfaces and abstract classes define contracts; implementations can change independently.

```java
public interface Cache<K, V> {
    Optional<V> get(K key);
    void put(K key, V value);
}

public final class InMemoryCache<K, V> implements Cache<K, V> {
    private final Map<K, V> store = new HashMap<>();

    public Optional<V> get(K key) { return Optional.ofNullable(store.get(key)); }
    public void put(K key, V value) { store.put(key, value); }
}
```

## 2. Interface vs abstract class

| Aspect | Interface | Abstract class |
|---|---|---|
| Primary purpose | Define a capability/contract | Share state + partial implementation |
| State | Constants only; no instance fields | Can hold instance fields |
| Constructors | None | Supported |
| Multiple inheritance | A class can implement many | A class can extend only one |
| Default behavior | `default`/`static` methods | Concrete + abstract methods |
| Best use | Cross-cutting role (`Comparable`, `Runnable`, ports) | Template/base type with shared lifecycle or invariants |
| Avoid when | You need shared mutable state or constructor logic | You only need a contract and want maximum flexibility |

- **Use an interface when**
  - Multiple implementations must be substitutable.
  - Consumers should depend only on behavior.
  - Types may need to combine several roles.

- **Use an abstract class when**
  - Subclasses share non-trivial state, lifecycle, or protected helper logic.
  - The base type must centralize invariant enforcement.
  - You intentionally want tighter coupling than an interface provides.

## 3. Composition over inheritance

- Prefer composition when behavior can be assembled from collaborators instead of inherited from a base class.
- Composition reduces superclass coupling, avoids fragile override points, and makes behavior replaceable in tests or runtime wiring.

```java
public interface DiscountPolicy {
    BigDecimal apply(BigDecimal subtotal);
}

public final class SeasonalDiscountPolicy implements DiscountPolicy {
    public BigDecimal apply(BigDecimal subtotal) {
        return subtotal.multiply(new BigDecimal("0.90"));
    }
}

public final class OrderService {
    private final DiscountPolicy discountPolicy;

    public OrderService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public BigDecimal total(BigDecimal subtotal) {
        return discountPolicy.apply(subtotal);
    }
}
```

- **Why this is better here**
  - `OrderService` depends on a capability, not a base class hierarchy.
  - Discount logic can vary independently without subclassing `OrderService`.
  - Testing becomes trivial with a stub or fake `DiscountPolicy`.

## 4. Common pitfalls

- **`equals`/`hashCode` contract violation**
  - If two objects are equal, they must return the same `hashCode`.
  - Violations break `HashSet`, `HashMap`, deduplication, and cache lookups in non-obvious ways.

- **Mutable fields used in `hashCode`**
  - If a field participates in `equals`/`hashCode`, mutating it after insertion into a hash-based collection makes the object unreachable in its current bucket.
  - Use immutable key fields or avoid storing mutable identity objects in hashed collections.

- **Inheritance breaking encapsulation (fragile base class)**
  - Subclasses can become dependent on superclass internals, call order, or overridable hooks that were never designed as extension points.
  - A safe refactor in the base class can change subclass behavior, even when the public API is unchanged.

```java
public class ReportGenerator {
    public final String generate() {
        String raw = load();
        return format(raw);
    }

    protected String load() { return "data"; }
    protected String format(String raw) { return raw.trim(); }
}

public final class AuditedReportGenerator extends ReportGenerator {
    @Override
    protected String format(String raw) {
        return "[AUDIT] " + raw;
    }
}
```

- **Why this is fragile**
  - If `generate()` later calls `format(load()).toUpperCase()` or changes hook order, subclass semantics change silently.
  - Prefer explicit composition/delegation when extension behavior is not a stable framework contract.

## 5. Done criteria

- Explain the difference between encapsulation, inheritance, polymorphism, and abstraction without mixing them.
- Choose between interface, abstract class, composition, and inheritance based on coupling and substitution requirements.
- Implement correct `equals`/`hashCode` for value objects and explain why mutable identity is risky in hash-based collections.
- Identify a fragile base class design and refactor it toward composition or a narrower contract.
- Review a Java type hierarchy and state whether it satisfies subtype semantics instead of only reusing code.
