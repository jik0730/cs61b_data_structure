public interface Deque<Item> {
	public void addFirst(Item item);
	public void addLast(Item item);
	public int size();
	public Item get(int i);
	public Item getRecursive(int i);
	public Item removeFirst();
	public Item removeLast();
	public void printDeque();
}