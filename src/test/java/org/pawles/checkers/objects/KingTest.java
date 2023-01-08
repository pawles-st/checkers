package org.pawles.checkers.objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    @BeforeAll
    public static void init() {
        SquareInstancer.initialise(8, 8);
    }

    @Test
    public void moveVerificationTest() {

        // create a King

        King king = new King(SquareInstancer.getInstance(5, 5), Colour.BLACK);

        // correct move

        assertTrue(king.verifyMove(SquareInstancer.getInstance(1, 1)));

        // incorrect moves

        assertFalse(king.verifyMove(SquareInstancer.getInstance(1, 5)));
        assertFalse(king.verifyMove(SquareInstancer.getInstance(2, 4)));
    }
}