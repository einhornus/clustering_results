public void remove(int index) {
		Node current = head;
		if (index == 0) {
			head = current.next;
			current = null;
			size--;
			return;
		}

		current = head.next;
		Node prev = head;
		int counter = 1;
		while (counter < index) {
			prev = prev.next;
			current = current.next;
			counter++;
		}
		prev.next = current.next;
		current = null;
		size--;

	}
--------------------

@Override
  public T pop() {
    if(top == null) {
      throw new EmptyStackException();
    }
    T value = (T) top.value;
    top = top.next;
    size--;
    return value;
  }
--------------------

public void enqueue(Object pObject) 
    { 
        if (pObject != null){
            Node lNewNode = new Node(pObject, null);
            if (this.isEmpty()){
                head = lNewNode;
                tail = lNewNode;
            }
            else{
                tail.setNext(lNewNode);
                tail = lNewNode;
            }
        }  
    }
--------------------

