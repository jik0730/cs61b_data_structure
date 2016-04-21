import org.junit.Test;
import static org.junit.Assert.*;

public class TestLinkedListDeque1B {
	
	@Test
	public void test() {
		StudentLinkedListDeque<Integer> testList = new StudentLinkedListDeque<Integer>();
		LinkedListDeque<Integer> expList = new LinkedListDeque<Integer>();
		Integer actual = 0;
		Integer expected = 0;
		int errorCheck = 0;
		
		for(int i = 0; i < 100; i++) {
			int caseNum = (int) (Math.random() * 100);
			int randomNum = (int) (Math.random() * 10);
			
			switch(caseNum % 7) {
				case 0: testList.addFirst(randomNum);
						expList.addFirst(randomNum);
						System.out.println("addFirst(" + randomNum + ")");
						break;
				case 1: testList.addLast(randomNum);
						expList.addLast(randomNum);
						System.out.println("addLast(" + randomNum + ")");
						break;
				case 2: actual = testList.removeFirst();
						expected = expList.removeFirst();
						System.out.println("removeFirst()");
						if(actual != expected) {
							errorCheck++;
						}
						break;
				case 3: actual = testList.removeLast();
						expected = expList.removeLast();
						System.out.println("removeLast()");
						if(actual != expected) {
							errorCheck++;
						}
						break;
				case 4: actual = testList.get(randomNum);
						expected = expList.get(randomNum);
						System.out.println("get(" + randomNum + ")");
						if(actual != expected) {
							errorCheck++;
						}
						break;
				case 5: boolean a = testList.isEmpty();
						boolean b = expList.isEmpty();
						System.out.println("isEmpty()");
						break;
				case 6: actual = testList.size();
						expected = expList.size();
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
		jh61b.junit.TestRunner.runTests(TestLinkedListDeque1B.class);
	}
}