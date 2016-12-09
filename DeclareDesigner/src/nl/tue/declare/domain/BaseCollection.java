package nl.tue.declare.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to store objects that extends class Base, i.e., objects
 * with Integer identification. Each element of the collection has to have a
 * unique ID. On the one hand, this class extends ArrayList, which enables
 * maintaining the order in which elements were added to the collection. On the
 * other hand, this class stores all its elements in a HashMap, which makes sure
 * all elements have unique IDs and enables quick retrieval of objects based on
 * their IDs.
 */

public class BaseCollection<E extends Base> extends ArrayList<E> {

	private static final long serialVersionUID = 8864827256065031046L;
	/**
	 * A hash map that stores all elements of the collection based on their
	 * Integer identification.
	 */
	private HashMap<Integer, E> map;

	/**
	 * This constructor initiates the ArrayList and the HashMap map.
	 */
	public BaseCollection() {
		super();
		map = new HashMap<Integer, E>();
	}

	/**
	 * Adds the new element to the collection. The element with one ID can be
	 * added only once.
	 * 
	 * @param e is the element to be added
	 */
	
	public boolean add(E e) {
		if (map.put(new Integer(e.getId()), e) == null) {
			return super.add(e);
		}
		return false;
	}
	
    /**
     * Removes one element from the collection.     
     * @param e is the element to be removed
     */
	public void remove(E e) {
		super.remove(e);
		map.remove(new Integer(e.getId()));
	}
	
    /**
     * Returns the greatest ID of all elements in the collection.
     * 
     * @return the greatest ID found in the collection.
     */
	private int getMaxId() {
		int id = 0;
		for (Integer i : map.keySet()) {
			if (id < i) {
				id = i;
			}
		}
		return id;
	}

	/**
	 * Returns the ID that should be assigned to the next element in order to
	 * be successfully added to the collection. 
	 * 
	 * @return getMaxId() + 1
	 */
	public int nextId() {
		return getMaxId() + 1;
	}
    /**
     * Returns the element with given ID.
     * 
     * @param id of the element to be returned.
     * 
     * @return The element with ID equal to parameter id.
     */
	public E getItemWithId(int id) {
		return map.get(new Integer(id));
	}
}
