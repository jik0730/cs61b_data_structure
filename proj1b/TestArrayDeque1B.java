import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayDeque1B {
	
	@Test
	public void test() {
		StudentArrayDeque<Integer> testArray = new StudentArrayDeque<Integer>();
		ArrayDeque<Integer> expArray = new ArrayDeque<Integer>();
		Integer actual = 0;
		Integer expected = 0;
		int errorCheck = 0;
		
		for(int i = 0; i < 100; i++) {
			int caseNum = (int) (Math.random() * 100);
			int randomNum = (int) (Math.random() * 10);
			
			switch(caseNum % 7) {
				case 0: testArray.addFirst(randomNum);
						expArray.addFirst(randomNum);
						System.out.println("addFirst(" + randomNum + ")");
						break;
				case 1: testArray.addLast(randomNum);
						expArray.addLast(randomNum);
						System.out.println("addLast(" + randomNum + ")");
						break;
				case 2: actual = testArray.removeFirst();
						expected = expArray.removeFirst();
						System.out.println("removeFirst()");
						if(actual != expected) {
							errorCheck++;
						}
						break;
				case 3: actual = testArray.removeLast();
						expected = expArray.removeLast();
						System.out.println("removeLast()");
						if(actual != expected) {
							errorCheck++;
						}
						break;
				case 4: actual = testArray.get(randomNum);
						expected = expArray.get(randomNum);
						System.out.println("get(" + randomNum + ")");
						if(actual != expected) {
							errorCheck++;
						}
						break;
				case 5: boolean a = testArray.isEmpty();
						boolean b = expArray.isEmpty();
						System.out.println("isEmpty()");
						break;
				case 6: actual = testArray.size();
						expected = expArray.size();
						System.out.println("size()");
						if(actual != expected) {
							errorCheck++;
						}
						break;
				default: System.out.println("Error!!");
			}
			if(errorCheck == 1) {
				break;
			}
		}
		
		assertEquals(expected, actual);
	}

	public static void main(String[] args) {
		jh61b.junit.TestRunner.runTests(TestArrayDeque1B.class);
	}
}