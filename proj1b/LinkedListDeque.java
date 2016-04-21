public class LinkedListDeque<Item>{
	private class Node{
		Item item;
		Node next;
		Node prev;

		private Node(){
			item = null;
			next = null;
			prev = null;
		}

		private Node(Item _item, Node _next, Node _prev){
			item = _item;
			next = _next;
			prev = _prev;
		}
	}

	private Node sentinel;
	private int size;
	private Node re_temp;

	public LinkedListDeque(){
		size = 0;
		sentinel = new Node(null, null, null);
		re_temp = sentinel;
	}

	public void addFirst(Item item){
		if(isEmpty()){
			Node newNode = new Node(item, sentinel, sentinel);
			sentinel.next = newNode;
			sentinel.prev = newNode;
			size++;
		}
		else{
			Node newNode = new Node(item, sentinel.next, sentinel);
			sentinel.next.prev = newNode;
			sentinel.next = newNode;
			size++;
		}
	}

	public void addLast(Item item){
		if(isEmpty()){
			Node newNode = new Node(item, sentinel, sentinel);
			sentinel.next = newNode;
			sentinel.prev = newNode;
			size++;
		}
		else{
			Node newNode = new Node(item, sentinel, sentinel.prev);
			sentinel.prev.next = newNode;
			sentinel.prev = newNode;
			size++;
		}
	}

	public boolean isEmpty(){
		if(size == 0){
			return true;
		}
		return false;
	}

	public int size(){
		return size;
	}

	public void printDeque(){
		Node temp = sentinel;
		int count = size;
		while(count != 0){
			System.out.print(temp.next.item + " ");
			temp = temp.next;
			count--;
		}
	}

	public Item removeFirst(){
		if(isEmpty()){
			return null;
		}
		else{
			Node temp = sentinel.next;
			sentinel.next = sentinel.next.next;
			sentinel.next.prev = sentinel;
			temp.next = null;
			temp.prev = null;
			size--;
			return temp.item;
		}

	}

	public Item removeLast(){
		if(isEmpty()){
			return null;
		}
		else{
			Node temp = sentinel.prev;
			sentinel.prev = sentinel.prev.prev;
			sentinel.prev.next = sentinel;
			temp.next = null;
			temp.prev = null;
			size--;
			return temp.item;
		}
	}

	public Item get(int index){
		if(isEmpty()){
			return null;
		}
		Node temp = sentinel.next;
		while(index != 0){
			temp = temp.next;
			index--;
		}
		return temp.item;
	}

	public Item getRecursive(int index){
		// Node temp = sentinel.next;
		if(isEmpty()){
			return null;
		}
		if(index == 0){
			Item temp = re_temp.next.item;
			re_temp = sentinel;
			return temp;
		}
		else{
			re_temp = re_temp.next;
			return getRecursive(index - 1);
		}
	}
}