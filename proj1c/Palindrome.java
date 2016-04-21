public class Palindrome  {
	public static Deque<Character> wordToDeque(String word) {
		Deque<Character> deque = new LinkedListDequeSolution<Character>();
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            deque.addLast(c);
        }
        return deque;
    }

    public static boolean isPalindrome(String word) {
    	Deque<Character> deque = Palindrome.wordToDeque(word);
    	for(int i = 0; i < deque.size() / 2; i++) {
    		if(deque.get(i) == deque.get(deque.size() - i - 1)) {
    			;
    		}
    		else {
    			return false;
    		}
    	}
    	return true;
    }

    public static boolean isPalindrome(String word, CharacterComparator cc) {
    	Deque<Character> deque = Palindrome.wordToDeque(word);
    	for(int i = 0; i < deque.size() / 2; i++) {
    		if(cc.equalChars(deque.get(i), deque.get(deque.size() - i - 1))) {
    			;
    		}
    		else {
    			return false;
    		}
    	}
    	return true;
    }

    // public static boolean isPalindrome(String word, CharacterComparator cc, int N) {
    // 	Deque<Character> deque = Palindrome.wordToDeque(word);
    // 	cc = new OffByN(N);
    // 	for(int i = 0; i < deque.size() / 2; i++) {
    // 		if(cc.equalChars(deque.get(i), deque.get(deque.size() - i - 1))) {
    // 			;
    // 		}
    // 		else {
    // 			return false;
    // 		}
    // 	}
    // 	return true;
    // }
}