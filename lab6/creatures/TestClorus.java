package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the plip class   
 *  @authr FIXME
 */

public class TestClorus {

    /* Replace with the magic word given in lab.
     * If you are submitting early, just put in "early" */
    public static final String MAGIC_WORD = "triangle";

    @Test
    public void testBasics() {
        Clorus p = new Clorus(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), p.color());
        p.move();
        assertEquals(1.97, p.energy(), 0.01);
        p.move();
        assertEquals(1.94, p.energy(), 0.01);
        p.stay();
        assertEquals(1.93, p.energy(), 0.01);
        p.stay();
        assertEquals(1.92, p.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus p = new Clorus(2);
        Clorus p_re = p.replicate();
        assertNotSame(p, p_re);
    }

    @Test
    public void testChoose() {
        Clorus p = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        //You can create new empties with new Empty();
        // Clorus p2 = new Clorus(2);
        // HashMap<Direction, Occupant> surrounded2 = new HashMap<Direction, Occupant>();
        // surrounded2.put(Direction.TOP, new Impassible());
        // surrounded2.put(Direction.BOTTOM, new Empty());
        // surrounded2.put(Direction.LEFT, new Impassible());
        // surrounded2.put(Direction.RIGHT, new Impassible());

        //Despite what the spec says, you cannot test for Cloruses nearby yet.
        //Sorry!  

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        // Action actual2 = p2.chooseAction(surrounded2);
        // Action expected2 = new Action(Action.ActionType.REPLICATE, Direction.BOTTOM);

        assertEquals(expected, actual);
        // assertEquals(expected2, actual2);
    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestClorus.class));
    }
} 
