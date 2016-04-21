package editor;

import javafx.scene.shape.Rectangle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Cursor {
	private static Rectangle cursor;

	public Cursor() {
		cursor = new Rectangle(0, 0, 1, 0);
	}

	// set x and y position of ractangle
	public void setXYPos(double x, double y) {
        cursor.setX(x);
        cursor.setY(y);
	}

	// set height of ractangle
	public void setrecHeight(double h) {
		cursor.setHeight(h);
	}

	public static Rectangle returnCursor() {
		return cursor;
	}

    public void changeLayoutY(double value) {
        cursor.setLayoutY(value);
    }

	/** An EventHandler to handle changing the color of the rectangle. */
    private class CursorBlinkEventHandler implements EventHandler<ActionEvent> {
        private int currentColorIndex = 0;
        private Color[] boxColors = {Color.BLACK, Color.WHITE};

        CursorBlinkEventHandler() {
            // Set the color to be the first color in the list.
            changeColor();
        }

        private void changeColor() {
            cursor.setFill(boxColors[currentColorIndex]);
            currentColorIndex = (currentColorIndex + 1) % boxColors.length;
        }

        @Override
        public void handle(ActionEvent event) {
            changeColor();
        }
    }

    /** Makes the cursor change color periodically. */
    public void makeCursorColorChange() {
        // Create a Timeline that will call the "handle" function of CursorBlinkEventHandler
        // every 0.5 second.
        final Timeline timeline = new Timeline();
        // The rectangle should continue blinking forever.
        timeline.setCycleCount(Timeline.INDEFINITE);
        CursorBlinkEventHandler cursorChange = new CursorBlinkEventHandler();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), cursorChange);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}