import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;

/**
 * Created by Ingyo on 2016. 4. 28..
 */
public class Boggle {
    private String dictionaryPath = "/usr/share/dict/words";
    private List<String> boardPath;
    private int numOfWords = 1;
    private List<String> words;
    private PlayBoggle playBoggle;

//    public Boggle(String dic, int n, String bo) throws IOException {
//        this.dictionaryPath = dic;
//        this.numOfWords = n;
//        this.boardPath = bo;
//        this.playBoggle = new PlayBoggle(Files.readAllLines(Paths.get(boardPath)),
//                Files.readAllLines(Paths.get(dictionaryPath)));
//        this.words = this.playBoggle.play();
//    }
//
//    public Boggle(int n, String bo) throws IOException {
//        this.numOfWords = n;
//        this.boardPath = bo;
//        this.playBoggle = new PlayBoggle(Files.readAllLines(Paths.get(boardPath)),
//                Files.readAllLines(Paths.get(dictionaryPath)));
//        this.words = this.playBoggle.play();
//    }
//
//    public Boggle(String dic, String bo) throws IOException {
//        this.dictionaryPath = dic;
//        this.boardPath = bo;
//        this.playBoggle = new PlayBoggle(Files.readAllLines(Paths.get(boardPath)),
//                Files.readAllLines(Paths.get(dictionaryPath)));
//        this.words = this.playBoggle.play();
//    }
//
//    public Boggle(String bo) throws IOException {
//        this.boardPath = bo;
//        this.playBoggle = new PlayBoggle(Files.readAllLines(Paths.get(boardPath)),
//                Files.readAllLines(Paths.get(dictionaryPath)));
//        this.words = this.playBoggle.play();
//    }

    public Boggle(String dic, int n) throws IOException {
        Scanner sc = new Scanner(System.in);
        boardPath = new LinkedList<>();
        while (sc.hasNext()) {
            boardPath.add(sc.next());
        }
        this.dictionaryPath = dic;
        this.numOfWords = n;
        this.playBoggle = new PlayBoggle(boardPath,
                Files.readAllLines(Paths.get(dictionaryPath)));
        this.words = this.playBoggle.play();
    }

    public Boggle(int n) throws IOException {
        Scanner sc = new Scanner(System.in);
        boardPath = new LinkedList<>();
        while (sc.hasNext()) {
            boardPath.add(sc.next());
        }
        this.numOfWords = n;
        this.playBoggle = new PlayBoggle(boardPath,
                Files.readAllLines(Paths.get(dictionaryPath)));
        this.words = this.playBoggle.play();
    }

    public Boggle(String dic) throws IOException {
        Scanner sc = new Scanner(System.in);
        boardPath = new LinkedList<>();
        while (sc.hasNext()) {
            boardPath.add(sc.next());
        }
        this.dictionaryPath = dic;
        this.playBoggle = new PlayBoggle(boardPath,
                Files.readAllLines(Paths.get(dictionaryPath)));
        this.words = this.playBoggle.play();
    }

    public void printWords() {
        Collections.sort(words, new LengthFirstComparator());
        for (int i = 0; i < Math.min(this.numOfWords, words.size()); i += 1) {
            System.out.println(words.get(i));
        }
    }

    public class LengthFirstComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            if (o1.length() != o2.length()) {
                return -o1.length()+o2.length();
            }
            return o1.compareTo(o2);
        }
    }

    public static void main (String args[]) {
        /**  * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                       *
         * Some constraints of putting arguments should be here. *
         *                                                       *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

//        try {
//            if (args.length == 2) {
//                if (args[0].equals("<")) {
//                    Boggle boggle = new Boggle(args[1]);
//                    boggle.printWords();
//                } else {
//                    System.out.println("Incorrect input");
//                }
//            } else if (args.length == 4) {
//                if (args[0].equals("-k") && args[2].equals("<")) {
//                    Boggle boggle = new Boggle(Integer.parseInt(args[1]), args[3]);
//                    boggle.printWords();
//                } else if (args[0].equals("-d") && args[2].equals("<")) {
//                    Boggle boggle = new Boggle(args[1], args[3]);
//                    boggle.printWords();
//                } else {
//                    System.out.println("Incorrect input");
//                }
//            } else if (args.length == 6) {
//                if (args[0].equals("-k") && args[2].equals("-d") && args[4].equals("<")) {
//                    Boggle boggle = new Boggle(args[3], Integer.parseInt(args[1]), args[5]);
//                    boggle.printWords();
//                } else if (args[0].equals("-d") && args[2].equals("-k") && args[4].equals("<")) {
//                    Boggle boggle = new Boggle(args[1], Integer.parseInt(args[3]), args[5]);
//                    boggle.printWords();
//                } else {
//                    System.out.println("Incorrect input");
//                }
//            } else {
//                System.out.println("Incorrect input");
//            }
//        } catch (IOException io) {
//            System.out.println("ioexception error");
//        }

        try {
            if(args.length == 2) {
                if (args[0].equals("-k")) {
                    Boggle boggle = new Boggle(Integer.parseInt(args[1]));
                    boggle.printWords();
                } else if (args[0].equals("-d")) {
                    Boggle boggle = new Boggle(args[1]);
                    boggle.printWords();
                } else {
                    System.out.println("Incorrect input1");
                }
            } else if (args.length == 4) {
                if (args[0].equals("-k") && args[2].equals("-d")) {
                    Boggle boggle = new Boggle(args[3], Integer.parseInt(args[1]));
                    boggle.printWords();
                } else if (args[0].equals("-d") && args[2].equals("-k")) {
                    Boggle boggle = new Boggle(args[1], Integer.parseInt(args[3]));
                    boggle.printWords();
                } else {
                    System.out.println("Incorrect input2");
                }
            } else {
                System.out.println("Incorrect input3");
            }
        } catch (IOException io) {
            System.out.println("ioexception error4");
        }



        /** Play Boggle Game! */
//        PlayBoggle playBoggle = new PlayBoggle(Files.readAllLines(Paths.get(args[0])),
//                Files.readAllLines(Paths.get(args[1])));
//        words = playBoggle.play();
//
//        for (String s : words) {
//            System.out.println(s);
//        }
    }
}
