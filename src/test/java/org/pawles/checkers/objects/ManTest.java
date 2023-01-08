package org.pawles.checkers.objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManTest {
    @BeforeAll
    public static void init() {
        SquareInstancer.initialise(8, 8);
    }

    @Test
    public void moveVerificationTest() {

        // create a Man

        Man man = new Man(SquareInstancer.getInstance(2, 4), Colour.BLACK);

        // correct moves

        assertTrue(man.verifyMove(SquareInstancer.getInstance(1, 5)));
        assertTrue(man.verifyMove(SquareInstancer.getInstance(4, 2)));

        // incorrect moves

        assertFalse(man.verifyMove(SquareInstancer.getInstance(3, 4)));
        assertFalse(man.verifyMove(SquareInstancer.getInstance(5, 7)));
    }
}