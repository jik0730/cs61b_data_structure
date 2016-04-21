public class OffByOne implements CharacterComparator {
	@Override
	public boolean equalChars(char x, char y) {
		if((int) x == (int) y - 1 || (int) x == (int) y + 1) {
			return true;
		}
		return false;
	}
}