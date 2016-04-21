public class OffByN implements CharacterComparator {
	private int change;
	public OffByN(int N) {
		change = N;
	}

	@Override
	public boolean equalChars(char x, char y) {
		if((int) x == (int) y - change || (int) x == (int) y + change) {
			return true;
		}
		return false;
	}
}