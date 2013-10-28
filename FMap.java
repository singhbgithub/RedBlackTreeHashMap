import java.util.*;

/**
 * FMap<K, V> is an immutable abstract data type whose values 
 * represent finite functions from keys of type K to values 
 * of type V.
 * @author Bhavneet Singh
 * @E-Mail singh.b@husky.neu.edu
 * @Professor This assignment was like a delicious cake
 */
public abstract class FMap<K, V> implements Iterable<K> {
    
    /**
     * empty:
     * returns a new empty FMap<K, V>
     * @return FMap<K, V> - an empty FMap<K, V>
     */
    public static <K, V> FMap<K, V> empty() {
        return new Empty<K, V>();
    }
    
    /**
     * empty:
     * returns a new empty FMap<K, V>
     * @param comp - a Comparator to sort this FMap<K, V>
     * @return - FMap<K, V> - an empty FMap<K, V> with a 
     * Comparator<? super K>
     */
    public static <K, V> FMap<K, V> empty(Comparator<? super K> c) {
        return new EmptyBST<K, V>(c);
    }
    
    /**
     * include:
     * returns a new non-empty FMap<K, V>
     * @param k0 - a key of type K to add this FMap<K, V>
     * @param v0 - a value of type V to be mapped to k0
     * @return - FMap<K, V> - a new FMap<K, V> with the 
     * new mapping of the key k0 to the value v0
     */
    public abstract FMap<K, V> include(K k0, V v0);
    
    /**
     * isEmpty:
     * tells us if this FMap<K, V> is empty
     * @return boolean - returns true if
     * this FMap<K, V> is empty
     */
    public abstract boolean isEmpty();
    
    /** 
     * size:
     * determines the size of this FMap<K, V>,
     * not counting repeated mappings of the
     * same key of type K
     * @return int - the number of distinct
     * keys in this FMap<K, V>
     */
    public abstract int size();
    
    /** containsKey:
     * determines if this FMap<K, V> contains the 
     * key k
     * @param k - the key of type K to be checked
     * is within this FMap<K, V>
     * @return boolean - returns true if k is
     * contained within this FMap<K, V>
     */
    public abstract boolean containsKey(K k);
    
    /**
     * get:
     * gives the value of type V associated to
     * the given key of type K, k, within this
     * FMap<K, V>
     * @param k - the key of type K whose value
     * is desired
     * @return V - the value associated to the
     * key k
     */
    public abstract V get(K k);
    
    /**
     * toString:
     * returns a String representation of this
     * FMap<K, V>
     * @return String - the String representation
     * of this FMap<K, V>, noting the amount of
     * distinct keys of type K mapped to values
     * of type V
     */
    public String toString() {
        return "{...(" + this.size() + " keys mapped to values)...}";
    }
    
    /**
     * equals:
     * determine if this FMap<K, V> equals the Object obj
     * @param obj - an Object to compare for equality
     * @return boolean - returns true if the this FMap<K, V>
     * equals the object obj
     */
    public boolean equals(Object obj) {
        if (obj instanceof FMap<?, ?>) {
            @SuppressWarnings("unchecked")
            FMap<K, V> that = (FMap<K, V>) obj;
            FMap<K, V> t1 = this.removeRepeatedKeys();
            FMap<K, V> t2 = that.removeRepeatedKeys();
        return t1.size() == t2.size() && 
                t1.reallyEquals(t2);
        }
        else return false;
    }
    
    /**
     * reallyEquals:
     * determine if this FMap<K, V> equals the FMap<K, V>
     * other
     * @param other - another FMap<K, V> to compare for
     * equality
     * @return boolean - returns true if the this FMap<K, V>
     * equals other
     */
    abstract boolean reallyEquals(FMap<K, V> other);
    
    /**
     * remove:
     * removes the first key of type K, k, from this FMap<K, V>
     * @param k - the key of type K to remove from this FMap<K, V>
     * @return FMap<K, V> - a new FMap<K, V> with the same mappings
     * as this one with the first instance of the key k removed. If 
     * this FMap<K, V> is empty, this method returns this FMap<K, V>.
     */
    abstract FMap<K, V> remove(K k);
    
    /**
     * removeRepeatedKeys:
     * if a key of type K has a more current mapping, this method
     * will remove all previous mappings to this key until this
     * FMap<K, V> has no duplicate bindings to the same key.
     * @return FMap<K, V> - this FMap<K, V> with duplicate bindings
     * removed and only the most current bindings preserved
     */
    abstract FMap<K, V> removeRepeatedKeys();
    
    /**
     * hashCode:
     * return this FMap<K, V>'s hash code
     * @return int - this FMap<K, V>'s hash code
     */
    public abstract int hashCode();
    
    
    /**
     * iterator:
     * creates a likely unordered iterator of type K from this
     * FMap<K, V> that has no duplicate keys listed
     * @return Iterator<K> - a likely unordered iterator of 
     * the type K
     */
    public abstract Iterator<K> iterator();
    
    /**
     * iterator:
     * creates a new ordered iterator of type K from this 
     * FMap<K, V> that has no duplicate keys listed
     * @param comp - a Comparator<K> to create an ordering to
     * sort by
     * @return Iterator<K> - an ordered iterator of the type K
     */
    public abstract Iterator<K> iterator(Comparator<? super K> comp);
    
    /**
     * sort:
     * sorts a by c 
     * @param a - an ArrayList<K> to be sorted
     * @param c - a Comparator<K> to be used in 
     * sorting a
     * @return ArrayList<K> - a sorted by the ordering
     * defined by c
     */
    ArrayList<K> sort(ArrayList<K> a, Comparator<? super K> c) {
        Collections.sort(a, c);
        return a;
    }
    
    /**
     * makeList: 
     * creates an ArrayList<K> of this FMap<K, V>
     * @param ar - an accumulation of the keys thus far,
     * it should be initialized as an empty ArrayList<K>.
     * @return ArrayList<K> - this FMap<K, V> converted
     * into an ArrayList<K> of keys
     */
    abstract ArrayList<K> makeList(ArrayList<K> ar); 
    
    /**
     * accept:
     * takes a visitor and modifies every value V bound
     * to a key K in this FMap<K, V>
     * @param visitor - the visitor from that maps values V
     * bound to keys K to new values of same type, however
     * modified by an equation
     * @return FMap<K, V> - an FMap<K, V> with the same keys
     * as this FMap<K, V> with the associated values modified
     * by the equation define by the visitor's visit method
     */
    public abstract FMap<K, V> accept(Visitor<K, V> visitor);
    
    /**
     * sumKeyValueHash:
     * sums the value of the hashcodes of every key and value in
     * this FMap<K, V>, manipulating the values through some constants
     * @return int - the sum of all the hashcodes of values and keys
     * in this FMap<K, V>
     */
    abstract int sumKeyValueHash();
    
}







/**
 * This class represents a list implementation of
 * the FMap<K, V> from the keys K to the values V
 * @param <K> - key type
 * @param <V> - value type
 */
abstract class AList<K, V> extends FMap<K, V> {
    
    /**
     * include:
     * returns a new non-empty FMap<K, V>
     * @param k0 - a key of type K to add this FMap<K, V>
     * @param v0 - a value of type V to be mapped to k0
     * @return - FMap<K, V> - a new FMap<K, V> with the 
     * new mapping of the key k0 to the value v0
     */
    public  FMap<K, V> include(K k0, V v0) {
        return new Include<K, V>(this, k0, v0); 
    }
    
    /**
     * iterator:
     * creates a likely unordered iterator of type K from this
     * FMap<K, V> that has no duplicate keys listed
     * @return Iterator<K> - a likely unordered iterator of 
     * the type K
     */
    public Iterator<K> iterator() {
        return new KIterator<K>(this.removeRepeatedKeys().makeList(
                                                new ArrayList<K> ()));
    }
    
    /**
     * iterator:
     * creates a new ordered iterator of type K from this 
     * FMap<K, V> that has no duplicate keys listed
     * @param comp - a Comparator<K> to create an ordering to
     * sort by
     * @return Iterator<K> - an ordered iterator of the type K
     */
    public Iterator<K> iterator(Comparator<? super K> comp) {
        return new KIterator<K>(this.sort(
                 this.removeRepeatedKeys().makeList(new ArrayList<K>()),
                 comp));
    }
    
    /**
     * accept:
     * takes a visitor and modifies every value V bound
     * to a key K in this FMap<K, V>
     * @param visitor - the visitor from that maps values V
     * bound to keys K to new values of same type, however
     * modified by an equation
     * @return FMap<K, V> - an FMap<K, V> with the same keys
     * as this FMap<K, V> with the associated values modified
     * by the equation define by the visitor's visit method
     */
    public FMap<K, V> accept(Visitor<K, V> visitor) {
        FMap<K, V> m2 = FMap.empty();
        for (K k : this) {
            V v = visitor.visit(k, this.get (k));
            m2 = m2.include (k, v);
        }
        return m2;
    }
    
}







/**
 * This class represents an empty FMap<K, V> 
 * whose values represent finite functions 
 * from keys of type K to values of type V.
 */
class Empty<K, V> extends AList<K, V> {
    // Empty constructor
    Empty() {}
    
    /**
     * isEmpty:
     * tells us if this FMap<K, V> is empty
     * @return boolean - returns true if
     * this FMap<K, V> is empty
     */
    public boolean isEmpty() {
        return true;
    }
    
    /** 
     * size:
     * determines the size of this FMap<K, V>,
     * not counting repeated mappings of the
     * same key of type K
     * @return int - the number of distinct
     * keys in this FMap<K, V>
     */
    public int size() {
        return 0;
    }
    
    /** containsKey:
     * determines if this FMap<K, V> contains the 
     * key k
     * @param k - the key of type K to be checked
     * is within this FMap<K, V>
     * @return boolean - returns true if k is
     * contained within this FMap<K, V>
     */
    public boolean containsKey(K k) {
        return false;
    }
    
    /**
     * get:
     * gives the value of type V associated to
     * the given key of type K, k, within this
     * FMap<K, V>
     * @param k - the key of type K whose value
     * is desired
     * @return V - the value associated to the
     * key k
     */
    public V get(K k) {
        throw new RuntimeException("Key not in mapping!");
    }
    
    /**
     * reallyEquals:
     * determine if this FMap<K, V> equals the FMap<K, V>
     * other
     * @param other - another FMap<K, V> to compare for
     * equality
     * @return boolean - returns true if the this FMap<K, V>
     * equals other
     */
    boolean reallyEquals(FMap<K, V> other) {
        return true;
    }
    
    /**
     * remove:
     * removes the first key of type K, k, from this FMap<K, V>
     * @param k - the key of type K to remove from this FMap<K, V>
     * @return FMap<K, V> - a new FMap<K, V> with the same mappings
     * as this one with the first instance of the key k removed. If 
     * this FMap<K, V> is empty, this method returns this FMap<K, V>.
     */
    FMap<K, V> remove(K k) {
        return this;
    }
    
    /**
     * removeRepeatedKeys:
     * if a key of type K has a more current mapping, this method
     * will remove all previous mappings to this key until this
     * FMap<K, V> has no duplicate bindings to the same key.
     * @return FMap<K, V> - this FMap<K, V> with duplicate bindings
     * removed and only the most current bindings preserved
     */
    FMap<K, V> removeRepeatedKeys() {
        return this;
    }
    
    /**
     * hashCode:
     * return this FMap<K, V>'s hash code
     * @return int - this FMap<K, V>'s hash code
     */
    public int hashCode() {
        return 0;
    }
    
    /**
     * makeList: 
     * creates an ArrayList<K> of this FMap<K, V>
     * @param ar - an accumulation of the keys thus far,
     * it should be initialized as an empty ArrayList<K>.
     * @return ArrayList<K> - this FMap<K, V> converted
     * into an ArrayList<K> of keys
     */
    ArrayList<K> makeList(ArrayList<K> ar) {
        return ar;
    }
    

    /**
     * sumKeyValueHash:
     * sums the value of the hashcodes of every key and value in
     * this FMap<K, V>, manipulating the values through some constants
     * @return int - the sum of all the hashcodes of values and keys
     * in this FMap<K, V>
     */
    int sumKeyValueHash() {
        return 0;
    }
    
}







/**
 * This class represents a non-empty FMap<K, V>
 * whose values represent finite functions 
 * from keys of type K to values of type V.
 * m0 - represents the FMap<K, V> containing all
 * previous bindings in this FMap<K, V> except for 
 * k0's binding to v0.
 * k0 - the most recent key bound to a value
 * v0 - the value bound to k0
 */
class Include<K, V> extends AList<K, V> {
    FMap<K, V> m0;
    K k0;
    V v0;
    // Include constructor
    Include(FMap<K, V> m0, K k0, V v0) {
        this.m0 = m0;
        this.k0 = k0;
        this.v0 = v0;
    }
    
    /**
     * isEmpty:
     * tells us if this FMap<K, V> is empty
     * @return boolean - returns true if
     * this FMap<K, V> is empty
     */
    public boolean isEmpty() {
        return false;
    }
    
    /** 
     * size:
     * determines the size of this FMap<K, V>,
     * not counting repeated mappings of the
     * same key of type K
     * @return int - the number of distinct
     * keys in this FMap<K, V>
     */
    public int size() {
        if (this.m0.containsKey(this.k0))
            return this.m0.size();
        else return 1 + this.m0.size();
    }
    
    /** containsKey:
     * determines if this FMap<K, V> contains the 
     * key k
     * @param k - the key of type K to be checked
     * is within this FMap<K, V>
     * @return boolean - returns true if k is
     * contained within this FMap<K, V>
     */
    public boolean containsKey(K k) {
        if (k.equals(k0))
            return true;
        else return this.m0.containsKey(k);
    }
    
    /**
     * get:
     * gives the value of type V associated to
     * the given key of type K, k, within this
     * FMap<K, V>
     * @param k - the key of type K whose value
     * is desired
     * @return V - the value associated to the
     * key k
     */
    public V get(K k) {
        if (k.equals(this.k0))
            return this.v0;
        else return this.m0.get(k);
    }
    
    
    /**
     * reallyEquals:
     * determine if this FMap<K, V> equals the FMap<K, V>
     * other
     * @param other - another FMap<K, V> to compare for
     * equality
     * @return boolean - returns true if the this FMap<K, V>
     * equals other
     */
    boolean reallyEquals(FMap<K, V> other) {
        return other.containsKey(this.k0) &&
                this.get(this.k0).equals(other.get(this.k0))
                && this.m0.reallyEquals(other);
    }
    
    /**
     * remove:
     * removes the first key of type K, k, from this FMap<K, V>
     * @param k - the key of type K to remove from this FMap<K, V>
     * @return FMap<K, V> - a new FMap<K, V> with the same mappings
     * as this one with the first instance of the key k removed. If 
     * this FMap<K, V> is empty, this method returns this FMap<K, V>.
     */
    FMap<K, V> remove(K k) {
        if (k.equals(this.k0))
            return this.m0;
        else return this.m0.remove(k).include(this.k0, this.v0);
    }
    
    /**
     * removeRepeatedKeys:
     * if a key of type K has a more current mapping, this method
     * will remove all previous mappings to this key until this
     * FMap<K, V> has no duplicate bindings to the same key.
     * @return FMap<K, V> - this FMap<K, V> with duplicate bindings
     * removed and only the most current bindings preserved
     */
    FMap<K, V> removeRepeatedKeys() {
        if (this.m0.containsKey(this.k0))
            return this.m0.remove(this.k0).include(this.k0,
                    this.v0).removeRepeatedKeys();
        else return this.m0.removeRepeatedKeys().include(this.k0,
                                                         this.v0);
    }
    
    /**
     * hashCode:
     * return this FMap<K, V>'s hash code
     * @return int - this FMap<K, V>'s hash code
     */
    public int hashCode() {
        return this.size() + this.removeRepeatedKeys().sumKeyValueHash();
    }
    
    /**
     * makeList: 
     * creates an ArrayList<K> of this FMap<K, V>
     * @param ar - an accumulation of the keys thus far,
     * it should be initialized as an empty ArrayList<K>.
     * @return ArrayList<K> - this FMap<K, V> converted
     * into an ArrayList<K> of keys
     */
    ArrayList<K> makeList(ArrayList<K> ar) {
        ar.add(k0);
        return this.m0.makeList(ar);
    }
    

    /**
     * sumKeyValueHash:
     * sums the value of the hashcodes of every key and value in
     * this FMap<K, V>, manipulating the values through some constants
     * @return int - the sum of all the hashcodes of values and keys
     * in this FMap<K, V>
     */
    int sumKeyValueHash() {
        return (2 * this.k0.hashCode()) +
                (3 * this.v0.hashCode()) + 7
                + this.m0.sumKeyValueHash();
    }
    
}







/**
 * This class represents an iterator of the type K
 * it - the ArrayList<K> to create an iterator out of,
 * and iterate down
 * @param <K> - different types to be associated with
 * this iterator
 */
class KIterator<K> implements Iterator<K> {
    ArrayList<K> it;
    
    // KIterator constructor
    KIterator(ArrayList<K> it) {
        this.it = it;
    }
    
    /**
     * hasNext:
     * determines if this iterator has a next element
     * @return boolean - returns true if there is a
     * next element
     */
    public boolean hasNext() {
        return !it.isEmpty();
    }

    /**
     * next:
     * returns the next element in the iteration
     * @return K - the next element in the iteration, or
     * an exception if there are no more elements in this
     * iteration
     */
    public K next() {
        if (this.hasNext())
            return it.remove(0);
        else throw new NoSuchElementException();
    }

    /**
     * remove:
     *  Removes from the underlying collection the last element
     *  returned by the iterator (optional operation).
     *  @return void - returns the iterator with the last element
     *  removed
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
}







/**
 * This class represents a binary search tree implementation of
 * the FMap<K, V> from the keys K to the values V
 * @param <K> - key type
 * @param <V> - value type
 */
abstract class BST<K, V> extends FMap<K, V> {
    
    /**
     * include:
     * returns a new non-empty FMap<K, V>
     * @param k0 - a key of type K to add this FMap<K, V>
     * @param v0 - a value of type V to be mapped to k0
     * @return - FMap<K, V> - a new FMap<K, V> with the 
     * new mapping of the key k0 to the value v0
     */
    public FMap<K, V> include(K k0, V v0) {
        return this.insert(k0, v0).makeBlack();
    }
    
    /**
     * makeBlack:
     * returns a new BST<K, V> with its color changed to black
     * (in both the empty and non-empty tree cases)
     * @return BST<K, V> - a new BST<K, V> with its color changed 
     * to black (in both the empty and non-empty tree cases)
     */
    abstract BST<K, V> makeBlack();
    
    /**
     * balance:
     * returns a new BST<K, V> that is balanced according to the
     * invariants of red-black trees
     * @return BST<K, V> - new BST<K, V> that is balanced according 
     * to the invariants of red-black trees i.e. no red child has
     * a red parent, and every path from the root to an empty node 
     * contains the same number of black nodes.
     */
    abstract BST<K, V> balance();
    
    /**
     * getLeft:
     * returns the left subtree of this BST<K, V>, if it has one
     * @return BST<K, V> - the left subtree of this BST<K, V> if
     * it exists
     */
    abstract BST<K, V> getLeft();
    
    /**
     * getRight:
     * returns the right subtree of this BST<K, V>, if it has one
     * @return BST<K, V> - the right subtree of this BST<K, V> if
     * it exists
     */
    abstract BST<K, V> getRight();
    
    /**
     * getColor:
     * returns the color of this BST<K, V>
     * @return String - the color of this BST<K, V>
     */
    abstract String getColor();
    
    /**
     * getKey:
     * returns the key associated to the root node
     * of this BST<K, V> if non-empty
     * @return K - the key associated to the root
     * node of this BST<K, V> if non-empty
     */
    abstract K getKey();
    
    /**
     * getValue:
     * returns the value associated to the root node
     * of this BST<K, V> if non-empty
     * @return V - the value associated to the root
     * node of this BST<K, V> if non-empty
     */
    abstract V getValue();
    
    /**
     * removeRepeatedKeys:
     * if a key of type K has a more current mapping, this method
     * will remove all previous mappings to this key until this
     * FMap<K, V> has no duplicate bindings to the same key.
     * @return FMap<K, V> - this FMap<K, V> with duplicate bindings
     * removed and only the most current bindings preserved
     */
    FMap<K, V> removeRepeatedKeys() {
        return this;
    }
    
    /**
     * remove:
     * removes the first key of type K, k, from this FMap<K, V>
     * @param k - the key of type K to remove from this FMap<K, V>
     * @return FMap<K, V> - a new FMap<K, V> with the same mappings
     * as this one with the first instance of the key k removed. If 
     * this FMap<K, V> is empty, this method returns this FMap<K, V>.
     */
    FMap<K, V> remove(K k) {
        return this;
    }
    
    /**
     * insert:
     * places the binding of the value v to the key k
     * in this BST<K, V>
     * if the key already exists, its previous value is
     * overwritten with v
     * @param k - a key of type K to bind
     * @param v - a value of type V to bind
     * @return BST<K, V> - a new FMap<K, V> that has the updated key
     * binding of k to v
     */
    abstract BST<K, V> insert(K k, V v);
    
    /**
     * iterator:
     * creates a likely unordered iterator of type K from this
     * FMap<K, V> that has no duplicate keys listed
     * @return Iterator<K> - a likely unordered iterator of 
     * the type K
     */
    public Iterator<K> iterator() {
        ArrayList<K> k = this.makeList(new ArrayList<K> ());
        return new KIterator<K>(k);
    }
    
    /**
     * iterator:
     * creates a new ordered iterator of type K from this 
     * FMap<K, V> that has no duplicate keys listed
     * @param comp - a Comparator<K> to create an ordering to
     * sort by
     * @return Iterator<K> - an ordered iterator of the type K
     */
    public Iterator<K> iterator(Comparator<? super K> comp) {
        return new KIterator<K>(this.sort(
                 this.makeList(new ArrayList<K>()),
                 comp));
    }
      
    /**
     * accept:
     * takes a visitor and modifies every value V bound
     * to a key K in this FMap<K, V>
     * @param visitor - the visitor from that maps values V
     * bound to keys K to new values of same type, however
     * modified by an equation
     * @return FMap<K, V> - an FMap<K, V> with the same keys
     * as this FMap<K, V> with the associated values modified
     * by the equation define by the visitor's visit method
     */
    public FMap<K, V> accept(Visitor<K, V> visitor) {
        return this.acceptBST(visitor);
    }
    
    /**
     * acceptBST:
     * takes a visitor and modifies every value V bound
     * to a key K in this BST<K, V>
     * @param visitor - the visitor from that maps values V
     * bound to keys K to new values of same type, however
     * modified by an equation
     * @return BST<K, V> - a BST<K, V> with the same keys
     * as this BST<K, V> with the associated values modified
     * by the equation define by the visitor's visit method
     */
    abstract BST<K, V> acceptBST(Visitor<K, V> visitor);
    
}







/**
 * This class represents an empty binary search tree implementation of
 * the FMap<K, V> from the keys K to the values V
 * @param <K> - key type
 * @param <V> - value type
 * @param c - a comparator of type K that defines this BST's ordering
 */
class EmptyBST<K, V> extends BST<K, V> {
    Comparator<? super K> c;
    String color;
    // Empty BST Constructor
    EmptyBST(Comparator<? super K> c) {
        this.c = c;
        this.color = "black";
    }
    
    /**
     * makeBlack:
     * returns a new BST<K, V> with its color changed to black
     * (in both the empty and non-empty tree cases)
     * @return BST<K, V> - a new BST<K, V> with its color changed 
     * to black (in both the empty and non-empty tree cases)
     */
    BST<K, V> makeBlack() {
        return this;
    }
    
    /**
     * balance:
     * returns a new BST<K, V> that is balanced according to the
     * invariants of red-black trees
     * @return BST<K, V> - new BST<K, V> that is balanced according 
     * to the invariants of red-black trees i.e. no red child has
     * a red parent, and every path from the root to an empty node 
     * contains the same number of black nodes.
     */
    BST<K, V> balance() {
        return this;
    }
    
    /**
     * getLeft:
     * returns the left subtree of this BST<K, V>, if it has one
     * @return BST<K, V> - the left subtree of this BST<K, V> if
     * it exists
     */
    BST<K, V> getLeft() {
        throw new RuntimeException("No left for empty");
    }
    
    /**
     * getRight:
     * returns the right subtree of this BST<K, V>, if it has one
     * @return BST<K, V> - the right subtree of this BST<K, V> if
     * it exists
     */
    BST<K, V> getRight() {
        throw new RuntimeException("No right for empty");
    }
    
    /**
     * getColor:
     * returns the color of this BST<K, V>
     * @return String - the color of this BST<K, V>
     */
    String getColor() {
        return this.color;
    }
    
    /**
     * getKey:
     * returns the key associated to the root node
     * of this BST<K, V> if non-empty
     * @return K - the key associated to the root
     * node of this BST<K, V> if non-empty
     */
    K getKey() {
        throw new RuntimeException("No key in empty");
    }
    
    /**
     * getValue:
     * returns the value associated to the root node
     * of this BST<K, V> if non-empty
     * @return V - the value associated to the root
     * node of this BST<K, V> if non-empty
     */
    V getValue() {
        throw new RuntimeException("No value in empty");
    }
    
    /**
     * insert:
     * places the binding of the value v to the key k
     * in this BST<K, V>
     * if the key already exists, its previous value is
     * overwritten with v
     * @param k - a key of type K to bind
     * @param v - a value of type V to bind
     * @return - a new FMap<K, V> that has the updated key
     * binding of k to v
     */
    BST<K, V> insert(K k, V v) {
        return new NodeBST<K, V>(k, v, this, this, this.c, "red");
    }
    
    /**
     * isEmpty:
     * tells us if this FMap<K, V> is empty
     * @return boolean - returns true if
     * this FMap<K, V> is empty
     */
    public boolean isEmpty() {
        return true;
    }
    
    /** 
     * size:
     * determines the size of this FMap<K, V>,
     * not counting repeated mappings of the
     * same key of type K
     * @return int - the number of distinct
     * keys in this FMap<K, V>
     */
    public int size() {
        return 0;
    }
    
    /** containsKey:
     * determines if this FMap<K, V> contains the 
     * key k
     * @param k - the key of type K to be checked
     * is within this FMap<K, V>
     * @return boolean - returns true if k is
     * contained within this FMap<K, V>
     */
    public boolean containsKey(K k) {
        return false;
    }
    
    /**
     * get:
     * gives the value of type V associated to
     * the given key of type K, k, within this
     * FMap<K, V>
     * @param k - the key of type K whose value
     * is desired
     * @return V - the value associated to the
     * key k
     */
    public V get(K k) {
        throw new RuntimeException("Key not in mapping!");
    }
    
    /**
     * reallyEquals:
     * determine if this FMap<K, V> equals the FMap<K, V>
     * other
     * @param other - another FMap<K, V> to compare for
     * equality
     * @return boolean - returns true if the this FMap<K, V>
     * equals other
     */
    boolean reallyEquals(FMap<K, V> other) {
        return true;
    }
    
    /**
     * hashCode:
     * return this FMap<K, V>'s hash code
     * @return int - this FMap<K, V>'s hash code
     */
    public int hashCode() {
        return 0;
    }
    
    /**
     * makeList: 
     * creates an ArrayList<K> of this FMap<K, V>
     * @param ar - an accumulation of the keys thus far,
     * it should be initialized as an empty ArrayList<K>.
     * @return ArrayList<K> - this FMap<K, V> converted
     * into an ArrayList<K> of keys
     */
    ArrayList<K> makeList(ArrayList<K> ar) {
        return ar;
    }
    
    /**
     * acceptBST:
     * takes a visitor and modifies every value V bound
     * to a key K in this BST<K, V>
     * @param visitor - the visitor from that maps values V
     * bound to keys K to new values of same type, however
     * modified by an equation
     * @return BST<K, V> - a BST<K, V> with the same keys
     * as this BST<K, V> with the associated values modified
     * by the equation define by the visitor's visit method
     */
    BST<K, V> acceptBST(Visitor<K, V> visitor) {
        return this;
    }
    
    /**
     * sumKeyValueHash:
     * sums the value of the hashcodes of every key and value in
     * this FMap<K, V>, manipulating the values through some constants
     * @return int - the sum of all the hashcodes of values and keys
     * in this FMap<K, V>
     */
    int sumKeyValueHash() {
        return 0;
    }
    
}







/**
 * This class represents a non-empty binary search tree implementation
 *  of the FMap<K, V> from the keys K to the values V
 * @param <K> - key type
 * @param <V> - value type
 * @param key - this node's key of type K
 * @param value - this node's key's value of type V
 * @param left - the FMap<K, V> containing key bindings
 * before this one according to c
 * @param right - the FMap<K, V> containing key bindings
 * after this one according to c
 * @param c - the Comparator<? super K> that determines
 * the ordering of this BST<K, V>
 * @param size - the number of nodes in this BST<K, V>
 */
class NodeBST<K, V> extends BST<K, V> {
    K key;
    V value;
    BST<K, V> left;
    BST<K, V> right;
    Comparator<? super K> c;
    int size;
    String color;
    // The NodeBST constructor
    NodeBST(K key, V value, BST<K, V> left, BST<K, V> right, 
            Comparator<? super K> c, String color) {
        this.key = key;
        this.value = value;
        this.left = left;
        this.right = right;
        this.c = c;
        this.size = 1 + this.right.size() + this.left.size();
        this.color = color;
    }
    
    /**
     * makeBlack:
     * returns a new BST<K, V> with its color changed to black
     * (in both the empty and non-empty tree cases)
     * @return BST<K, V> - a new BST<K, V> with its color changed 
     * to black (in both the empty and non-empty tree cases)
     */
    BST<K, V> makeBlack() {
        return new NodeBST<K, V>(
                this.key,
                this.value,
                this.left, 
                this.right,
                this.c,
                "black");
    }  

    /**
     * balance:
     * returns a new BST<K, V> that is balanced according to the
     * invariants of red-black trees
     * @return BST<K, V> - new BST<K, V> that is balanced according 
     * to the invariants of red-black trees i.e. no red child has
     * a red parent, and every path from the root to an empty node 
     * contains the same number of black nodes.
     */
    BST<K, V> balance() {
        String b = "black";
        String r = "red";
        // left left double red case
        boolean caseLL = this.color.equals(b) &&
                        !this.left.isEmpty() &&
                        this.left.getColor().equals(r) &&
                        !this.left.getLeft().isEmpty() &&
                        this.left.getLeft().getColor().equals(r);
        // left right double red case
        boolean caseLR = this.color.equals(b) &&
                        !this.left.isEmpty() &&
                        this.left.getColor().equals(r) &&
                        !this.left.getRight().isEmpty() &&
                        this.left.getRight().getColor().equals(r);
        // right left double red case
        boolean caseRL = this.color.equals(b) &&
                        !this.right.isEmpty() &&
                        this.right.getColor().equals(r) &&
                        !this.right.getLeft().isEmpty() &&
                        this.right.getLeft().getColor().equals(r);
        // right right double red case
        boolean caseRR = this.color.equals(b) &&
                        !this.right.isEmpty() &&
                        this.right.getColor().equals(r) &&
                        !this.right.getRight().isEmpty() &&
                        this.right.getRight().getColor().equals(r);
        
        if (caseLL)
            return new NodeBST<K, V>(
                    this.left.getKey(),
                    this.left.getValue(),
                    this.left.getLeft().makeBlack(),
                    new NodeBST<K, V>(this.key,
                                      this.value,
                                      this.left.getRight(),
                                      this.right,
                                      this.c,
                                      b),
                    this.c, 
                    r);
        
        if (caseLR)
            return new NodeBST<K, V>(
                    this.left.getRight().getKey(),
                    this.left.getRight().getValue(),
                    new NodeBST<K, V>(
                            this.left.getKey(),
                            this.left.getValue(),
                            this.left.getLeft(),
                            this.left.getRight().getLeft(),
                            this.c,
                            b),
                    new NodeBST<K, V>(
                            this.key,
                            this.value,
                            this.left.getRight().getRight(),
                            this.right,
                            this.c,
                            b),
                    this.c,
                    r);
        
        if (caseRL)
            return new NodeBST<K, V>(
                    this.right.getLeft().getKey(),
                    this.right.getLeft().getValue(),
                    new NodeBST<K, V>(
                            this.key,
                            this.value,
                            this.left,
                            this.right.getLeft().getLeft(),
                            this.c,
                            b),
                    new NodeBST<K, V> (
                            this.right.getKey(),
                            this.right.getValue(),
                            this.right.getLeft().getRight(),
                            this.right.getRight(),
                            this.c,
                            b),
                    this.c,
                    r);
        
        if (caseRR)
            return new NodeBST<K, V>(
                    this.right.getKey(),
                    this.right.getValue(),
                    new NodeBST<K, V>(
                            this.key,
                            this.value,
                            this.left,
                            this.right.getLeft(),
                            this.c,
                            b),
                    new NodeBST<K, V>(
                            this.right.getRight().getKey(),
                            this.right.getRight().getValue(),
                            this.right.getRight().getLeft(),
                            this.right.getRight().getRight(),
                            this.c,
                            b),
                    this.c,
                    r);
        
        else return this;
    }
    
    /**
     * getLeft:
     * returns the left subtree of this BST<K, V>, if it has one
     * @return BST<K, V> - the left subtree of this BST<K, V> if
     * it exists
     */
    BST<K, V> getLeft() {
        return this.left;
    }
    
    /**
     * getRight:
     * returns the right subtree of this BST<K, V>, if it has one
     * @return BST<K, V> - the right subtree of this BST<K, V> if
     * it exists
     */
    BST<K, V> getRight() {
        return this.right;
    }
    
    /**
     * getColor:
     * returns the color of this BST<K, V>
     * @return String - the color of this BST<K, V>
     */
    String getColor() {
        return this.color;
    }
    
    /**
     * getKey:
     * returns the key associated to the root node
     * of this BST<K, V> if non-empty
     * @return K - the key associated to the root
     * node of this BST<K, V> if non-empty
     */
    K getKey() {
        return this.key;
    }
    
    /**
     * getValue:
     * returns the value associated to the root node
     * of this BST<K, V> if non-empty
     * @return V - the value associated to the root
     * node of this BST<K, V> if non-empty
     */
    V getValue() {
        return this.value;
    }
    
    /**
     * insert:
     * places the binding of the value v to the key k
     * in this BST<K, V>
     * if the key already exists, its previous value is
     * overwritten with v
     * @param k - a key of type K to bind
     * @param v - a value of type V to bind
     * @return - a new FMap<K, V> that has the updated key
     * binding of k to v
     */
    BST<K, V> insert(K k, V v) {
        if (this.key.equals(k))
            return new NodeBST<K, V>(
                    k,
                    v, 
                    this.left,
                    this.right,
                    this.c,
                    this.color);
        if (this.c.compare(k, this.key) < 0)
            return new NodeBST<K, V>(
                    this.key,
                    this.value,
                    this.left.insert(k, v), 
                    this.right,
                    this.c, 
                    this.color).balance();
        else return new NodeBST<K, V>(
                this.key, 
                this.value,
                this.left,
                this.right.insert(k, v),
                this.c,
                this.color).balance(); 
    }
    
    /**
     * isEmpty:
     * tells us if this FMap<K, V> is empty
     * @return boolean - returns true if
     * this FMap<K, V> is empty
     */
    public boolean isEmpty() {
        return false;
    }

    /** 
     * size:
     * determines the size of this FMap<K, V>,
     * not counting repeated mappings of the
     * same key of type K
     * @return int - the number of distinct
     * keys in this FMap<K, V>
     */
    public int size() {
        return this.size;
    }

    /** containsKey:
     * determines if this FMap<K, V> contains the 
     * key k
     * @param k - the key of type K to be checked
     * is within this FMap<K, V>
     * @return boolean - returns true if k is
     * contained within this FMap<K, V>
     */
    public boolean containsKey(K k) {
        if (this.key.equals(k))
            return true;
        if (this.c.compare(k, this.key) < 0)
            return this.left.containsKey(k);
        else return this.right.containsKey(k);
    }

    /**
     * get:
     * gives the value of type V associated to
     * the given key of type K, k, within this
     * FMap<K, V>
     * @param k - the key of type K whose value
     * is desired
     * @return V - the value associated to the
     * key k
     */
    public V get(K k) {
        if (this.key.equals(k))
            return this.value;
        if (this.c.compare(k, this.key) < 0)
            return this.left.get(k);
        else return this.right.get(k);
    }

    /**
     * reallyEquals:
     * determine if this FMap<K, V> equals the FMap<K, V>
     * other
     * @param other - another FMap<K, V> to compare for
     * equality
     * @return boolean - returns true if the this FMap<K, V>
     * equals other
     */
    boolean reallyEquals(FMap<K, V> other) {
        return other.containsKey(this.key) &&
                other.get(this.key).equals(this.get(this.key))
                && this.right.reallyEquals(other)
                && this.left.reallyEquals(other);
    }

    /**
     * hashCode:
     * return this FMap<K, V>'s hash code
     * @return int - this FMap<K, V>'s hash code
     */
    public int hashCode() {
        return this.size() + this.sumKeyValueHash();
    }

    /**
     * makeList: 
     * creates an ArrayList<K> of this FMap<K, V>
     * @param ar - an accumulation of the keys thus far,
     * it should be initialized as an empty ArrayList<K>.
     * @return ArrayList<K> - this FMap<K, V> converted
     * into an ArrayList<K> of keys
     */
    ArrayList<K> makeList(ArrayList<K> ar) {
        ar.add(this.key);
        return this.left.makeList(this.right.makeList(ar));
    }
    
    /**
     * acceptBST:
     * takes a visitor and modifies every value V bound
     * to a key K in this BST<K, V>
     * @param visitor - the visitor from that maps values V
     * bound to keys K to new values of same type, however
     * modified by an equation
     * @return BST<K, V> - a BST<K, V> with the same keys
     * as this BST<K, V> with the associated values modified
     * by the equation define by the visitor's visit method
     */
    BST<K, V> acceptBST(Visitor<K, V> visitor) {
        BST<K, V> l = this.left.acceptBST(visitor);
        V v = visitor.visit(this.key, this.value);
        BST<K, V> r = this.right.acceptBST(visitor);
        return new NodeBST<K, V>(this.key, v, l, r, this.c, this.color);
    }
    
    /**
     * sumKeyValueHash:
     * sums the value of the hashcodes of every key and value in
     * this FMap<K, V>, manipulating the values through some constants
     * @return int - the sum of all the hashcodes of values and keys
     * in this FMap<K, V>
     */
    int sumKeyValueHash() {
        return (2 * this.key.hashCode()) +
                (3 * this.value.hashCode()) + 7
                + this.left.sumKeyValueHash()
                + this.right.sumKeyValueHash();
    }
    
}
