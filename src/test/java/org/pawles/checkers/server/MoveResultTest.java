package org.pawles.checkers.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveResultTest {
    final MoveResult kill = new MoveResult(MoveType.KILL);
    final MoveResult normal = new MoveResult(MoveType.NORMAL);
    final MoveResult none = new MoveResult(MoveType.NONE);

    @Test
    void getTypeTest1() {
        assertEquals(kill.getType(), MoveType.KILL);
    }

    @Test
    void getTypeTest2() {
        assertEquals(normal.getType(), MoveType.NORMAL);
    }

    @Test
    void getTypeTest3() {
        assertEquals(none.getType(), MoveType.NONE);
    }
}