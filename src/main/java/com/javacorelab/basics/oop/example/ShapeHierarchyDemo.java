package com.javacorelab.basics.oop.example;

/**
 * Demonstrates a shape hierarchy backed by an abstract base type.
 *
 * <p>Why abstract class (instead of interface), aligned with README section
 * "Interface vs abstract class":
 * shared state + invariant enforcement live in one place here. Every shape has a
 * non-empty name and dimensions validated through protected helpers. That is constructor
 * and lifecycle logic; an interface is ideal for capability contracts, but not for
 * centralized mutable/instance state + shared base initialization.</p>
 */
public final class ShapeHierarchyDemo {

    public static void main(String[] args) {
        Shape[] shapes = {
                new Circle("Circle", 5.0),
                new Rectangle("Rectangle", 4.0, 6.0),
                new Triangle("Triangle", 3.0, 4.0, 5.0)
        };

        for (Shape shape : shapes) {
            System.out.printf(
                    "%s -> area=%.4f, perimeter=%.4f%n",
                    shape.name(),
                    shape.area(),
                    shape.perimeter()
            );
        }
    }
}

abstract class Shape {
    private final String name;

    protected Shape(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
    }

    public final String name() {
        return name;
    }

    protected final double requirePositive(String dimensionName, double value) {
        if (value <= 0.0) {
            throw new IllegalArgumentException(dimensionName + " must be > 0");
        }
        return value;
    }

    public abstract double area();

    public abstract double perimeter();
}

final class Circle extends Shape {
    private final double radius;

    Circle(String name, double radius) {
        super(name);
        this.radius = requirePositive("radius", radius);
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2.0 * Math.PI * radius;
    }
}

final class Rectangle extends Shape {
    private final double width;
    private final double height;

    Rectangle(String name, double width, double height) {
        super(name);
        this.width = requirePositive("width", width);
        this.height = requirePositive("height", height);
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2.0 * (width + height);
    }
}

final class Triangle extends Shape {
    private final double a;
    private final double b;
    private final double c;

    Triangle(String name, double a, double b, double c) {
        super(name);
        this.a = requirePositive("side a", a);
        this.b = requirePositive("side b", b);
        this.c = requirePositive("side c", c);
        if (this.a + this.b <= this.c || this.a + this.c <= this.b || this.b + this.c <= this.a) {
            throw new IllegalArgumentException("triangle inequality is violated");
        }
    }

    @Override
    public double area() {
        double s = perimeter() / 2.0;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    @Override
    public double perimeter() {
        return a + b + c;
    }
}
