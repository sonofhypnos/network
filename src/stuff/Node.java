package stuff;

import java.util.*;

/**
 * The type Tree node.
 *
 * @param <E> the type parameter
 */
public class Node<E extends Comparable<E>> implements Comparable<Node<E>> {
    /*
     * @project FinalAssignmentNoBloat
     * @author Tassilo Neubauer
     */

    /**
     * The Value.
     */
    protected final E value;
    /**
     * The Children.
     */
    private final List<Node<E>> connectedNodes;


    /**
     * Instantiates a new Tree node.
     *
     * @param value the value
     */
    public Node(E value) {
        this.value = value;
        this.connectedNodes = new ArrayList<>();
    }

//    @Override
//    public boolean equals(final Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Node<?> node = (Node<?>) o;
//        if (parent.equals(node.parent) || connectedNodes.size() != node.getChildSize()) return false;
//        if (connectedNodes.size() == 0) return true;
//        // TODO: 17.02.22 Equals method shsshoudl probably change here
//        for (Node<E> child: connectedNodes) {
//            if (!node.getConnectedNodes().contains(child)) return false;
//        }
//        return true;
//    }

    /**
     * Is directed boolean.
     *
     * @return the boolean
     */
    protected Boolean isDirected(){
        // TODO: 18.02.22 kann man einfach schauen, ob list eintrag doppelt hat?
        return this.list().stream().distinct().count() != this.list().size();
    }

    /**
     * Get child size int.
     *
     * @return the int
     */
    public int getChildSize(){
        return connectedNodes.size();
    }


    /**
     * Get children array list.
     *
     * @return the array list
     */
    public List<Node<E>> getConnectedNodes(){
        // TODO: 17.02.22 copy?
        return Collections.unmodifiableList(connectedNodes);
    }

    /**
     * Add child.
     *
     * @param child the child
     */
    public void addUndirectedEdge(Node<E> child) {
        // TODO: 18.02.22 remove if not used?
        this.connectedNodes.add(child);
        child.addDirectedEdge(this);
    }

    /**
     * Add directed edge.
     *
     * @param child the child
     */
    public void addDirectedEdge(Node<E> child){
        this.connectedNodes.add(child);
    }


    /**
     * Get level int.
     *
     * @return the int
     */
    public int getLevel(){
        // TODO: 18.02.22 DFS?
        return 0;
    }



    /**
     * returns all elements that are downstream of this Node in the Tree
     *
     * @return the list
     */
    public List<E> list(){
        // TODO: 17.02.22 make sure does not contain null
        // TODO: 16.02.22 might be (worth and) faster without copy
        // TODO Treeset might not be appropriate here
        List<E> elements = new ArrayList<>();
        elements.add(value);
        for (Node<E> connectedNode: connectedNodes) {
            elements.addAll(connectedNode.list()); //todo is this copying?
        }
        return Collections.unmodifiableList(elements);
    }

    /**
     * Get connected list.
     *
     * @return the list
     */
    public List<Node<E>> getConnected(){
        // TODO: 18.02.22 refactor with above?
        List<Node<E>> elements = new ArrayList<>();
        elements.add(this);
        for (Node<E> connectedNode: connectedNodes) {
            elements.addAll(connectedNode.getConnected());
        }
        return Collections.unmodifiableList(elements);
    }


    /**
     * Gets value.
     *
     * @return the value
     */
    public E getValue() {
        return value;
    }

    public String toString() {
        if (getConnectedNodes().size() == 0){
            return this.value.toString();
        }
        List<String> ipStrings = new ArrayList<>();
        ipStrings.add(this.value.toString());

        List<Node<E>> children = getConnectedNodes();
        children.sort(Comparator.comparing(Node::getValue));
        for (Node<E> child: getConnectedNodes() ) {
            ipStrings.add(child.toString());
        }
        return "(" + String.join(" ", ipStrings) + ")"; // TODO: 18.02.22 make this seperate thingy
    }

    @Override
    public int compareTo(final Node<E> o) {
        return this.getValue().compareTo(o.getValue());
    }

    public Node<E> copy(){
        Node<E> newNode = new Node<E>(this.getValue());
        List<E>
    }
}
