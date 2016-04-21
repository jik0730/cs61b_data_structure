public class ArrayDeque<Item>{
	public ArrayDeque(){
		items = (Item[]) new Object[8];
		size = 0;
		front = 0;
		back = 0;
	}

	private Item[] items;
	private int size;
	private int front;
	private int back;

	private static int RFACTOR = 2;

	private void resize(int capacity){
		Item[] toReturn = (Item[]) new Object[capacity];
		if(front <= back){
			System.arraycopy(items, front, toReturn, 0, size);
		}
		else{
			System.arraycopy(items, front, toReturn, 0, items.length - front);
			System.arraycopy(items, 0, toReturn, items.length - front, back+1);
		}
		items = toReturn;
		if(size == 0 || size == 1){
			front = 0;
			back = 0;
		}
		else{
			front = 0;
			back = size - 1;
		}
	}

	public void addFirst(Item item){
		if(size == items.length){
			resize(size * RFACTOR);
		}
		if(front == 0 && !isEmpty()){
			front = items.length - 1;
		}
		else if(front == back && isEmpty()){
			;
		}
		else{
			front--;
		}
		items[front] = item;
		size++;
	}

	public void addLast(Item item){
		if(size == items.length){
			resize(size * RFACTOR);
		}
		if(isEmpty()){
			;
		}
		else{
			back = (back + 1) % items.length;
		}
		items[back] = item;
		size++;
		
	}

	public boolean isEmpty(){
		if(size == 0){
			return true;
		}
		else{
			return false;
		}
	}

	public int size(){
		return size;
	}

	public void printDeque(){
		if(front < back){
			for(int i = front; i <= back; i++){
				System.out.print(items[i] + " ");
			}
		}
		else if(front == back){
			return ;
		}
		else{
			for(int i = front; i < items.length; i++){
				System.out.print(items[i] + " ");
			}
			for(int i = 0; i < back; i++){
				System.out.print(items[i] + " ");
			}
		}
	}

	public Item removeFirst(){
		if(size * RFACTOR == items.length){
			resize(items.length / RFACTOR);
		}
		Item temp = items[front];
		if(isEmpty()){
			return null;
		}
		else{
			items[front] = null;
			size--;
			if(isEmpty()){
				;
			}
			else{
				front = (front + 1) % items.length;
			}
		}
		return temp;
	}

	public Item removeLast(){
		if(size * RFACTOR == items.length){
			resize(items.length / RFACTOR);
		}
		Item temp = items[back];
		if(isEmpty()){
			return null;
		}
		else{
			items[back] = null;
			size--;
			if(isEmpty()){
				;
			}
			else if(back == 0){
				back = items.length - 1;
			}
			else{
				back--;
			}
		}
		return temp;
	}

	public Item get(int index){
		if(index < size){
			return items[(front + index) % items.length];
		}
		return null;
	}
}