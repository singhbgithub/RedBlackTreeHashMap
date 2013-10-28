import java.util.Random;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Basic test program for assignment 9 Summer 2013.
 * improving performance by implementing red-black trees
 * @author Clinger
 * @author Schmidt
 * @author Singh (modifications)
 */
public class TestFMap {

    /**
     * Runs the tests
     * @param args command line arguments
     */
    public static void main(String args[]) {
        TestFMap test = new TestFMap();  //TestFMap instance

        // Test with 0-argument FMap.empty().

        System.out.println("Testing 0-argument empty()");
        test.creation(0);
        test.accessors();
        test.usual();
        test.iterators(0);
        test.iterators(1);
        test.visitors();
        test.accessors();            // repeated to test for side effects
        test.usual();                // repeated to test for side effects
        test.visitors();             // repeated to test for side effects

        // Test with 1-argument FMap.empty().

        System.out.println("Testing 1-argument empty()");
        test.creation(1);
        test.accessors();
        test.usual();
        test.iterators(0);
        test.iterators(1);
        test.visitors();
        test.accessors();            // repeated to test for side effects
        test.usual();                // repeated to test for side effects
        test.visitors();             // repeated to test for side effects

        System.out.println("Testing cross-representation equality");

        test.equality();

        try {
            test.performance();
        }
        catch (ClassCastException e) {
            System.out.println("Exception thrown during performance tests:");
            System.out.println(e);
            test.assertTrue ("performance", false);
        }

        test.summarize();
    }

    /**
     * Prints a summary of the tests.
     */
    private void summarize () {
        System.out.println();
        System.out.println (totalErrors + " errors found in " +
                            totalTests + " tests.");
    }

    /**
     * Constructor for TestFMap
     */
    public TestFMap () { }

    // String objects to serve as values.

    private final String alice = "Alice";
    private final String bob = "Bob";
    private final String carol = "Carol";
    private final String dave = "Dave";

    // Integer objects to serve as keys.

    private final Integer one = 1;
    private final Integer two = 2;
    private final Integer three = 3;
    private final Integer four = 4;
    private final Integer five = 5;
    private final Integer six = 6;

    // FMap<Integer,String> objects to be created and then tested.

    private FMap<Integer,String> f0;// [ ]
    private FMap<Integer,String> f1;// [ (1 Alice) ]
    private FMap<Integer,String> f2;// [ (2 Bob) (1 Alice) ]
    private FMap<Integer,String> f3;// [ (3 Carol) (2 Bob) (1 Alice) ]
    private FMap<Integer,String> f4;// [ (4 Dave) (3 Carol) (2 Bob) (1 Alice) ]
    private FMap<Integer,String> f5;// [ (1 Carol) (2 Bob) (1 Alice) ]
    private FMap<Integer,String> f6;// [ (3 Carol) (4 Dave) (2 Bob) (1 Alice) ]
    private FMap<Integer,String> f7;// [ (1 Alice) (2 Bob) (2 dave) (1 dave) ]
    private FMap<Integer,String> f8;// [ (2 dave) (1 dave) ]
    private FMap<Integer,String> f9;// [ (1 Alice) (1 dave) ]
    private FMap<Integer,String> f10;// [ (1 dave) (1 Alice) ]

    /**
     * A comparator for Integer values.
     */
    private static class UsualIntegerComparator
        implements Comparator<Integer> {

        /**
         * Compares its two arguments for order.
         * @param m first Integer to compare
         * @param n second Integer to compare
         * @return Returns a negative integer, zero, or a positive integer 
         *    as m is less than, equal to, or greater than n
         */
        public int compare (Integer m, Integer n) {
            return m.compareTo(n);
        }

    }

    Comparator<Integer> usualIntegerComparator
        = new UsualIntegerComparator(); //Instance of Comparator

    /**
     * Another comparator for Integer values.
     */
    private static class ReverseIntegerComparator
        implements Comparator<Integer> {

        /**
         * Compares its two arguments for order.
         * @param m first Integer to compare
         * @param n second Integer to compare
         * @return Returns a negative integer, zero, or a positive integer 
         *    as n is less than, equal to, or greater than m
         */
        public int compare (Integer m, Integer n) {
            return n.compareTo(m);
        }

    }

    Comparator<Integer> reverseIntegerComparator
        = new ReverseIntegerComparator(); //Instance of Comparator



    /**
     * Creates some FMap<Integer,String> objects.
     *
     * @param nargs argument
     *
     * If nargs is 0, then 0-argument FMap.empty() is used.
     * Otherwise the more efficient 1-argument version is used.
     */
    private void creation (int nargs) {
        creation (nargs, reverseIntegerComparator);
    }

    /**
     * Creates some FMap<Integer,String> objects.
     *
     * @param nargs argument
     * @param c Comparator<Integer>
     */
    private void creation (int nargs, Comparator<Integer> c) {
        try {
            if (nargs == 0)
                f0 = FMap.empty();
            else
                f0 = FMap.empty(c);
            f1 = f0.include(one, alice);
            f2 = f1.include(two, bob);
            f3 = f2.include(three, carol);
            f4 = f3.include(four, dave);
            f5 = f2.include(one, carol);
            f6 = f2.include(four, dave).include(three, carol);

            f7 = f0.include(one, dave);
            f7 = f7.include(two, dave);
            f7 = f7.include(two, bob);
            f7 = f7.include(one, alice);

            f8 = f0.include(two, bob);
            f8 = f8.include(one, alice);

            f9 = f0.include(one, dave);
            f9 = f9.include(one, alice);

            f10 = f0.include(one, alice);
            f10 = f10.include(one, dave);
        }
        catch (Exception e) {
            System.out.println("Exception thrown during creation tests:");
            System.out.println(e);
            assertTrue ("creation", false);
        }
    }

    /**
     * Tests the accessors.
     */
    private void accessors () {
        try {
            //Testing isEmpty()
            assertTrue ("empty", f0.isEmpty());
            assertFalse ("nonempty", f1.isEmpty());
            assertFalse ("nonempty", f3.isEmpty());

            //Testing size()
            assertTrue ("f0.size()", f0.size() == 0);
            assertTrue ("f1.size()", f1.size() == 1);
            assertTrue ("f2.size()", f2.size() == 2);
            assertTrue ("f3.size()", f3.size() == 3);
            assertTrue ("f4.size()", f4.size() == 4);
            assertTrue ("f5.size()", f5.size() == 2);
            assertTrue ("f7.size()", f7.size() == 2);

            //Testing containsKey
            assertFalse ("containsKey01", f0.containsKey(one));
            assertFalse ("containsKey04", f0.containsKey(four));
            assertTrue  ("containsKey11", f1.containsKey(one));
            assertTrue  ("containsKey11new", f1.containsKey(new Integer(1)));
            assertFalse ("containsKey14", f1.containsKey(four));
            assertTrue  ("containsKey21", f2.containsKey(one));
            assertFalse ("containsKey24", f2.containsKey(four));
            assertTrue  ("containsKey31", f3.containsKey(one));
            assertFalse ("containsKey34", f3.containsKey(four));
            assertTrue  ("containsKey41", f4.containsKey(one));
            assertTrue  ("containsKey44", f4.containsKey(four));
            assertTrue  ("containsKey51", f5.containsKey(one));
            assertFalse ("containsKey54", f5.containsKey(four));

            //Testing get
            assertTrue ("get11", f1.get(one).equals(alice));
            assertTrue ("get11new", f1.get(new Integer(1)).equals(alice));
            assertTrue ("get21", f2.get(one).equals(alice));
            assertTrue ("get22", f2.get(two).equals(bob));
            assertTrue ("get31", f3.get(one).equals(alice));
            assertTrue ("get32", f3.get(two).equals(bob));
            assertTrue ("get33", f3.get(three).equals(carol));
            assertTrue ("get41", f4.get(one).equals(alice));
            assertTrue ("get42", f4.get(two).equals(bob));
            assertTrue ("get43", f4.get(three).equals(carol));
            assertTrue ("get44", f4.get(four).equals(dave));
            assertTrue ("get51c", f5.get(one).equals(carol));
            assertFalse ("get51a", f5.get(one).equals(alice));
            assertTrue ("get52", f5.get(two).equals(bob));
            assertTrue ("get71t", f7.get(one).equals("Alice"));
            assertTrue ("get72t", f7.get(two).equals(bob));
            assertFalse ("get71f", f7.get(one).equals(dave));
            assertFalse ("get72f", f7.get(two).equals(dave));

        }
        catch (Exception e) {
            System.out.println("Exception thrown during accessors tests:");
            System.out.println(e);
            assertTrue ("accessors", false);
        }
    }

    /**
     * Tests the usual overloaded methods. (toString, equals, hashCode)
     */
    private void usual () {
        try {
            //Testing toString
            assertTrue ("toString0",
                        f0.toString().equals("{...(0 keys mapped to values)...}"));
            assertTrue ("toString1",
                        f1.toString().equals("{...(1 keys mapped to values)...}"));
            assertTrue ("toString7",
                        f7.toString().equals("{...(2 keys mapped to values)...}"));

            //Testing equals
            assertTrue ("equals00", f0.equals(f0));
            assertTrue ("equals33", f3.equals(f3));
            assertTrue ("equals55", f5.equals(f5));
            assertTrue ("equals55a", f5.equals(f0.include(one, carol).include(two,bob)));
            assertTrue ("equals46", f4.equals(f6));
            assertTrue ("equals64", f6.equals(f4));
            assertTrue ("equals27", f2.equals(f7));
            assertTrue ("equals72", f7.equals(f2));
            assertFalse ("equals78", f7.equals(f0.include(one, dave).include(two,dave)));
            assertFalse ("equals1_10", f1.equals(f10));
            assertFalse ("equals25", f2.equals(f5));

            assertFalse ("equals01", f0.equals(f1));
            assertFalse ("equals02", f0.equals(f2));
            assertFalse ("equals10", f1.equals(f0));
            assertFalse ("equals12", f1.equals(f2));
            assertFalse ("equals21", f2.equals(f1));
            assertFalse ("equals23", f2.equals(f3));
            assertFalse ("equals35", f3.equals(f5));

            assertFalse ("equals0string", f0.equals("just a string"));
            assertFalse ("equals4string", f4.equals("just a string"));

            assertFalse ("equals0null", f0.equals(null));
            assertFalse ("equals1null", f1.equals(null));

            //Testing hashCode
            assertTrue ("hashCode00", f0.hashCode() == f0.hashCode());
            assertTrue ("hashCode44", f4.hashCode() == f4.hashCode());
            assertTrue ("hashCode46", f4.hashCode() == f6.hashCode());
            assertTrue ("hashCode27", f2.hashCode() == f7.hashCode());

            //Probabilistic Testing
            probabilisticTests();
        }
        catch (Exception e) {
            System.out.println("Exception thrown during usual tests:");
            System.out.println(e);
            assertTrue ("usual", false);
        }
    }


    /**
     * Tests the 0-argument or 1-argument iterator method,
     * as determined by nargs.
     */
    private void iterators (int nargs) {
        if (nargs == 0)
            iterators();
        else
            iteratorsSorted();
    }

    /**
     * Tests iterators
     */
    private void iterators () {
        try {
            FMap<Integer,String> f;
            FMap<Integer,String> m;
            Iterator<Integer> it;
            int count;
            
            f = f0;
            m = f0;
            it = f.iterator();
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator001", f0.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count001", f.size() == count);
            
            f = f0;
            m = f0;
            count = 0;
            for (Integer k : f) {
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator001", f0.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count001", f.size() == count);
            
            f = f5;
            m = f0;
            it = f.iterator();
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator551", f5.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count551", f.size() == count);
            
            f = f5;
            m = f0;
            count = 0;
            for (Integer k : f) {
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator551", f5.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count551", f.size() == count);
            
            f = f6;
            m = f0;
            it = f.iterator();
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator461", f4.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count461", f.size() == count);
            
            f = f6;
            m = f0;
            count = 0;
            for (Integer k : f) {
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator461", f4.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count461", f.size() == count);
            
            f = f4;
            m = f0;
            it = f.iterator();
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator641", f6.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count641", f.size() == count);
            
            f = f4;
            m = f0;
            count = 0;
            for (Integer k : f) {
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator641", f6.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count641", f.size() == count);
            
            f = f7;
            m = f0;
            it = f.iterator();
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator271", f2.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count271", f.size() == count);
            
            f = f7;
            m = f0;
            count = 0;
            for (Integer k : f) {
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator271", f2.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count271", f.size() == count);
            
            f = f8;
            m = f0;
            it = f.iterator();
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator881", f8.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count881a", f.size() == count);
            assertTrue ("count881b", f.size() == f8.size());
            
            f = f8;
            m = f0;
            count = 0;
            for (Integer k : f) {
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator881", f8.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count881a", f.size() == count);
            assertTrue ("count881b", f.size() == f8.size());
            
            f = f2;
            m = f0;
            it = f.iterator();
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator721", f7.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count721", f.size() == count);
            
            f = f2;
            m = f0;
            count = 0;
            for (Integer k : f) {
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator721", f7.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count721", f.size() == count);
            
            // Make sure the next() method throws the right exception.

            try {
                it.next();
                assertTrue ("next (exception)", false);
            }
            catch (NoSuchElementException e) {
                assertTrue ("next (exception)", true);
            }
            catch (Exception e) {
                assertTrue ("next (exception)", false);
            }

            // Make sure the remove() method throws the right exception.

            try {
                it = f.iterator();
                it.remove();
                assertTrue ("remove", false);
            }
            catch (UnsupportedOperationException e) {
                assertTrue ("remove", true);
            }
            catch (Exception e) {
                assertTrue ("remove", false);
            }
        }
        catch (Exception e) {
            System.out.println("Exception thrown during iterator tests:");
            System.out.println(e);
            assertTrue ("iterators", false);
        }
    }

    /**
     * Tests sorted iterators
     */
    private void iteratorsSorted () {
        try {
            FMap<Integer,String> f;
            FMap<Integer,String> m;
            Iterator<Integer> it;
            int count;

            f = f0;
            m = f0;
            it = f.iterator(usualIntegerComparator);
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator001", f0.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count001", f.size() == count);

            f = f5;
            m = f0;
            it = f.iterator(reverseIntegerComparator);
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator551", f5.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count551", f.size() == count);

            f = f6;
            m = f0;
            it = f.iterator(usualIntegerComparator);
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator461", f4.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count461", f.size() == count);

            f = f4;
            m = f0;
            it = f.iterator(reverseIntegerComparator);
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator641", f6.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count641", f.size() == count);

            f = f7;
            m = f0;
            it = f.iterator(usualIntegerComparator);
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator271", f2.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count271", f.size() == count);

            f = f2;
            m = f0;
            it = f.iterator(reverseIntegerComparator);
            count = 0;
            while (it.hasNext()) {
                Integer k = it.next();
                m = m.include(k, f.get(k));
                count = count + 1;
            }
            assertTrue ("iterator721", f7.equals(m));
            assertFalse ("iteratorSanity", it.hasNext());
            assertTrue ("count721", f.size() == count);

            // The keys must be generated in ascending order,
            // as determined by the comparator.

            it = f6.iterator(reverseIntegerComparator);
            Integer previous = it.next();
            while (it.hasNext()) {
                Integer k = it.next();
                int comparison
                    = reverseIntegerComparator.compare(previous, k);
                assertTrue("ascending", comparison < 0);
                previous = k;
            }

            it = f6.iterator(usualIntegerComparator);
            previous = it.next();
            while (it.hasNext()) {
                Integer k = it.next();
                int comparison
                    = usualIntegerComparator.compare(previous, k);
                assertTrue("ascending2", comparison < 0);
                previous = k;
            }

            // Make sure the next() method throws the right exception.

            try {
                it.next();
                assertTrue ("next (exception)", false);
            }
            catch (NoSuchElementException e) {
                assertTrue ("next (exception)", true);
            }
            catch (Exception e) {
                assertTrue ("next (exception)", false);
            }

            // Make sure the remove() method throws the right exception.

            try {
                it = f.iterator(reverseIntegerComparator);
                it.remove();
                assertTrue ("remove", false);
            }
            catch (UnsupportedOperationException e) {
                assertTrue ("remove", true);
            }
            catch (Exception e) {
                assertTrue ("remove", false);
            }
        }
        catch (Exception e) {
            System.out.println("Exception thrown during iterator tests:");
            System.out.println(e);
            assertTrue ("iterators", false);
        }
    }


    /**
     * Tests the accept method.
     */
    private void visitors () {
        try {
            Visitor<Integer,String> v1
                = new Visitor<Integer,String>() {
                public String visit (Integer k, String v) {
                    return v;
                }
            };

            assertTrue ("accept001", f0.equals(f0.accept(v1)));
            assertTrue ("accept551", f5.equals(f5.accept(v1)));
            assertTrue ("accept461", f4.equals(f6.accept(v1)));
            assertTrue ("accept641", f6.equals(f4.accept(v1)));
            assertTrue ("accept281", f2.equals(f8.accept(v1))); 
  
            Visitor<Integer,String> v2
                = new Visitor<Integer,String>() {
                public String visit (Integer k, String v) {
                    return v + v;
                }
            };

            assertTrue ("accept7,2,1",
                        f7.accept(v2).get(one).equals("AliceAlice"));  
            assertTrue ("accept7,2,2",
                        f7.accept(v2).get(two).equals("BobBob")); 
            assertTrue ("accept7,2size",
                        f7.accept(v2).size() == 2);
        }
        catch (Exception e) {
            System.out.println("Exception thrown during accept tests:");
            System.out.println(e);
            assertTrue ("accept", false);
        }
    }


    /**
     * Tests equality of FMap values that were created using
     * 0-argument and 1-argument empty, as well as different
     * comparators.
     *
     * Precondition:
     *     this.f0 through this.f7 have already been initialized
     *     using 1-argument empty(_).
     */
    private void equality () {

        // According to the precondition, these should be red-black trees.

        FMap<Integer,String> f0 = this.f0;
        FMap<Integer,String> f1 = this.f1;
        FMap<Integer,String> f2 = this.f2;
        FMap<Integer,String> f3 = this.f3;
        FMap<Integer,String> f4 = this.f4;
        FMap<Integer,String> f5 = this.f5;
        FMap<Integer,String> f6 = this.f6;
        FMap<Integer,String> f7 = this.f7;

        creation(0);

        try {
            //Testing equals
            assertTrue ("equals00", this.f0.equals(f0));
            assertTrue ("equals33", this.f3.equals(f3));
            assertTrue ("equals55", this.f5.equals(f5));
            assertTrue ("equals55a", this.f5.equals(f0.include(one, carol).include(two,bob)));
            assertTrue ("equals46", this.f4.equals(f6));
            assertTrue ("equals64", this.f6.equals(f4));
            assertTrue ("equals27", this.f2.equals(f7));
            assertTrue ("equals72", this.f7.equals(f2));
            assertFalse ("equals78", this.f7.equals(f0.include(one, dave).include(two,dave)));
            assertFalse ("equals1_10", this.f1.equals(f10));
            assertFalse ("equals25", this.f2.equals(f5));

            assertTrue ("equals00", f0.equals(this.f0));
            assertTrue ("equals33", f3.equals(this.f3));
            assertTrue ("equals55", f5.equals(this.f5));
            assertTrue ("equals55a", f5.equals(this.f0.include(one, carol).include(two,bob)));
            assertTrue ("equals46", f4.equals(this.f6));
            assertTrue ("equals64", f6.equals(this.f4));
            assertTrue ("equals27", f2.equals(this.f7));
            assertTrue ("equals72", f7.equals(this.f2));
            assertFalse ("equals78", f7.equals(this.f0.include(one, dave).include(two,dave)));
            assertFalse ("equals1_10", f1.equals(this.f10));
            assertFalse ("equals25", f2.equals(this.f5));

            assertFalse ("equals01", this.f0.equals(f1));
            assertFalse ("equals02", this.f0.equals(f2));
            assertFalse ("equals10", this.f1.equals(f0));
            assertFalse ("equals12", this.f1.equals(f2));
            assertFalse ("equals21", this.f2.equals(f1));
            assertFalse ("equals23", this.f2.equals(f3));
            assertFalse ("equals35", this.f3.equals(f5));

            assertFalse ("equals01", f0.equals(this.f1));
            assertFalse ("equals02", f0.equals(this.f2));
            assertFalse ("equals10", f1.equals(this.f0));
            assertFalse ("equals12", f1.equals(this.f2));
            assertFalse ("equals21", f2.equals(this.f1));
            assertFalse ("equals23", f2.equals(this.f3));
            assertFalse ("equals35", f3.equals(this.f5));

            assertFalse ("equals0string", f0.equals("just a string"));
            assertFalse ("equals4string", f4.equals("just a string"));

            assertFalse ("equals0null", f0.equals(null));
            assertFalse ("equals1null", f1.equals(null));

            //Testing hashCode
            assertTrue ("hashCode00", f0.hashCode() == f0.hashCode());
            assertTrue ("hashCode44", f4.hashCode() == f4.hashCode());
            assertTrue ("hashCode46", f4.hashCode() == f6.hashCode());
            assertTrue ("hashCode27", f2.hashCode() == f7.hashCode());

            // Initialize this.f0 through this.f7 
            // that use a different comparator.

            creation (1, usualIntegerComparator);

            //Testing equals
            assertTrue ("equals00", this.f0.equals(f0));
            assertTrue ("equals33", this.f3.equals(f3));
            assertTrue ("equals55", this.f5.equals(f5));
            assertTrue ("equals55a", this.f5.equals(f0.include(one, carol).include(two,bob)));
            assertTrue ("equals46", this.f4.equals(f6));
            assertTrue ("equals64", this.f6.equals(f4));
            assertTrue ("equals27", this.f2.equals(f7));
            assertTrue ("equals72", this.f7.equals(f2));
            assertFalse ("equals78", this.f7.equals(f0.include(one, dave).include(two,dave)));
            assertFalse ("equals1_10", this.f1.equals(f10));
            assertFalse ("equals25", this.f2.equals(f5));

            assertTrue ("equals00", f0.equals(this.f0));
            assertTrue ("equals33", f3.equals(this.f3));
            assertTrue ("equals55", f5.equals(this.f5));
            assertTrue ("equals55a", f5.equals(this.f0.include(one, carol).include(two,bob)));
            assertTrue ("equals46", f4.equals(this.f6));
            assertTrue ("equals64", f6.equals(this.f4));
            assertTrue ("equals27", f2.equals(this.f7));
            assertTrue ("equals72", f7.equals(this.f2));
            assertFalse ("equals78", f7.equals(this.f0.include(one, dave).include(two,dave)));
            assertFalse ("equals1_10", f1.equals(this.f10));
            assertFalse ("equals25", f2.equals(this.f5));

            assertFalse ("equals01", this.f0.equals(f1));
            assertFalse ("equals02", this.f0.equals(f2));
            assertFalse ("equals10", this.f1.equals(f0));
            assertFalse ("equals12", this.f1.equals(f2));
            assertFalse ("equals21", this.f2.equals(f1));
            assertFalse ("equals23", this.f2.equals(f3));
            assertFalse ("equals35", this.f3.equals(f5));

            assertFalse ("equals01", f0.equals(this.f1));
            assertFalse ("equals02", f0.equals(this.f2));
            assertFalse ("equals10", f1.equals(this.f0));
            assertFalse ("equals12", f1.equals(this.f2));
            assertFalse ("equals21", f2.equals(this.f1));
            assertFalse ("equals23", f2.equals(this.f3));
            assertFalse ("equals35", f3.equals(this.f5));

            assertFalse ("equals0string", f0.equals("just a string"));
            assertFalse ("equals4string", f4.equals("just a string"));

            assertFalse ("equals0null", f0.equals(null));
            assertFalse ("equals1null", f1.equals(null));

            //Testing with comparator and without
            assertTrue("equalsReverseIntegerComparator", 
                       FMap.empty().include(1,"test")
                       .equals(FMap.empty(reverseIntegerComparator)
                               .include(1,"test")));
            assertTrue("equalsReverseIntegerComparator2", 
                       FMap.empty().include(2,"two").include(1,"test")
                       .equals(FMap.empty(reverseIntegerComparator)
                               .include(1,"test").include(2,"two")));

            //Testing hashCode
            assertTrue ("hashCode00", f0.hashCode() == f0.hashCode());
            assertTrue ("hashCode44", f4.hashCode() == f4.hashCode());
            assertTrue ("hashCode46", f4.hashCode() == f6.hashCode());
            assertTrue ("hashCode27", f2.hashCode() == f7.hashCode());
            assertTrue("equalsReverseIntegerComparator", 
                       FMap.empty().include(1,"test").hashCode() ==
                       FMap.empty(reverseIntegerComparator)
                       .include(1,"test").hashCode());
            assertTrue("equalsReverseIntegerComparator2", 
                       FMap.empty().include(2,"two").include(1,"test")
                       .hashCode() ==
                       FMap.empty(reverseIntegerComparator)
                       .include(1,"test").include(2,"two").hashCode());
        }
        catch (Exception e) {
            System.out.println("Exception thrown during "
                               + "cross-representation equality tests:");
            System.out.println(e);
            assertTrue ("equality", false);
        }
    }

    /**
     * Probabilistic test for distribution of hash codes.
     */
    private void probabilisticTests () {
        probabilisticTests (400, 20);
        base = -2;
        probabilisticTests (400, 20);
        base = 412686306;
        probabilisticTests (400, 20);
    }

    // random number generator, initialed by probabilisticTests()
    Random rng;

    int base = 0;   // base for Frob hash codes

    private void initializeRNG () {
        rng = new Random(1505976868);
    }

    private void initializeRNGrandomly () {
        rng = new Random(System.nanoTime());
    }

    /**
     * Generate n random pairs of unequal FMap<K,V> values.
     * If k or more have the same hash code, then report failure.
     *
     * @param n number of random pairs
     * @param k number to report failue
     */
    private void probabilisticTests (int n, int k) {
        initializeRNGrandomly();
        int sameHash = 0;
        int i = 0;
        while (i < n) {
            FMap<Frob,Frob> f1 = randomFMap();
            FMap<Frob,Frob> f2 = randomFMap();
            if (! (f1.equals(f2))) {
                i = i + 1;
                if (f1.hashCode() == f2.hashCode())
                    sameHash = sameHash + 1;
            }
        }
        //System.out.println ("Same Hash: "+sameHash + " / " + n);
        if (sameHash >= k)
            assertTrue ("hashCode quality", 0 == sameHash);
    }

    /**
     * Returns a randomly selected FMap<Frob,Frob>.
     */
    private FMap<Frob,Frob> randomFMap () {
        // First pick the size.
        double x = rng.nextDouble();
        double y = 0.5;
        int size = 0;
        while (y > x) {
            size = size + 1;
            y = y / 2.0;
        }
        FMap<Frob,Frob> f = FMap.empty();
        while (f.size() < size)
            f = f.include (randomFrob(), randomFrob());
        return f;
    }

    /**
     * Returns a randomly selected Frob.
     */
    private Frob randomFrob () {
        int h = base + rng.nextInt(5);
        return new Frob(h);
    }

    /**
     * Frob
     */
    private static class Frob {
        int theHash;
        Frob (int h) { theHash = h; }

        /**
         * hashCode for Frob
         * @return hashCode
         */
        public int hashCode () {
            return theHash;
        }
    }


    /**
     * Tests the asymptotic amortized performance for the average case
     * of five public operations.
     *
     * Strategy:
     *
     * For each operation, double the size and/or iteration parameters
     *     until the benchmark takes at least half a second to run.
     * Run the benchmark to make sure.
     * Multiply the size by 4 and run the benchmark again.
     * Compare with the expected increase in timing.
     */
    private static abstract class Benchmark {

        int n;            // size of a benchmark that takes at least 1 second
        long iterations;   // number of iterations for that benchmark  
        long t1n;         // time (in milliseconds) for that benchmark
        long t4n;         // time for that same benchmark with size 4n

        int n0;           // size of smallest benchmark to be tried
        long iterations0;  // number of iterations for smallest benchmark

        static final long SECOND = 1000;  // milliseconds per second

        /**
         * Uses n0 and iterations0 to compute n, iterations, t1n, t4n.
         */
        boolean run () {
            n = n0;
            iterations = iterations0;
            t1n = 0;
            while (t1n < SECOND/4) {
                t1n = run (n, iterations);
                if (t1n >= SECOND/4) {
                    t1n = run (n, iterations);
                }
                else {
                    n = 2 * n;
                    iterations = 2 * iterations;
                }
            }
            t4n = run (4*n, iterations);
            return compareToExpected();
        }

        /**
         * Returns the time (in milliseconds) to run this Benchmark
         * with size n and iters iterations.
         */
        abstract long run (int n, long iters);

        /**
         * Uses n, t1n, and t4n to determine whether the asymptotic
         * performance is acceptable.
         */
        abstract boolean compareToExpected ();

        /**
         * Helper method - for lg
         */
        double lg (double x) {
            return Math.log(x) / Math.log(2.0);
        }
    }

    /**
     * Timing m.include(k,v)
     */
    private static class TimeInclude extends Benchmark {

        private FMap<Foo,Double> m0;

        TimeInclude (FMap<Foo,Double> m0, int n0, long iterations0) {
            this.m0 = m0;
            this.n0 = n0;
            this.iterations0 = iterations0;
        }

        long run (int n, long iters) {
            FMap<Foo,Double> m = m0;
            for (int j = 0; j < n; j = j + 1){
                m = m.include(new Foo(j+n), new Double((double) j+n));
            }
            FMap<Foo,Double> m1 = m;
            long tStart = System.currentTimeMillis();
            for (long i = 0; i < iters; i = i + 1) {
                m = m1;
                for (int j = 0; j < n; j = j + 1){
                    m = m.include(new Foo(j), new Double((double) j));
                }
            }
            long tFinish = System.currentTimeMillis();
            return tFinish - tStart;
        }

        /**
         * Should run in O(lg n) time.
         */
        boolean compareToExpected () {
            System.out.println();
            System.out.println("m.include(k,v) benchmark ("
                               + iterations + " iterations): O(lg n)");
            System.out.println("    n=" + n + " in " + t1n + "ms");
            System.out.println("    n=" + (4*n) + " in " + t4n + "ms");
            double lgn  = lg((double) n);
            double lg4n = lg((double) 4*n);
            double slop = 1.5;
            return ((double) t4n)
                < slop * 4.0 * (lg4n / lgn) * ((double) t1n);
        }
    }

    /**
     * Timing m.isEmpty().
     */
    private static class TimeIsEmpty extends Benchmark {

        private FMap<Foo,Double> m0;

        TimeIsEmpty (FMap<Foo,Double> m0, int n0, long iterations0) {
            this.m0 = m0;
            this.n0 = n0;
            this.iterations0 = iterations0;
        }

        long run (int n, long iters) {
            FMap<Foo,Double> m = m0;
            for (int j = 0; j < n; j = j + 1)
                m = m.include(new Foo(j), new Double((double) j));
            long tStart = System.currentTimeMillis();
            for (long i = 0; i < iters; i = i + 1) {
                if (m.isEmpty())
                    throw new RuntimeException("incorrect isEmpty() method");
            }
            long tFinish = System.currentTimeMillis();
            return tFinish - tStart;
        }

        /**
         * Should run in O(1) time.
         */
        boolean compareToExpected () {
            System.out.println();
            System.out.println("m.isEmpty() benchmark ("
                               + iterations + " iterations): O(1)");
            System.out.println("    n=" + n + " in " + t1n + "ms");
            System.out.println("    n=" + (4*n) + " in " + t4n + "ms");
            return ((double) t4n) < 1.5 * ((double) t1n);
        }
    }

    /**
     * Timing m.size().
     */
    private static class TimeSize extends Benchmark {

        private FMap<Foo,Double> m0;

        TimeSize (FMap<Foo,Double> m0, int n0, long iterations0) {
            this.m0 = m0;
            this.n0 = n0;
            this.iterations0 = iterations0;
        }

        long run (int n, long iters) {
            FMap<Foo,Double> m = m0;
            for (int j = 0; j < n; j = j + 1)
                m = m.include(new Foo(j), new Double((double) j));
            long tStart = System.currentTimeMillis();
            for (long i = 0; i < iters; i = i + 1) {
                if (m.size() == 0)
                    throw new RuntimeException("incorrect size() method");
            }
            long tFinish = System.currentTimeMillis();
            return tFinish - tStart;
        }

        /**
         * Should run in O(1) time.
         */
        boolean compareToExpected () {
            System.out.println();
            System.out.println("m.size() benchmark ("
                               + iterations + " iterations): O(1)");
            System.out.println("    n=" + n + " in " + t1n + "ms");
            System.out.println("    n=" + (4*n) + " in " + t4n + "ms");
            return ((double) t4n) < 1.5 * ((double) t1n);
        }
    }

    /**
     * Timing m.containsKey(k)
     */
    private static class TimeContainsKey extends Benchmark {

        private FMap<Foo,Double> m0;

        TimeContainsKey (FMap<Foo,Double> m0, int n0, long iterations0) {
            this.m0 = m0;
            this.n0 = n0;
            this.iterations0 = iterations0;
        }

        long run (int n, long iters) {
            Foo f0 = new Foo(0);
            FMap<Foo,Double> m = m0.include(f0,0.0);
            for (int j = 1; j < n; j = j + 1)
                m = m.include(new Foo(j), new Double((double) j));
            long tStart = System.currentTimeMillis();
            for (long i = 0; i < iters; i = i + 1) {
                if (! m.containsKey(f0))
                    throw
                        new RuntimeException("incorrect containsKey() method");
            }
            long tFinish = System.currentTimeMillis();
            return tFinish - tStart;
        }

        /**
         * Should run in O(lg n) time.
         */
        boolean compareToExpected () {
            System.out.println();
            System.out.println("m.containsKey(k) benchmark ("
                               + iterations + " iterations): O(lg n)");
            System.out.println("    n=" + n + " in " + t1n + "ms");
            System.out.println("    n=" + (4*n) + " in " + t4n + "ms");
            //System.out.println(Foo.counter);
            double lgn  = lg((double) n);
            double lg4n = lg((double) 4*n);
            return ((double) t4n)
                < 1.25 * (lg4n / lgn) * ((double) t1n);
        }
    }
    
    /**
     * Timing m.get(k)
     */
    private static class TimeGet extends Benchmark {

        private FMap<Foo,Double> m0;

        TimeGet (FMap<Foo,Double> m0, int n0, long iterations0) {
            this.m0 = m0;
            this.n0 = n0;
            this.iterations0 = iterations0;
        }

        long run (int n, long iters) {
            Foo f0 = new Foo(0);
            FMap<Foo,Double> m = m0.include(f0,0.0);
            for (int j = 1; j < n; j = j + 1)
                m = m.include(new Foo(j), new Double((double) j));
            long tStart = System.currentTimeMillis();
            for (long i = 0; i < iters; i = i + 1) {
                if (m.get(f0) != 0.0)
                    throw
                        new RuntimeException("incorrect get() method");
            }
            long tFinish = System.currentTimeMillis();
            return tFinish - tStart;
        }

        /**
         * Should run in O(lg n) time.
         */
        boolean compareToExpected () {
            System.out.println();
            System.out.println("m.get(k) benchmark ("
                               + iterations + " iterations): O(lg n)");
            System.out.println("    n=" + n + " in " + t1n + "ms");
            System.out.println("    n=" + (4*n) + " in " + t4n + "ms");
            //System.out.println(Foo.counter);
            double lgn  = lg((double) n);
            double lg4n = lg((double) 4*n);
            return ((double) t4n)
                < 1.25 * (lg4n / lgn) * ((double) t1n);
        }
    }

    /**
     * Timing m.iterator().
     */
    private static class TimeIterator extends Benchmark {

        private FMap<Foo,Double> m0;

        TimeIterator (FMap<Foo,Double> m0, int n0, long iterations0) {
            this.m0 = m0;
            this.n0 = n0;
            this.iterations0 = iterations0;
        }

        long run (int n, long iters) {
            FMap<Foo,Double> m = m0;
            for (int j = 0; j < n; j = j + 1)
                m = m.include(new Foo(j), new Double((double) j));
            long tStart = System.currentTimeMillis();
            for (long i = 0; i < iters; i = i + 1) {
                Iterator<Foo> it = m.iterator();
                if (! (it.hasNext()))
                    throw new RuntimeException("incorrect hasNext() method");
            }
            long tFinish = System.currentTimeMillis();
            return tFinish - tStart;
        }

        /**
         * Should run in O(n) time.
         */
        boolean compareToExpected () {
            System.out.println();
            System.out.println("m.iterator() benchmark ("
                               + iterations + " iterations): O(n)");
            System.out.println("    n=" + n + " in " + t1n + "ms");
            System.out.println("    n=" + (4*n) + " in " + t4n + "ms");
            return ((double) t4n) < 6 * ((double) t1n);
        }
    }

    /**
     * Timing it.hasNext().
     */
    private static class TimeHasNext extends Benchmark {

        private FMap<Foo,Double> m0;

        TimeHasNext (FMap<Foo,Double> m0, int n0, long iterations0) {
            this.m0 = m0;
            this.n0 = n0;
            this.iterations0 = iterations0;
        }

        long run (int n, long iters) {
            FMap<Foo,Double> m = m0;
            for (int j = 0; j < n; j = j + 1)
                m = m.include(new Foo(j), new Double((double) j));
            Iterator<Foo> it = m.iterator();
            Foo whatever = null;
            long tStart = System.currentTimeMillis();
            for (long i = 0; i < iters; i = i + 1) {
                if (it.hasNext())
                    whatever = it.next();
            }
            long tFinish = System.currentTimeMillis();
            return tFinish - tStart;
        }

        /**
         * Should run in O(1) time.
         */
        boolean compareToExpected () {
            System.out.println();
            System.out.println("it.hasNext() benchmark ("
                               + iterations + " iterations): O(1)");
            System.out.println("    n=" + n + " in " + t1n + "ms");
            System.out.println("    n=" + (4*n) + " in " + t4n + "ms");
            return ((double) t4n) < 1.5 * ((double) t1n);
        }
    }

    /**
     * Timing m.accept(v)
     */
    private static class TimeAccept extends Benchmark {

        private FMap<Foo,Double> m0;

        TimeAccept (FMap<Foo,Double> m0, int n0, long iterations0) {
            this.m0 = m0;
            this.n0 = n0;
            this.iterations0 = iterations0;
        }

        long run (int n, long iters) {
            Foo f0 = new Foo(0);
            FMap<Foo,Double> m = m0.include(f0,0.0);
            for (int j = 1; j < n; j = j + 1)
                m = m.include(new Foo(j), new Double((double) j));
            Visitor<Foo,Double> v
                = new Visitor<Foo,Double>() {
                public Double visit (Foo x, Double d) {
                    return d;
                }
            };
            long tStart = System.currentTimeMillis();
            for (long i = 0; i < iters; i = i + 1) {
                if (m.accept(v).get(f0) != 0.0)
                    throw
                        new RuntimeException("incorrect accept() method");
            }
            long tFinish = System.currentTimeMillis();
            return tFinish - tStart;
        }
        /**
         * Should run in O(n*log(n)) time.
         */
        boolean compareToExpected () {
            System.out.println();
            System.out.println("m.accept(v) benchmark ("
                               + iterations + " iterations): O(n*log(n))");
            System.out.println("    n=" + n + " in " + t1n + "ms");
            System.out.println("    n=" + (4*n) + " in " + t4n + "ms");
            //System.out.println(Foo.counter);
            /* return ((double) t4n)
               < 1.5 * 4 * ((double) t1n);*/
            double lgn  = lg((double) n);
            double lg4n = lg((double) 4*n);
              //System.out.println("    factor="+(((double) t4n)/((double) t1n)));
            return ((double) t4n)
                < 1.5 * (lg4n / lgn) * 4 * ((double) t1n);
        }
    }

    private static class Foo {
        long j;     // primary state of Foo; determines equality of Foo objects
        long j0;    // low order bits of j; used by FooComparator
        long j1;    // high order bits of j; used by FooComparator
        long k;     // some standard permutation of the bits of j

        // k is used by RandomFooComparator; for the worst case, j == k

        // true means generate a worst case.
        // false means generate an average case.
        //
        // Warning!
        // Changing the value of Foo.worstCase invalidates all extant
        // Foo objects (because instances of RandomFooComparator no
        // longer compare old objects correctly)

        static boolean worstCase = false;

        public static long counter = 0; // to disable compiler optimizations

        Foo (int j) {
            this.j = j;
            this.j0 = j % 1024;
            this.j1 = j / 1024;
            this.k = randomize (j);
            //System.out.println ("Foo " + this.k);
            counter = counter + 1;
        }

        /**
         * Perfect hash function on int values.
         */
        private static long randomize (int j) {
            if (worstCase)
                return j;
            long jj = j;
            jj = jj & 0xffffffff;    // convert to unsigned
            long j0 = jj % p;        // chop to pieces: j0, j1, j2, j3, j4
            jj = jj / p;
            long j1 = jj % p;
            jj = jj / p;
            long j2 = jj % p;
            jj = jj / p;
            long j3 = jj % p;
            jj = jj / p;
            long j4 = jj % p;
            jj = jj / p;
            assert jj == 0;
            /*
              j0 = (m * j0 + a) % p;   // scramble each piece
              j1 = (m * j1 + a) % p;
              j2 = (m * j2 + a) % p;
              j3 = (m * j3 + a) % p;
              j4 = (m * j4 + a) % p;
            */
            j0 = j0 ^ 101;           // FIXME
            jj = jj + j0;            // assemble the pieces in reverse order
            jj = p * jj;
            jj = jj + j1;
            jj = p * jj;
            jj = jj + j2;
            jj = p * jj;
            jj = jj + j3;
            jj = p * jj;
            jj = jj + j4;
            return jj;
        }

        private static final int p = 1001;      // some convenient prime
        private static final int m = 731;       // some convenient multiplier
        private static final int a = 678;       // some convenient constant

        public String toString () {
            return "Foo(" + j + ", " + k + ")";
        }

        public boolean equals (Object x) {
            if (x instanceof Foo) {
                Foo f = (Foo) x;
                // FIXME
                /*if ((j == f.j) && (0 != fooComparator.compare (this, f)))
                  System.out.println ("**** " + j + " " + k + " " + f.k);*/
                return j == f.j;
            }
            else return false;
        }

        public int hashCode () { return (int) j; }
    }

    // FIXME: this is no longer used

    private static class FooComparator implements Comparator<Foo> {
        public int compare (Foo f1, Foo f2) {
            long f1j1 = f1.j1;
            long f2j1 = f2.j1;
            long f1j0 = f1.j0;
            long f2j0 = f2.j0;
            if (f1j1 < f2j1)
                return -1;
            else if (f1j1 == f2j1)
                if (f1j0 < f2j0)
                    return -1;
                else if (f1j0 == f2j0)
                    return 0;
                else
                    return +1;
            else
                return +1;
        }
    }

    private static class RandomFooComparator implements Comparator<Foo> {
        public int compare (Foo f1, Foo f2) {
            long direction = f1.k - f2.k;
            //System.out.println ("compare(Foo,Foo) " + direction);
            if (direction < 0)
                return -1;
            else if (direction == 0)
                return 0;
            else
                return +1;
        }
    }

    //    private static Comparator<Foo> fooComparator = new FooComparator();

    private static Comparator<Foo> fooComparator = new RandomFooComparator();

    private void performance () {
        System.out.println();
        System.out.println("Timing public operations...");
        FMap<Foo, Double> f0 = FMap.empty();
        FMap<Foo, Double> f0c = FMap.empty(fooComparator);

        /**
         * Tests that use 0-argument emptyMap() can be enabled
         * by changing false to true in the next line.
         */
        if (false) {
            assertTrue("include(k,v) is O(lg n)",
                       new TimeInclude(f0, 1024, 1024).run());
            assertTrue("isEmpty() is O(1)",
                       new TimeIsEmpty(f0, 1, 1024*1024).run());
            assertTrue("size() is O(1)",
                       new TimeSize(f0, 1, 1024*1024).run());
            assertTrue("containsKey(k) is O(lg n)",
                       new TimeContainsKey(f0, 1, 1024*1024).run());
            assertTrue("get(k) is O(lg n)",
                       new TimeGet(f0, 1, 1024*1024).run());
            assertTrue("iterator() is O(n)",
                       new TimeIterator(f0, 64, 32).run());
            assertTrue("hasNext() is O(1)",
                       new TimeHasNext(f0, 64, 64*1024*1024).run());
            assertTrue("accept(visitor) is O(n lg n)",
                       new TimeAccept(f0, 64, 32).run());
        }

        System.out.println ("\nAverage case:");

        assertTrue("include(k,v) is O(lg n)",
                   new TimeInclude(f0c, 1024, 1024).run());
        assertTrue("isEmpty() is O(1)",
                   new TimeIsEmpty(f0c, 1, 1024*1024).run());
        assertTrue("size() is O(1)",
                   new TimeSize(f0c, 1, 1024*1024).run());
        assertTrue("containsKey(k) is O(lg n)",
                   new TimeContainsKey(f0c, 1, 1024*1024).run());
        assertTrue("get(k) is O(lg n)",
                   new TimeGet(f0c, 1, 1024*1024).run());
        assertTrue("iterator() is O(n)",
                   new TimeIterator(f0c, 64, 32).run());
        assertTrue("hasNext() is O(1)",
                   new TimeHasNext(f0c, 64, 64*1024*1024).run());
        assertTrue("accept(visitor) is O(n lg n)",
                   new TimeAccept(f0c, 64, 32).run());

        System.out.println ("\nWorst case:");

        Foo.worstCase = true;    // invalidates all Foo objects

        assertTrue("include(k,v) is O(lg n)",
                   new TimeInclude(f0c, 1024, 1024).run());
        assertTrue("isEmpty() is O(1)",
                   new TimeIsEmpty(f0c, 1, 1024*1024).run());
        assertTrue("size() is O(1)",
                   new TimeSize(f0c, 1, 1024*1024).run());
        assertTrue("containsKey(k) is O(lg n)",
                   new TimeContainsKey(f0c, 1, 1024*1024).run());
        assertTrue("get(k) is O(lg n)",
                   new TimeGet(f0c, 1, 1024*1024).run());
        assertTrue("iterator() is O(n)",
                   new TimeIterator(f0c, 64, 32).run());
        assertTrue("hasNext() is O(1)",
                   new TimeHasNext(f0c, 64, 64*1024*1024).run());
        assertTrue("accept(visitor) is O(n lg n)",
                   new TimeAccept(f0c, 64, 32).run());


    }




    ////////////////////////////////////////////////////////////////

    private int totalTests = 0;       // tests run so far
    private int totalErrors = 0;      // errors so far

    /**
     * For anonymous tests.  Deprecated.
     * @param result to test
     */
    private void assertTrue (boolean result) {
        assertTrue ("anonymous", result);
    }

    /**
     * Prints failure report if the result is not true.
     * @param name name of test
     * @param result result to test
     */
    private void assertTrue (String name, boolean result) {
        if (! result) {
            System.out.println ();
            System.out.println ("***** Test failed ***** "
                                + name + ": " +totalTests);
            totalErrors = totalErrors + 1;
        }/*else{
           System.out.println ("^^^^ Test passed ^^^^ "
           + name + ": " +totalTests);
           }*/
        totalTests = totalTests + 1;
    }

    /**
     * For anonymous tests.  Deprecated.
     * @return result to test
     */
    private void assertFalse (boolean result) {
        assertTrue (! result);
    }

    /**
     * Prints failure report if the result is not false.
     * @param name name of test
     * @param result result to test
     */
    private void assertFalse (String name, boolean result) {
        assertTrue (name, ! result);
    }

}
