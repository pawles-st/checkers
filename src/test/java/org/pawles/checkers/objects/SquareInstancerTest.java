package org.pawles.checkers.objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareInstancerTest { // don't run this test together with others - weird errors pop up

    @BeforeAll
    public static void init() {
        SquareInstancer.initialise(10, 12);
    }

    @Test
    public void sameSquareTest() {

        // receive the square two times; assert it's the same object

        final Square square1 = SquareInstancer.getInstance(4, 11);
        final Square square2 = SquareInstancer.getInstance(4, 11);
        assertSame(square1, square2);

        // assert objects created with new are NOT the same

        assertNotSame(new Square(47, 13), new Square(47, 13));
    }

    @Test
    public void correctCoordinatesTest() {

        // check if the square coordinates are correct

        final Square square = SquareInstancer.getInstance(9, 3);
        assertEquals(9, square.getX());
        assertEquals(3, square.getY());
    }
}