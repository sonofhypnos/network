package stuff;

import java.util.*;

public interface UndirectedTree <E> {

    /**
     * Get levels list. Implements breadth search. Inspired by findPath solution I used one year ago for this course
     *
     * @param root the root
     * @return the list
     */

    List<E> list();
    boolean add(final UndirectedTree tree);


    boolean connect(final E a, final E b);
    boolean disconnect(final E a, final E b);
    boolean contains(final E element);
    int getHeight(final E root);
    List<List<E>> getLevels(final E root);
    List<E> getRoute(final E start, final E end);

    void add(E first, E second);

    boolean isTree();
}
