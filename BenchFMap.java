/** 
 *  Basic benchmark program for the FMap<K,V> ADT.
 *  Compares against java.util's TreeMap<K,V> and HashMap<K,V>.
 *
 *  Summer-1 2013
 *
 *  Usage:
 *
 *     java BenchFMap textFile nIterations nTimes nWords
 *
 *  where
 *
 *     textFile is the name of a file containing the input text
 *     nIterations is the number of times to create a Map mapping all
 *          words in textFile to the number of times they occur
 *     nTimes is the number of times to extract the words from a Map
 *     nWords is the number of common words to print at the end
 *
 *   nIterations defaults to 1.
 *   nTimes defaults to 1.
 *   nWords defaults to 10.
 *
 *  @author Clinger
 *  @author Schmidt (modifications)
 */

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Collections;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BenchFMap {

    // Runs the tests.
               
    public static void main(String args[]) {

        // Default values of command-line arguments.

        String textFile = "iliad.txt";
        int nIterations = 1;
        int nTimes = 1;
        int nWords = 10;

        // Processing of command-line arguments.

        if (args.length > 0)
            textFile = args[0];
        else {
            System.err.println ("No text file specified.");
            System.exit(1);
        }
        if (args.length > 1)
            nIterations = Integer.parseInt(args[1]);
        if (args.length > 2)
            nTimes = Integer.parseInt(args[2]);
        if (args.length > 3)
            nWords = Integer.parseInt(args[3]);

        // Create a test object and runs the tests.

        BenchFMap bench
            = new BenchFMap(textFile, nIterations, nTimes, nWords);
        bench.readText();
        bench.makeMap();
    }

    // The constructor is passed the values for command-line arguments.

    public BenchFMap (String textFile,
                      int nIterations, int nTimes, int nWords) {
        this.textFile = textFile;
        this.nIterations = nIterations;
        this.nTimes = nTimes;
        this.nWords = nWords;
    }

    // Command-line arguments.

    private String textFile;     // text file to analyze
    private int nIterations;     // number of maps to create
    private int nTimes;          // number of times to perform each benchmark
    private int nWords;          // number of common words to print

    // The comparator to be used during each search.

    private static final Comparator<String> sc
        = new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        };

    // Another comparator that could be used instead but isn't.

    private static final Comparator<String> sci
        = String.CASE_INSENSITIVE_ORDER;

    // The list of words in textFile.
    // Initialized by readText().

    ArrayList<String> theWords;

    // The FileInputStream obtained from textFile.
    // Initialized by readText().

    private java.io.InputStream input;

    // Opens the file and reads the text.

    private void readText() {
        long t0 = System.currentTimeMillis();
        try {
            FileInputStream fin = new FileInputStream (textFile);
            this.input = fin;
            readText(fin);
        }
        catch (FileNotFoundException e) {
            System.err.println (textFile + " not found.");
            System.exit(1);
        }
        long t1 = System.currentTimeMillis();
        System.out.println ("Reading of text: " + (t1 - t0) + " msec");
    }

    // Reads the text, adding each word to theWords.

    private void readText (FileInputStream fin) {
        this.theWords = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        while (scanChar() != EOF) {
            buf.setLength(0);
            while ((! (Character.isLetter(scanChar())))
                   && (scanChar() != EOF)) {
                consumeChar();
            }
            while (Character.isLetter(scanChar())) {
                buf.append (scanChar());
                consumeChar();
            }
            if (buf.length() > 0) {
                String word = buf.toString().toLowerCase();
                theWords.add(word);
            }
        }
        try {
            fin.close();
        }
        catch (IOException e) { }
    }

    // State machine for buffering characters.
    // FIXME: assumes Ascii input.

    private char    nextCharacter;
    private boolean nextCharacterIsReady = false;

    private static final char EOF = '\uffff';

    private char scanChar() {
        if (nextCharacterIsReady)
            return nextCharacter;
        else {
            try {
                int c = input.read();
                nextCharacter = (c == -1) ? EOF : (char) c;
            }
            catch (IOException e) {
                nextCharacter = EOF;
            }
            nextCharacterIsReady = true;
            return nextCharacter;
        }
    }

    private void consumeChar() {
        if (! nextCharacterIsReady)
            scanChar();
            nextCharacterIsReady = false;
    }

    ////////////////////////////////////////////////////////////////

    // Creates maps that map all words in theWords
    // to the number of times they appear in theWords,
    // and then extracts the words found from the maps.

    private void makeMap () {

        long t0 = 0;
        long t1 = 0;

        Map<String,Integer> result1 = new HashMap<String,Integer>();
        t0 = System.currentTimeMillis();
        for (int i = 0; i < nIterations; i = i + 1) {
            Map<String,Integer> m = new HashMap<String,Integer>();
            for (String word : theWords) {
                if (m.containsKey(word))
                    m.put(word, 1 + m.get(word));
                else
                    m.put(word, 1);
            }
            result1 = m;
        }
        t1 = System.currentTimeMillis();
        System.out.println ("Creating HashMap of words: "
                            + (t1 - t0) + " msec");

        Map<String,Integer> result2 = new TreeMap<String,Integer>(sc);
        t0 = System.currentTimeMillis();
        for (int i = 0; i < nIterations; i = i + 1) {
            Map<String,Integer> m = new TreeMap<String,Integer>(sc);
            for (String word : theWords) {
                if (m.containsKey(word))
                    m.put(word, 1 + m.get(word));
                else
                    m.put(word, 1);
            }
            result2 = m;
        }
        t1 = System.currentTimeMillis();
        System.out.println ("Creating TreeMap of words: "
                            + (t1 - t0) + " msec");

        FMap<String,Integer> result3 = FMap.empty(sc);
        t0 = System.currentTimeMillis();
        for (int i = 0; i < nIterations; i = i + 1) {
            FMap<String,Integer> m = FMap.empty(sc);
            for (String word : theWords) {
                if (m.containsKey(word))
                    m = m.include(word, 1 + m.get(word));
                else
                    m = m.include(word, 1);
            }
            result3 = m;
        }
        t1 = System.currentTimeMillis();
        System.out.println ("Creating FMap of words: " + (t1 - t0) + " msec");

        if (result1.size() == result2.size()
            && result1.size() == result3.size())
            System.out.println (result1.size() + " words in list");
        else
            System.out.println ("ERROR: "
                                + result1.size() + " vs "
                                + result2.size() + " vs " + result3.size());
        System.out.println();

        // Extract the keys into an ArrayList.

        ArrayList<String> uniqueWords;

        t0 = System.currentTimeMillis();
        for (int i = 0; i < nTimes; i = i + 1) {
            uniqueWords = new ArrayList<String>();
            for (String word : result1.keySet())
                uniqueWords.add (word);
        }
        t1 = System.currentTimeMillis();
        System.out.println ("Extracting words from HashMap using foreach: "
                            + (t1 - t0) + " msec");

        t0 = System.currentTimeMillis();
        for (int i = 0; i < nTimes; i = i + 1) {
            uniqueWords = new ArrayList<String>();
            for (String word : result2.keySet())
                uniqueWords.add (word);
        }
        t1 = System.currentTimeMillis();
        System.out.println ("Extracting words from TreeMap using foreach: "
                            + (t1 - t0) + " msec");

        uniqueWords = new ArrayList<String>();
        t0 = System.currentTimeMillis();
        for (int i = 0; i < nTimes; i = i + 1) {
            uniqueWords = new ArrayList<String>();
            result3.accept(new MyVisitor(uniqueWords));
        }
        t1 = System.currentTimeMillis();
        System.out.println ("Extracting words from FMap using accept: "
                            + (t1 - t0) + " msec");

        System.out.println();

        // Sort so the most common words come first.

        ArrayList<String> uniqueWords0 = new ArrayList<String>();
        uniqueWords0.addAll (uniqueWords);

        t0 = System.currentTimeMillis();
        for (int i = 0; i < nTimes; i = i + 1) {
            uniqueWords.clear();
            uniqueWords.addAll (uniqueWords0);
            Collections.sort( uniqueWords, new MapComparator(result1) );
        }
        t1 = System.currentTimeMillis();
        System.out.println ("Sorting words using counts from HashMap: "
                            + (t1 - t0) + " msec");

        t0 = System.currentTimeMillis();
        for (int i = 0; i < nTimes; i = i + 1) {
            uniqueWords.clear();
            uniqueWords.addAll (uniqueWords0);
            Collections.sort( uniqueWords, new MapComparator(result2) );
        }
        t1 = System.currentTimeMillis();
        System.out.println ("Sorting words using counts from TreeMap: "
                            + (t1 - t0) + " msec");

        t0 = System.currentTimeMillis();
        for (int i = 0; i < nTimes; i = i + 1) {
            uniqueWords.clear();
            uniqueWords.addAll (uniqueWords0);
            Collections.sort( uniqueWords, new MyComparator(result3) );
        }
        t1 = System.currentTimeMillis();
        System.out.println ("Sorting words using counts from FMap: "
                            + (t1 - t0) + " msec");

      System.out.println();

        // Extract the most common words and their frequencies.

        ArrayList<Integer> wordCounts;

        wordCounts = new ArrayList<Integer>();
        t0 = System.currentTimeMillis();
        for (int i = 0; i < nWords; i = i + 1)
            wordCounts.add (result1.get (uniqueWords.get (i)));
        t1 = System.currentTimeMillis();
        System.out.println ("Extracting the " + nWords
                            + " most common word counts from HashMap: "
                            + (t1 - t0) + " msec");

        wordCounts = new ArrayList<Integer>();
        t0 = System.currentTimeMillis();
        for (int i = 0; i < nWords; i = i + 1)
            wordCounts.add (result2.get (uniqueWords.get (i)));
        t1 = System.currentTimeMillis();
        System.out.println ("Extracting the " + nWords
                            + " most common word counts from TreeMap: "
                            + (t1 - t0) + " msec");

        wordCounts = new ArrayList<Integer>();
        t0 = System.currentTimeMillis();
        for (int i = 0; i < nWords; i = i + 1)
            wordCounts.add (result3.get (uniqueWords.get (i)));
        t1 = System.currentTimeMillis();
        System.out.println ("Extracting the " + nWords
                            + " most common word counts from FMap: "
                            + (t1 - t0) + " msec");

        System.out.println();

        for (int i = 0; i < nWords; i = i + 1) {
            String word = uniqueWords.get(i);
            int count = wordCounts.get(i);
            System.out.println (count + "  " + word);
        }

  }

    // Adds the visited words to an ArrayList.

    private static class MyVisitor implements Visitor<String,Integer> {

        ArrayList<String> words;

        MyVisitor (ArrayList<String> words) {
            this.words = words;
        }

        public Integer visit (String word, Integer count) {
            words.add(word);
            return count;
        }
    }

    // Uses the counts in an FMap<String,Integer> to compare strings.

    private static class MyComparator implements Comparator<String> {

        FMap<String,Integer> m;

        MyComparator (FMap<String,Integer> m) {
            this.m = m;
        }

        // To sort in reverse order of frequency (most common comes first).
        // Ties are broken using the usual lexicographic order on strings.

        public int compare(String word1, String word2) {
            Integer count1 = m.get(word1);
            Integer count2 = m.get(word2);
            int direction = count1.compareTo (count2);
            if (direction == 0)
                return word1.compareTo (word2);    // it was a tie
            else
                return - direction;
        }
    }

    // Uses the counts in a Map<String,Integer> to compare strings.

    private static class MapComparator implements Comparator<String> {

        Map<String,Integer> m;

        MapComparator (Map<String,Integer> m) {
            this.m = m;
        }

        // To sort in reverse order of frequency (most common comes first).

        public int compare(String word1, String word2) {
            Integer count1 = m.get(word1);
            Integer count2 = m.get(word2);
            int direction = count1.compareTo (count2);
            if (direction == 0)
                return word1.compareTo (word2);    // it was a tie
            else
                return - direction;
        }
    }
}
