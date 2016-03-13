package editor;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollBar;
import javafx.geometry.Orientation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Editor extends Application {
    private static int WINDOW_WIDTH = 500;
    private static int WINDOW_HEIGHT = 500;
    private static String inputFilename;

    // Define buffer which would be displayed on the window.
    private static TextBuffer buffer = new TextBuffer();
    private static Cursor cursor = new Cursor();
    private static Text displayText = new Text(0, 0, "");
    private ScrollBar scrollBar = new ScrollBar();
    //private File inputFile;

    private static double mousePressedX;
    private static double mousePressedY;

    /** An EventHandler to handle keys that get pressed. */
    private class KeyEventHandler implements EventHandler<KeyEvent> {
        private static final int STARTING_FONT_SIZE = 12;

        /** The Text to display on the screen. */
        private Text individualText;
        private Text printCursorPos;

        private int fontSize = STARTING_FONT_SIZE;
        private String fontName = "Verdana";

        KeyEventHandler(final Group root, int windowWidth, int windowHeight) {
            // Initialize some empty text and add it to root so that it will be displayed.
            individualText = new Text(0, 0, "");
            printCursorPos = new Text(0, 0, "");

            displayText.setTextOrigin(VPos.TOP);
            individualText.setTextOrigin(VPos.TOP);
            printCursorPos.setTextOrigin(VPos.TOP);
            displayText.setFont(Font.font(fontName, fontSize));
            individualText.setFont(Font.font(fontName, fontSize));
            printCursorPos.setFont(Font.font(fontName, fontSize));

            buffer.setTextHeight(individualText.getLayoutBounds().getHeight());

            // All new Nodes need to be added to the root in order to be displayed.
            root.getChildren().add(displayText);
            root.getChildren().add(printCursorPos);

            
        }

        @Override
        public void handle(KeyEvent keyEvent) {

            if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
                // Use the KEY_TYPED event rather than KEY_PRESSED for letter keys, because with
                // the KEY_TYPED event, javafx handles the "Shift" key and associated
                // capitalization.

                String characterTyped = keyEvent.getCharacter();
                

                if (characterTyped.length() > 0 && !keyEvent.isShortcutDown()) {
                    buffer.redoUpdate();
                    printCursorPos.setText("");
                    individualText.setText(characterTyped);

                    // handle when enter key is returned
                    if(characterTyped.equals("\r")) {
                        cursor.setrecHeight(individualText.getLayoutBounds().getHeight() / 2.0);
                    } else {
                        cursor.setrecHeight(individualText.getLayoutBounds().getHeight());
                    }
                    buffer.addChar(characterTyped, individualText.getLayoutBounds().getWidth(), individualText.getLayoutBounds().getHeight());
                    cursor.setXYPos(buffer.getCursorX(), buffer.getCursorY());

                    displayText.setText(buffer.makeFullSentence());
                    updateScrollBar(scrollBar);
                    
                    keyEvent.consume();
                }
                else if(characterTyped.length() == 0) {
                    buffer.redoUpdate();
                    printCursorPos.setText("");

                    String temp = buffer.deleteChar();
                    displayText.setText(buffer.makeFullSentence());

                    individualText.setText(temp);

                    // handle when enter key is returned
                    if(temp.equals("\r")) {
                        cursor.setrecHeight(individualText.getLayoutBounds().getHeight() / 2.0);
                    } else {
                        cursor.setrecHeight(individualText.getLayoutBounds().getHeight());
                    }
                    cursor.setXYPos(buffer.getCursorX(), buffer.getCursorY());
                    updateScrollBar(scrollBar);

              		keyEvent.consume();
                }
            }
            else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
                // events have a code that we can check (KEY_TYPED events don't have an associated
                // KeyCode).
                if(keyEvent.isShortcutDown()) {
                    if (keyEvent.getCode() == KeyCode.EQUALS) {
                        fontSize += 4;
                        displayText.setFont(Font.font(fontName, fontSize));
                        individualText.setFont(Font.font(fontName, fontSize));
                        if(individualText.getText().equals("\r")) {
                            cursor.setrecHeight(individualText.getLayoutBounds().getHeight() / 2.0);
                            buffer.setTextHeight(individualText.getLayoutBounds().getHeight() / 2.0);
                        } else {
                            buffer.setTextHeight(individualText.getLayoutBounds().getHeight());
                            cursor.setrecHeight(individualText.getLayoutBounds().getHeight());
                        }
                        buffer.renderingBufferWhileResizing(fontName, fontSize);
                        displayText.setText(buffer.makeFullSentence());
                        cursor.setXYPos(buffer.getCursorXPosAfterRendering(), buffer.getCursorYPosAfterRendering());
                    } else if (keyEvent.getCode() == KeyCode.MINUS) {
                        fontSize = Math.max(0, fontSize - 4);
                        displayText.setFont(Font.font(fontName, fontSize));
                        individualText.setFont(Font.font(fontName, fontSize));
                        if(individualText.getText().equals("\r")) {
                            cursor.setrecHeight(individualText.getLayoutBounds().getHeight() / 2.0);
                            buffer.setTextHeight(individualText.getLayoutBounds().getHeight() / 2.0);
                        } else {
                            buffer.setTextHeight(individualText.getLayoutBounds().getHeight());
                            cursor.setrecHeight(individualText.getLayoutBounds().getHeight());
                        }
                        buffer.renderingBufferWhileResizing(fontName, fontSize);
                        displayText.setText(buffer.makeFullSentence());
                        cursor.setXYPos(buffer.getCursorXPosAfterRendering(), buffer.getCursorYPosAfterRendering());
                    } else if(keyEvent.getCode() == KeyCode.P) {
                        displayText.setText("");
                        cursor.setrecHeight(0.0);
                        System.out.println(buffer.getCursorX() + ", " + buffer.getCursorY());
                    } else if(keyEvent.getCode() == KeyCode.Z) {
                        buffer.undoImplement();
                        displayText.setText(buffer.makeFullSentence());
                        cursor.setXYPos(buffer.getCursorX(), buffer.getCursorY());
                    } else if(keyEvent.getCode() == KeyCode.Y) {
                        buffer.redoImplement();
                        displayText.setText(buffer.makeFullSentence());
                        cursor.setXYPos(buffer.getCursorX(), buffer.getCursorY());
                    }
                } else if(keyEvent.getCode() == KeyCode.LEFT) {
                    buffer.updateCursorWithArrowLeft();
                    cursor.setXYPos(buffer.getCursorX(), buffer.getCursorY());
                } else if(keyEvent.getCode() == KeyCode.RIGHT) {
                    buffer.updateCursorWithArrowRight();
                    cursor.setXYPos(buffer.getCursorX(), buffer.getCursorY());
                } else if(keyEvent.getCode() == KeyCode.UP) {
                    buffer.updateCursorWithArrowUp();
                    cursor.setXYPos(buffer.getCursorX(), buffer.getCursorY());
                } else if(keyEvent.getCode() == KeyCode.DOWN) {
                    buffer.updateCursorWithArrowDown();
                    cursor.setXYPos(buffer.getCursorX(), buffer.getCursorY());
                }
            }
        }
    }

    /** An event handler that displays the current position of the mouse whenever it is clicked. */
    private class MouseClickEventHandler implements EventHandler<MouseEvent> {
        /** A Text object that will be used to print the current mouse position. */
        Text positionText;

        MouseClickEventHandler(Group root) {
            // For now, since there's no mouse position yet, just create an empty Text object.
            positionText = new Text("");
            // We want the text to show up immediately above the position, so set the origin to be
            // VPos.BOTTOM (so the x-position we assign will be the position of the bottom of the
            // text).
            positionText.setTextOrigin(VPos.BOTTOM);

            // Add the positionText to root, so that it will be displayed on the screen.
            root.getChildren().add(positionText);
        }


        @Override
        public void handle(MouseEvent mouseEvent) {
            // get mouse position which I clicked.
            mousePressedX = mouseEvent.getX();
            mousePressedY = mouseEvent.getY();

            // adjust position of cursor.
            buffer.updateCursorWithMouseClicking(mousePressedX - 5, mousePressedY - 5);
            cursor.setXYPos(buffer.getCursorX(), buffer.getCursorY());
        }
    }
    
    // update the setting of scroll bar which we can see the display in the winodw console.
    public void updateScrollBar(ScrollBar scrollBar) {
        // display text 의 높이가 scroll bar의 높이보다 낮으면 가만 았음

        // 초과하면 setMax를 늘려라, 그와 동시에 setmin도 늘려야 될 것임(?)
        if(displayText.getLayoutBounds().getHeight() <= scrollBar.getLayoutBounds().getHeight()) {
            scrollBar.setMin(0);
            scrollBar.setMax(0);
        } else {
            scrollBar.setMax(displayText.getLayoutBounds().getHeight() - WINDOW_HEIGHT);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a Node that will be the parent of all things displayed on the screen.
        Group root = new Group();
        root.setLayoutX(5.0);
        // The Scene represents the window: its height and width will be the height and width
        // of the window displayed.
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);

        double usableScreenWidth = WINDOW_WIDTH - scrollBar.getLayoutBounds().getWidth();
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setPrefHeight(WINDOW_HEIGHT);
        scrollBar.setMin(0);
        scrollBar.setMax(0);
        scrollBar.setLayoutX(usableScreenWidth);

        // To get information about what keys the user is pressing, create an EventHandler.
        // EventHandler subclasses must override the "handle" function, which will be called
        // by javafx.
        EventHandler<KeyEvent> keyEventHandler =
                new KeyEventHandler(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        // Register the event handler to be called for all KEY_PRESSED and KEY_TYPED events.
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);
        scene.setOnMouseClicked(new MouseClickEventHandler(root));

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenWidth,
                    Number newScreenWidth) {
                // Re-compute window width
                int newWinWidth = newScreenWidth.intValue();
                WINDOW_WIDTH = newWinWidth;
                scrollBar.setLayoutX(WINDOW_WIDTH - scrollBar.getLayoutBounds().getWidth());
                buffer.changeWinWidth(newWinWidth);
                buffer.renderingBuffer();
                displayText.setText(buffer.makeFullSentence());
                cursor.setXYPos(buffer.getCursorXPosAfterRendering(), buffer.getCursorYPosAfterRendering());
                updateScrollBar(scrollBar);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenHeight,
                    Number newScreenHeight) {
                // Re-compute window Height
                int newWinHeight = newScreenHeight.intValue();
                WINDOW_HEIGHT = newWinHeight;
                scrollBar.setPrefHeight(WINDOW_HEIGHT);
                buffer.changeWinHeight(newWinHeight);
                updateScrollBar(scrollBar);
            }
        });

        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                // newValue describes the value of the new position of the scroll bar. The numerical
                // value of the position is based on the position of the scroll bar, and on the min
                // and max we set above. For example, if the scroll bar is exactly in the middle of
                // the scroll area, the position will be:
                //      scroll minimum + (scroll maximum - scroll minimum) / 2
                // Here, we can directly use the value of the scroll bar to set the height of Josh,
                // because of how we set the minimum and maximum above.

                displayText.setLayoutY(-newValue.doubleValue());
                cursor.changeLayoutY(-newValue.doubleValue());
                scrollBar.setValue(newValue.doubleValue());

            }
        });
        
        // All new Nodes need to be added to the root in order to be displayed.
        root.getChildren().add(cursor.returnCursor());
        root.getChildren().add(scrollBar);

        cursor.makeCursorColorChange();

        primaryStage.setTitle(inputFilename);

        // This is boilerplate, necessary to setup the window where things are displayed.
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No filename is provided.");
            System.exit(1);
        } else if (args.length > 2) {
            System.out.println("Unaccpetable input");
            System.exit(1);
        } else if (args.length == 2) {
            System.out.println(args[1] + "printinginging");
        }
        inputFilename = args[0];

        launch(args);
    }
}
