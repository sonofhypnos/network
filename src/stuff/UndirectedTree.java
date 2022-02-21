package stuff;

import java.util.*;

/**
 * The interface Undirected tree.
 *
 * @param <E> the type parameter
 */
public interface UndirectedTree <E> {
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
    UndirectedTree<E> copy();

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
     * Add boolean.
     *
     * @param tree the tree
     * @return the boolean
     */
    boolean add(UndirectedTree<E> tree);


    /**
     * Connect boolean.
     *
     * @param a the a
     * @param b the b
     * @return the boolean
     */
    boolean connect(final E a, final E b);

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
     *
     * @param a the a
     * @param b the b
     */
    void add(E a, E b);

    /**
     * Is tree boolean.
     *
     * @return the boolean
     */
    boolean isTree();

    boolean equals(UndirectedTree<E> secondGraph);
}
