package stuff;

import java.util.*;

/**
 * The interface Undirected tree.
 *
 * @param <E> the type parameter
 */
public interface TreeTopology<E> {
    // TODO: 20.02.22 rename undirected Trees?
    // TODO: 20.02.22 rename Treetopology?
    // TODO: 20.02.22 maybe mention the fact this isn't a real tree?

    /**
     * Gets edges.
     *
     * @return the edges
     */
    List<List<E>> getEdges();

    /**
     * Copy undirected tree.
     *
     * @return the undirected tree
     */
    TreeTopology<E> copy();

    /**
     * Gets levels.
     *
     * @param root the root
     * @return the levels
     */
    List<List<E>> getLevels(E root);

    /**
     * Gets height.
     *
     * @param root the root
     * @return the height
     */
    int getHeight(E root);

    /**
     * Get levels list.
     *
     * @param root the root
     * @return the list
     */
    List<E> list();

    /**
     * Disconnect boolean.
     *
     * @param a the a
     * @param b the b
     * @return the boolean
     */
    boolean disconnect(E a, E b);

    /**
     * Remove boolean.
     *
     * @param a the a
     * @param b the b
     * @return the boolean
     */
    boolean remove(final E a, final E b);

    /**
     * Contains boolean.
     *
     * @param element the element
     * @return the boolean
     */
    boolean contains(final E element);

    /**
     * Gets route.
     *
     * @param start the start
     * @param end   the end
     * @return the route
     */
    List<E> getRoute(final E start, final E end);

    /**
     * Get list.
     *
     * @param element the element
     * @return the list
     */
    List<E> get(final E element);

    /**
     * Add.
     *  @param a the a
     * @param b the b
     * @return
     */
    boolean add(E a, E b);

    /**
     * Is tree boolean.
     *
     * @return the boolean
     */
    boolean isTree();

    /**
     * Equals boolean.
     *
     * @param tree the second tree
     * @return the boolean
     */
    boolean equals(TreeTopology<E> tree);
}
