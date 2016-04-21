import static org.junit.Assert.*;
import org.junit.Test;

public class FlikTest {

	@Test
	public void test(){
		assertTrue(Flik.isSameNumber(i, j));
	}
	public static void main(String[] args){
		jh61b.junit.TestRunner.runTests("all", FlikTest.class);
	}
}