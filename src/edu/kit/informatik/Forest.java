package edu.kit.informatik;

import edu.kit.informatik.resources.ForestException;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The Forest Class. This class contains all functions for Forests that aren't specific to the fact that Network.
 * contains IPs.
 *
 * @param <E> the type parameter
 * @author upkim
 * @version 1.0 2022-03-08 09:58
 */
public class Forest<E> {
    /**
     * Minimum nodes a forest has to have.
     */
    static final int MIN_NODES = 2;
    private static final String LOOP_FOREST = "Adding this edge would add a loop to the forest";

    private final HashMap<E, List<E>> edges;

    /**
     * Instantiates a new Forest.
     *
     * @param nodes the nodes
     */
    public Forest(HashMap<E, List<E>> nodes) {
        this.edges = copyEdges(nodes);
    }

    /**
     * Instantiates a new Forest object without any Nodes.
     */
    public Forest() {
        this.edges = new HashMap<>();
    }

    private HashMap<E, List<E>> copyEdges(final HashMap<E, List<E>> nodes) {
        HashMap<E, List<E>> newEdges = new HashMap<>();
        for (E firstNode : nodes.keySet()) {
            List<E> nextNodes = new ArrayList<>(nodes.get(firstNode));
            newEdges.put(firstNode, nextNodes);
        }
        return newEdges;
    }

    private HashMap<E, List<E>> getMap() {
        return copyEdges(edges);
    }

    /**
     * Returns List of edges in the Forest.
     *
     * @return the edges
     */
    public List<List<E>> getEdges() {
        List<List<E>> edges = new ArrayList<>();
        HashMap<E, List<E>> newMap = getMap();
        for (E node : newMap.keySet()) {
            edges.addAll(newMap.get(node).stream().map((E connectedNode) -> List.of(node, connectedNode))
                    .collect(Collectors.toList()));
        }
        return edges;
    }

    /**
     * Copy forest.
     *
     * @return the forest
     */
    public Forest<E> copy() {
        return new Forest<>(getMap());
    }

    /**
     * Get levels list. Implements breadth search. Inspired by findPath solution I used one year ago for this course
     *
     * @param root the root
     * @return the levels
     */
    public List<List<E>> getLevels(final E root) {
        if (!this.contains(root)) {
            return new ArrayList<>();
        }
        final Set<E> visited = new HashSet<>();
        final List<List<E>> levels = new ArrayList<>(List.of(List.of(root)));
        visited.add(root);
        return getLevelsRecursive(visited, levels);
    }


    private List<List<E>> getLevelsRecursive(final Set<E> visited, final List<List<E>> levels) {
        List<E> lastNodes = levels.get(levels.size() - 1);

        List<E> newLevel = lastNodes.stream().flatMap((E x) -> this.edges.get(x).stream()
                .filter(isChildPredicate(visited))).distinct().sorted().collect(Collectors.toList());

        if (newLevel.isEmpty()) {
            return levels;
        }

        levels.add(newLevel);
        visited.addAll(newLevel);
        return getLevelsRecursive(visited, levels);
    }

    private Predicate<E> isChildPredicate(final Set<E> visited) {
        return (E y) -> !(visited.contains(y));
    }

    /**
     * Gets height.
     *
     * @param root the root
     * @return the height
     */
    public int getHeight(final E root) {
        return getLevels(root).size() - 1;
    }

    /**
     * List list.
     *
     * @return the list
     */
    public List<E> list() {
        return edges.keySet().stream().sorted().collect(Collectors.toList());
    }

    /**
     * Returns nodes connected to this node.
     *
     * @param element the element
     * @return the connected
     */
    public List<E> getConnected(final E element) {
        return getLevels(element).stream().flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * Disconnect boolean. Removes an undirected edge if successful.
     *
     * @param a first node
     * @param b second node
     * @return the boolean
     */
    public boolean disconnect(final E a, final E b) {
        boolean isLastEdge = this.edges.containsKey(a) && edges.get(a).contains(b) && this.size() == MIN_NODES;
        if (isLastEdge) {
            return false;
        }
        return this.remove(a, b);
    }

    /**
     * Remove boolean. Adds an edge to the instance if successful.
     *
     * @param a first node
     * @param b second node
     * @return the boolean
     */
    public boolean remove(final E a, final E b) {
        removeOneSided(a, b);
        return removeOneSided(b, a);
    }


    /**
     * Removes edge one entry in the edgeMap if successful.
     * @param a from edge
     * @param b to edge
     * @return boolean If edge successfully added.
     */
    private boolean removeOneSided(final E a, final E b) {
        if (edges.containsKey(a) && edges.get(a).remove(b)) {
            if (edges.get(a).isEmpty()) {
                edges.remove(a);
            }
            return true;
        }
        return false;
    }

    /**
     * Contains returns true if element in Graph
     *
     * @param element the element
     * @return the boolean
     */
    public boolean contains(final E element) {
        return this.edges.containsKey(element);
    }

    /**
     * Gets route in the graph between start and end node. Returns empty List if no route exists or start equals the
     * end.
     *
     * @param start the start
     * @param end   the end
     * @return the route
     */
    public List<E> getRoute(final E start, final E end) {
        boolean connected = getConnected(start).contains(end);

        if (!(this.contains(start) && this.contains(end)) || !connected || start.equals(end)) {
            return new ArrayList<>();
        }
        List<List<E>> levels = getLevels(start);
        // Since graph is a tree, the "end"-node only has one parent, which must be on the level above "end" in the
        // tree. We therefore find the route by going through the lineage of "end's" ancestors.
        while (!levels.get(levels.size() - 1).contains(end)) {
            levels.remove(levels.size() - 1);
        }
        List<E> route = new ArrayList<>();
        route.add(end);
        E currentNode = end;
        // We track the path backward (from branch to root) through the tree which is easier, since every node is
        // guaranteed to have one and only one parent.
        for (int i = levels.size() - 2; i >= 0; i--) {
            final E currentNodeCopy = currentNode; //We do this because Java does not support real closures.
            // The value of currentNode is not allowed to change after being used in the lambda, so we create a final
            // variable and the compiler stops complaining.
            List<E> connectedNodes = levels.get(i).stream().filter((E potentialParent) ->
                            this.get(potentialParent).contains(currentNodeCopy)).collect(Collectors.toList());
            connectedNodes.retainAll(levels.get(i));
            E parent = connectedNodes.get(0);
            route.add(parent);
            currentNode = parent;
        }
        Collections.reverse(route); //We need to reverse the list since we've gone backward from end to start.
        return route;
    }


    /**
     * Get list.
     *
     * @param element the element
     * @return the list
     */
    public List<E> get(final E element) {
        assert element != null;
        if (edges.get(element) == null) {
            return new ArrayList<E>();
        }
        return new ArrayList<>(edges.get(element));
    }

    /**
     * Add edge between first and second to the graph and returns true.
     *
     * @param first  the first node
     * @param second the second node
     * @return true if successful and false otherwise.
     */
    public boolean add(final E first, final E second) {
        if (first == null || second == null) {
            return false;
        }
        boolean addsLoop = edges.containsKey(first) && !edges.get(first).contains(second)
                && getConnected(first).contains(second); //a connected node that is not adjacent adds a loop to a graph
        if (addsLoop) {
            throw new ForestException(LOOP_FOREST);
        }
        addOneDirection(first, second);
        return addOneDirection(second, first);
    }

    //

    /**
     * Adds edge to edges in one direction.
     * @param first node
     * @param second node
     * @return true if successfully added edge.
     */
    private boolean addOneDirection(final E first, final E second) {
        if (edges.containsKey(first)) {
            final List<E> connectedNodes = edges.get(first);
            if (!connectedNodes.contains(second)) { //we only want to add the node if it is not already connected.
                connectedNodes.add(second);
                edges.put(first, connectedNodes);
                return true;
            }
            return false;
        } else {
            edges.put(first, new ArrayList<>(List.of(second)));
            return true;
        }
    }


//    /**
//     * Returns if the instance is a valid Forest.
//     *
//     * @return if every Tree in the Forest has a valid tree topology.
//     */
//    public boolean isForest() {
//        List<E> nodes = this.list();
//        if (nodes.size() < MIN_NODES) {
//            return false;
//        }
//
//        while (!nodes.isEmpty()) {
//            E currentRoot = nodes.get(0);
//            List<E> connectedSubGraph = new ArrayList<>(getConnected(currentRoot));
//            nodes.removeAll(connectedSubGraph);
//            if (!isTree(connectedSubGraph)) return false;
//        }
//        return true;
//    }

//    private boolean isTree(final List<E> connectedSubGraph) {
//        // For every node, get the sum of the number of connected nodes
//        // which is equivalent to the number of directed edges,
//        // which is half the number of undirected edges.
//        long subgraphEdgeNumber = (this.edges.values().stream()
//                .flatMap(node -> node.stream().filter(connectedSubGraph::contains)).count() / DIRECTED_EDGE_PER_EDGE);
//
//        // For a graph to be a tree, it needs to be connected, and every child is connected to exactly one
//        // parent. Thus, the number of edges must be the number of children, which is the number of nodes
//        // minus the root node. As a formula |E| = |V| - 1.
//        return subgraphEdgeNumber == connectedSubGraph.size() - 1;
//    }
//
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Forest)) return false;
        Forest<?> forest = (Forest<?>) o;
        //we compare the sets since order of the Edges does not matter.
        Set<Set<E>> first = this.getEdgeSets();
        Set<? extends Set<?>> second = forest.getEdgeSets();
        return Objects.equals(first, second);
    }


    /**
     * Returns the number of nodes in tree.
     *
     * @return the number of nodes in tree.
     */
    public int size() {
        return this.list().size();
    }

    @Override
    public int hashCode() {
        Set<Set<E>> collect = getEdgeSets();
        return Objects.hash(collect);
    }


    private Set<Set<E>> getEdgeSets() {
        return this.getEdges().stream().map(HashSet::new).collect(Collectors.toSet());
    }
}
