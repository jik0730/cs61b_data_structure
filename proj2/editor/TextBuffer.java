package editor;

import java.util.Deque;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Stack;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class TextBuffer {
	public class BufferHelper {
		public double x;
		public double y;
		public double width;
		public double height;
		public String letter;

		public BufferHelper(double _x, double _y, double _width, double _height, String _letter) {
			x = _x;
			y = _y;
            width = _width;
            height = _height;
			letter = _letter;
		}
	}

	public class UndoRedoHelper {
		public BufferHelper item;
		public int x;
		public int y;
		public int whetherAddORDelete;

		public UndoRedoHelper(BufferHelper _item, int _x, int _y, int _whetherAddORDelete) {
			item = _item;
			x = _x;
			y = _y;
			whetherAddORDelete = _whetherAddORDelete;
		}
	}

	private static ArrayList<LinkedList<BufferHelper>> buffer;
	private static Deque<UndoRedoHelper> undoQueue;
	private static Stack<UndoRedoHelper> redoStack;
	private static int currentXPos;
	private static int currentYPos;
	private static double cursorX = 0;
	private static double cursorY = 0;
	private static double windowWidth = 500;
	private static double windowHeight = 500;

	private static double textHeight = 0;

	// constructor
	public TextBuffer() {
		buffer = new ArrayList<LinkedList<BufferHelper>>();
        buffer.add(new LinkedList<BufferHelper>());
        undoQueue = new LinkedList<UndoRedoHelper>();
        redoStack = new Stack<UndoRedoHelper>();
		currentXPos = 0;
		currentYPos = -1;
	}

	public void setTextHeight(double _textHeight) {
		textHeight = _textHeight;
	}
    public void setPosition(int x, int y) {
        currentXPos = x;
        currentYPos = y;
    }
	// add letter and update cursor
	public void addChar(String input, double letterLengthX, double letterLengthY) {
		if(cursorX + letterLengthX > windowWidth - 22.0) {
			cursorX = 0;
			updateCursorY(letterLengthY);
			buffer.get(currentXPos).add(currentYPos + 1, new BufferHelper(cursorX, cursorY, 0, letterLengthY, "\r"));
            currentYPos += 1;

			updateCursorX(letterLengthX);
			buffer.get(currentXPos).add(currentYPos + 1, new BufferHelper(cursorX, cursorY, letterLengthX, letterLengthY, input));
			currentYPos += 1;
            if(buffer.get(currentXPos).size() != currentYPos + 1) {
                if(buffer.get(currentXPos).get(currentYPos + 1).letter.equals("\r")) {
                    buffer.get(currentXPos).remove(currentYPos + 1);
                }
            }
            
            renderingBuffer();
            cursorX = buffer.get(currentXPos).get(currentYPos).x;
            cursorY = buffer.get(currentXPos).get(currentYPos).y;
            if(redoStack.size() == 0) {
                undoUpdate(new UndoRedoHelper(new BufferHelper(cursorX, cursorY, letterLengthX, letterLengthY, input), currentXPos, currentYPos, 1));
            }
			return ;
		}

		
		if(currentYPos != buffer.get(currentXPos).size() - 1 && input.equals("\r")) {
            buffer.add(currentXPos + 1, new LinkedList<BufferHelper>());
            int location = currentYPos + 1;
            int size = buffer.get(currentXPos).size();
            currentXPos += 1;
            currentYPos = 0;
            cursorX = 0;
            letterLengthY = textHeight;
			updateCursorX(letterLengthX);
			updateCursorY(letterLengthY);
			buffer.get(currentXPos).add(currentYPos, new BufferHelper(cursorX, cursorY, letterLengthX, letterLengthY, input));
			for(int i = location; i < buffer.get(currentXPos - 1).size(); i++) {
				buffer.get(currentXPos).add(i - location + 1, new BufferHelper(buffer.get(currentXPos - 1).get(i).x, 
					buffer.get(currentXPos - 1).get(i).y, buffer.get(currentXPos - 1).get(i).width, buffer.get(currentXPos - 1).get(i).height, buffer.get(currentXPos - 1).get(i).letter));
            }
            for(int i = location; i < size; i++) {
            	buffer.get(currentXPos - 1).removeLast();
            }
            renderingBuffer();
            
		    return ;

		} else if(input.equals("\r")) {
			buffer.add(currentXPos + 1, new LinkedList<BufferHelper>());
			currentXPos += 1;
			currentYPos = -1;
			cursorX = 0;
			letterLengthY = textHeight;
			updateCursorX(letterLengthX);
			updateCursorY(letterLengthY);
		}
        updateCursorX(letterLengthX);
		buffer.get(currentXPos).add(currentYPos + 1, new BufferHelper(cursorX, cursorY, letterLengthX, letterLengthY, input));
		currentYPos += 1;

		// rendering
		renderingBuffer();
        cursorX = buffer.get(currentXPos).get(currentYPos).x;
        cursorY = buffer.get(currentXPos).get(currentYPos).y;
        if(redoStack.size() == 0) {
            undoUpdate(new UndoRedoHelper(new BufferHelper(cursorX, cursorY, letterLengthX, letterLengthY, input), currentXPos, currentYPos,1));
        }
	}

	// delete char on the current position
	public String deleteChar() {
		if(currentYPos == -1 && currentXPos == 0) {
			return "";
		}
		else if(currentYPos == 0 && currentXPos != 0) {
			LinkedList<BufferHelper> temp = buffer.remove(currentXPos);
			currentXPos -= 1;
			currentYPos = buffer.get(currentXPos).size() - 1;
            if(buffer.get(currentXPos).size() == 0) {
                cursorX = 0;
                cursorY = 0;
            } else {
                cursorX = buffer.get(currentXPos).get(currentYPos).x;
                cursorY = buffer.get(currentXPos).get(currentYPos).y;
            }
            renderingBuffer();
            cursorX = buffer.get(currentXPos).get(currentYPos).x;
        	cursorY = buffer.get(currentXPos).get(currentYPos).y;
        	temp.get(0).x = cursorX;
        	temp.get(0).y = cursorY;
            if(redoStack.size() == 0) {
                undoUpdate(new UndoRedoHelper(temp.get(0), currentXPos, currentYPos, 0));
            }
			return "\r";
		}
		BufferHelper temp = buffer.get(currentXPos).remove(currentYPos);
		currentYPos -= 1;

        if(currentXPos == 0 && currentYPos == -1){
            cursorX = 0;
            cursorY = 0;
            if(buffer.get(0).size() == 0) {
                if(redoStack.size() == 0) {
                    undoUpdate(new UndoRedoHelper(temp, currentXPos, currentYPos, 0));
                }
                return temp.letter;
            }
            renderingBuffer();
            if(redoStack.size() == 0) {
                undoUpdate(new UndoRedoHelper(temp, currentXPos, currentYPos, 0));
            }
            return temp.letter;
        }
        renderingBuffer();
        cursorX = buffer.get(currentXPos).get(currentYPos).x;
        cursorY = buffer.get(currentXPos).get(currentYPos).y;
        temp.x = cursorX;
        temp.y = cursorY;
        if(redoStack.size() == 0) {
            undoUpdate(new UndoRedoHelper(temp, currentXPos, currentYPos, 0));
        }
		return temp.letter;
	}

	// implement undo function
    public void undoUpdate(UndoRedoHelper item) {
        if(undoQueue.size() == 100) {
            UndoRedoHelper temp = undoQueue.removeLast();
        	undoQueue.addFirst(item);
        } else {
        	undoQueue.addFirst(item);
        }
    }

    public void undoImplement() {
    	if(undoQueue.size() == 0) {
    		return ;
    	}
    	UndoRedoHelper temp = undoQueue.removeFirst();
    	redoStack.push(temp);
    	if(temp.whetherAddORDelete == 0) {
    		currentXPos = temp.x;
    		currentYPos = temp.y;
            addChar(temp.item.letter, temp.item.width, temp.item.height);
    	} else {
            currentXPos = temp.x;
            currentYPos = temp.y;
            String tresh = deleteChar();
    	}
    }

    // implement redo function
    public void redoUpdate() {
    	if(!redoStack.isEmpty()) {
    		redoStack.clear();
    	}
    }

    public void redoImplement() {
    	if(redoStack.size() == 0) {
    		return ;
    	}
    	UndoRedoHelper temp = redoStack.peek();
    	if(temp.whetherAddORDelete == 0) {
            if(temp.item.letter.equals("\r")) {
                currentXPos = temp.x + 1;
                temp.y = 0;
            } else if(temp.y == 0) {
                currentYPos = 1;
                currentXPos = temp.x;
            } else {
                currentYPos = temp.y + 1;
                currentXPos = temp.x;
            }
            String tresh = deleteChar();
            undoUpdate(temp);
    	} else {
            if(temp.item.letter.equals("\r")) {
                currentXPos = temp.x -1;
                currentYPos = buffer.get(currentXPos).size() - 1;
            } else if(temp.y == 0 && temp.x == 0) {
                currentXPos = 0;
                currentYPos = -1;
            }
            else if(temp.y == 0) {
                currentYPos = 0;
                currentXPos = temp.x - 1;
            } else {
                if(temp.y - 2 == -1) {
                    currentYPos = temp.y - 1;
                    currentXPos = temp.x;
                }
                else if(buffer.get(temp.x).get(temp.y - 2).x + temp.item.width > windowWidth) {
                    currentYPos = temp.y - 2;
                    currentXPos = temp.x;
                } else {
                    currentYPos = temp.y - 1;
                    currentXPos = temp.x;
                }
            }
            addChar(temp.item.letter, temp.item.width, temp.item.height);
            undoUpdate(temp);
    	}
        temp = redoStack.pop();
    }

	// get size of buffer
	public int size() {
		return buffer.size();
	}

	// make a full sentence
	public String makeFullSentence() {
		String sentence = "";
		for(int i = 0; i < this.size(); i++){
			for(int j = 0; j < buffer.get(i).size(); j++) {
                sentence = sentence + buffer.get(i).get(j).letter;
			}
        }
        return sentence;
	}

	// update cursorX
	public void updateCursorX(double x) {
        cursorX += x;
	}

	// update cursorY
	public void updateCursorY(double y) {
		cursorY += y;
	}

	// get cursorX
	public double getCursorX() {
		return cursorX;
	}

	// get cursorY
	public double getCursorY() {
		return cursorY;
	}

	// adjust cursor's position when mouse clicking occurs.
	public void adjustCursorPosWhenClick(double x, double y) {
		cursorX = x;
		cursorY = y;
	}

    // update cursor when pressing arrow left or right.
	public void updateCursorWithArrowLeft() {
		if(currentYPos > 0){
			if(cursorX == 0) {
				cursorX = buffer.get(currentXPos).get(currentYPos - 2).x;
            	cursorY = buffer.get(currentXPos).get(currentYPos - 2).y;
            	currentYPos -= 2;
			} else {
                cursorX = buffer.get(currentXPos).get(currentYPos - 1).x;
            	cursorY = buffer.get(currentXPos).get(currentYPos - 1).y;
            	currentYPos -= 1;
            }
        } else if(currentYPos == 0 && currentXPos > 0) {
        	currentXPos -= 1;
        	currentYPos = buffer.get(currentXPos).size() - 1;
            if(currentYPos == -1) {
                cursorX = 0;
                cursorY = 0;
            } else {
                cursorX = buffer.get(currentXPos).get(currentYPos).x;
                cursorY = buffer.get(currentXPos).get(currentYPos).y;
            }
        } else if(currentYPos == 0 && currentXPos == 0) {
            cursorX = 0;
            cursorY = 0;
            currentYPos -= 1;
        }
	}

	public void updateCursorWithArrowRight() {
		if(currentYPos < buffer.get(currentXPos).size() - 1){
            if(currentXPos == -1 && currentYPos == 0) {
                cursorX = buffer.get(currentXPos).get(currentYPos).x;
        	    cursorY = buffer.get(currentXPos).get(currentYPos).y;
            } else if(buffer.get(currentXPos).get(currentYPos + 1).x == 0) {
            	cursorX = buffer.get(currentXPos).get(currentYPos + 2).x;
            	cursorY = buffer.get(currentXPos).get(currentYPos + 2).y;
                currentYPos += 2;
            } else {
            	cursorX = buffer.get(currentXPos).get(currentYPos + 1).x;
            	cursorY = buffer.get(currentXPos).get(currentYPos + 1).y;
                currentYPos += 1;
            }
        } else if(currentYPos == buffer.get(currentXPos).size() - 1 && (currentXPos + 1) < buffer.size()) {
        	currentXPos += 1;
        	currentYPos = 0;
        	cursorX = buffer.get(currentXPos).get(currentYPos).x;
        	cursorY = buffer.get(currentXPos).get(currentYPos).y;
        }
	}

	public void updateCursorWithArrowUp() {
        if(currentXPos == 0 && cursorY == 0) {
        	return ;
        } else if(currentXPos == 1 && currentYPos == 0 && buffer.get(0).size() == 0) {
            currentXPos -= 1;
            currentYPos = -1;
            cursorX = 0;
            cursorY = 0;
            return ;
        } else if(currentXPos == 1 && currentYPos == 0 && buffer.get(0).size() != 0 && buffer.get(0).getLast().y == 0) {
            currentXPos -= 1;
            currentYPos = -1;
            cursorX = 0;
            cursorY = 0;
            return ;
        } else if(buffer.get(currentXPos).get(0).y != buffer.get(currentXPos).get(currentYPos).y) {
            if(currentXPos == 0 && buffer.get(currentXPos).get(currentYPos - 1).y == 0) {
                currentYPos = -1;
                cursorX = 0;
                cursorY = 0;
                return ;
            }
            int wannaFind = currentYPos;
            for(int i = currentYPos - 1; i >= 0; i--) {
                if(buffer.get(currentXPos).get(currentYPos).y != buffer.get(currentXPos).get(i).y) {
                    wannaFind = i;
                    break;
                }
            }
            double compareWith2 = buffer.get(currentXPos).get(currentYPos).x;
            double temp2 = Math.abs(buffer.get(currentXPos).get(currentYPos).x - buffer.get(currentXPos).get(wannaFind).x);
            int expectedXindex2 = wannaFind;
            for(int i = wannaFind - 1; i >= 0; i--) {
                if(temp2 > Math.abs(compareWith2 - buffer.get(currentXPos).get(i).x)) {
                    temp2 = Math.abs(compareWith2 - buffer.get(currentXPos).get(i).x);
                    expectedXindex2 = i;
                }
                if(buffer.get(currentXPos).get(i).letter.equals("\r")) {
                    break;
                }
            }
            currentYPos = expectedXindex2;
            cursorX = buffer.get(currentXPos).get(currentYPos).x;
            cursorY = buffer.get(currentXPos).get(currentYPos).y;
            return ;
        }

        double compareWith = buffer.get(currentXPos).get(currentYPos).x;
        double temp = Math.abs(buffer.get(currentXPos).get(currentYPos).x - buffer.get(currentXPos - 1).get(buffer.get(currentXPos - 1).size() - 1).x);
        int expectedXindex = buffer.get(currentXPos - 1).size() - 1;
        currentXPos -= 1;
        for(int i = buffer.get(currentXPos).size() - 2; i >= 0; i--) {
            if(temp > Math.abs(compareWith - buffer.get(currentXPos).get(i).x)) {
            	temp = Math.abs(compareWith - buffer.get(currentXPos).get(i).x);
                expectedXindex = i;
            }
            if(buffer.get(currentXPos).get(i).letter.equals("\r")) {
            	break;
            }
        }
        currentYPos = expectedXindex;
        cursorX = buffer.get(currentXPos).get(currentYPos).x;
        cursorY = buffer.get(currentXPos).get(currentYPos).y;
	}

	public void updateCursorWithArrowDown() {
		if(currentXPos == buffer.size() - 1 && cursorY == buffer.get(currentXPos).getLast().y) {
        	return ;
        } else if(currentXPos == 0 && currentYPos == -1 && buffer.get(0).size() == 0) {
        	if(buffer.size() == 0) {
        		return ;
        	}
            currentXPos += 1;
            currentYPos = 0;
            cursorX = buffer.get(currentXPos).get(currentYPos).x;
            cursorY = buffer.get(currentXPos).get(currentYPos).y;
            return ;
        } else if(currentXPos == 0 && currentYPos == -1 && buffer.get(0).size() != 0 && buffer.get(0).getLast().y == 0) {
            currentXPos += 1;
            currentYPos = 0;
            cursorX = buffer.get(currentXPos).get(currentYPos).x;
            cursorY = buffer.get(currentXPos).get(currentYPos).y;
            return ;
        } else if(buffer.get(currentXPos).getLast().y != buffer.get(currentXPos).get(currentYPos).y) {
            int wannaFind = currentYPos;
            for(int i = currentYPos + 1; i < buffer.get(currentXPos).size(); i++) {
                if(buffer.get(currentXPos).get(currentYPos).y != buffer.get(currentXPos).get(i).y) {
                    wannaFind = i;
                    break;
                }
            }
            double compareWith2 = buffer.get(currentXPos).get(currentYPos).x;
            double temp2 = Math.abs(compareWith2 - buffer.get(currentXPos).get(wannaFind).x);
            int expectedXindex2 = wannaFind;
            for(int i = wannaFind + 1; i < buffer.get(currentXPos).size(); i++) {
                if(temp2 > Math.abs(compareWith2 - buffer.get(currentXPos).get(i).x)) {
                	temp2 = Math.abs(compareWith2 - buffer.get(currentXPos).get(i).x);
                    expectedXindex2 = i;
                }
                if(buffer.get(currentXPos).get(i).letter.equals("\r")) {
                	break;
                }
            }
            currentYPos = expectedXindex2;
            cursorX = buffer.get(currentXPos).get(currentYPos).x;
            cursorY = buffer.get(currentXPos).get(currentYPos).y;
            return ;
        }
        double compareWith = buffer.get(currentXPos).get(currentYPos).x;
        double temp = Math.abs(compareWith - buffer.get(currentXPos + 1).get(0).x);
        int expectedXindex = 0;
        currentXPos += 1;
        for(int i = 1; i < buffer.get(currentXPos).size(); i++) {
            if(temp > Math.abs(compareWith - buffer.get(currentXPos).get(i).x)) {
            	temp = Math.abs(compareWith - buffer.get(currentXPos).get(i).x);
                expectedXindex = i;
            }
            if(buffer.get(currentXPos).get(i).letter.equals("\r")) {
            	break;
            }
        }
        currentYPos = expectedXindex;
        cursorX = buffer.get(currentXPos).get(currentYPos).x;
        cursorY = buffer.get(currentXPos).get(currentYPos).y;
	}

	// Mouse clicking
    public void updateCursorWithMouseClicking(double mousePressedX, double mousePressedY) {
    	if(buffer.isEmpty()) {
    		return ;
    	}
    	int indexY = -1;
        if(mousePressedY <= 0) {
            indexY = 0;
        }
    	int indexX = -1;
        if(mousePressedX <= 0) {
            indexX = 0;
        }
    	for(int i = 0; i < buffer.size(); i++) {
    		if(buffer.get(i).getFirst().y > mousePressedY) {
    			break;
    		}
    		indexY += 1;
    	}
    	for(int i = 0; i < buffer.get(indexY).size(); i++) {
    		if(buffer.get(indexY).get(i).x > mousePressedX && mousePressedY - buffer.get(indexY).get(i).y < textHeight && mousePressedY - buffer.get(indexY).get(i).y > 0) {
    			break;
    		}
    		indexX += 1;
    	}
        if(indexX == -1 && indexY == 0) {
            currentXPos = indexY;
            currentYPos = indexX;
            cursorX = 0;
            cursorY = 0;
            return ;
        }

        if(indexX == buffer.get(indexY).size() - 1) {
            ;
        } else {
            if (mousePressedX - buffer.get(indexY).get(indexX).x > buffer.get(indexY).get(indexX + 1).x - mousePressedX) {
                indexX += 1;
            } else {
                ;
            }
        }
    	currentXPos = indexY;
    	currentYPos = indexX;
    	cursorX = buffer.get(currentXPos).get(currentYPos).x;
    	cursorY = buffer.get(currentXPos).get(currentYPos).y;

    }

	// change windowWidth and windowHeight to make buffer resize
	public void changeWinWidth(double winX) {
		windowWidth = winX;
	}

	public void changeWinHeight(double winY) {
		windowHeight = winY;
	}

	public void renderingBuffer() {
		for(int i = 0; i < buffer.size(); i++) {
			deleteEnterKeyinSubBuffer(buffer.get(i), i);
			makeOneLine(buffer.get(i), i);
		}
		for(int i = 0; i < buffer.size(); i++) {
            int index = 0;
            if(buffer.get(i).size() != 0) {
                while(buffer.get(i).getLast().x > windowWidth - 22.0) {
                	index = insertEnterKey(buffer.get(i), index, i);
                    adjustSubBufferWhileRenderingX(buffer.get(i), buffer.get(i).get(index - 2).x, index);
                    adjustSubBufferWhileRenderingY(buffer, i + 1);
                }
            }
		}
	}

    // rendering buffer when changing font size
	public void renderingBufferWhileResizing(String fontName, int fontSize) {
        Text temp = new Text("");
        temp.setFont(Font.font(fontName, fontSize));
		for(int i = 0; i < buffer.size(); i++) {
			deleteEnterKeyinSubBuffer(buffer.get(i), i);
			makeOneLine(buffer.get(i), i);
		}
		double tempY = 0;
		for(int i = 0; i < buffer.size(); i++) {
			double tempX = 0;
			
			for(int j = 0; j < buffer.get(i).size(); j++) {
				temp = new Text(buffer.get(i).get(j).letter);
				temp.setFont(Font.font(fontName, fontSize));
				tempX += temp.getLayoutBounds().getWidth();
				buffer.get(i).get(j).x = tempX;
				buffer.get(i).get(j).y = tempY;
                buffer.get(i).get(j).width = temp.getLayoutBounds().getWidth();
                if(buffer.get(i).get(j).letter.equals("\r")) {
                    buffer.get(i).get(j).height = temp.getLayoutBounds().getHeight() / 2.0;
                } else {
                    buffer.get(i).get(j).height = temp.getLayoutBounds().getHeight();
                }
			}
            if(temp.getText().equals("\r")) {
                tempY += temp.getLayoutBounds().getHeight() / 2.0;
            } else {
                tempY += temp.getLayoutBounds().getHeight();
            }
		}
        for(int i = 0; i < buffer.size(); i++) {
            int index = 0;
            if(buffer.get(i).size() != 0) {
                while(buffer.get(i).getLast().x > windowWidth - 22.0) {
                    index = insertEnterKey(buffer.get(i), index, i);
                    adjustSubBufferWhileRenderingX(buffer.get(i), buffer.get(i).get(index - 2).x, index);
                    adjustSubBufferWhileRenderingY(buffer, i + 1);
                }
            }
        }
	}

	// get cursor after rendering
	public double getCursorXPosAfterRendering() {
		if(currentXPos == 0 && currentYPos == -1) {
			return 0.0;
		}
		cursorX = buffer.get(currentXPos).get(currentYPos).x;
		return cursorX;
	}

	public double getCursorYPosAfterRendering() {
		if(currentXPos == 0 && currentYPos == -1) {
			return 0.0;
		}
		cursorY = buffer.get(currentXPos).get(currentYPos).y;
		return cursorY;
	}

    // delete enter key (helper function)
	public void deleteEnterKeyinSubBuffer(LinkedList<BufferHelper> subBuffer, int index) {
        for(int i = 1; i < subBuffer.size(); i++) {
            if(subBuffer.get(i).letter.equals("\r")) {
            	// adjust the position of present cursor
            	if(index == currentXPos && i <= currentYPos) {
            		currentYPos -= 1;
            	}
                subBuffer.remove(i);
            }
        }
	}

    // inserting enter key (helper function)
    public int insertEnterKey(LinkedList<BufferHelper> subBuffer, int index, int currentXIndex) {
        int temp = 0;
        for(int i = index; i < subBuffer.size(); i++) {
        	temp = i;
            if(subBuffer.get(i).x > windowWidth - 22.0) {
            	// wrapping 보충 작업
            	for(int j = i; j >= index; j--) {
            		if(subBuffer.get(j).letter.equals(" ")) {
            			temp = j + 1;
            			break;
            		}
            	}
            	break;
            }
        }
        if(currentXIndex == currentXPos && temp <= currentYPos) {
        	currentYPos += 1;
        }
        subBuffer.add(temp, new BufferHelper(0, subBuffer.get(index).y + textHeight, 0, textHeight, "\r"));
        return temp + 1;
    }

    // make one line
    public void makeOneLine(LinkedList<BufferHelper> subBuffer, int index) {
    	double tempX = 0;
    	double tempY = (double) index * textHeight;
    	for(int i = 0; i < subBuffer.size(); i++) {
    		tempX += subBuffer.get(i).width;
            subBuffer.get(i).x = tempX;
            subBuffer.get(i).y = tempY;
        }
    }

    // adjust x position of buffers
    public void adjustSubBufferWhileRenderingX(LinkedList<BufferHelper> subBuffer, double tempX, int index) {
    	for(int i = index; i < subBuffer.size(); i++) {
        	subBuffer.get(i).x -= tempX;
        	subBuffer.get(i).y += textHeight;
        }
    }

    // adjust y position of buffers
    public void adjustSubBufferWhileRenderingY(ArrayList<LinkedList<BufferHelper>> buffer, int index) {
        for(int i = index; i < buffer.size(); i++) {
            for(int j = 0; j < buffer.get(i).size(); j++) {
                buffer.get(i).get(j).y += textHeight;
            }
        }
    }
}
