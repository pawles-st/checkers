package org.pawles.checkers.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveDataTest {

    final MoveData moveData = new MoveData("12:34");

    @Test
    void getNewX() {
        assertEquals(3, moveData.getNewX());
    }

    @Test
    void getNewY() {
        assertEquals(4, moveData.getNewY());
    }

    @Test
    void getStartX() {
        assertEquals(1, moveData.getStartX());
    }

    @Test
    void getStartY() {
        assertEquals(2, moveData.getStartY());
    }
}