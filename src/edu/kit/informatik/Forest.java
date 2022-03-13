package edu.kit.informatik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Forest Class contains all functions for a generic Forest, a collection of undirected trees.
 *
 * @param <E> type of nodes. Is expected to be immutable.
 * @author upkim
 * @version 1.0 2022-03-12 22:13
 */
public class Forest<E> {

    private final Map<E, List<E>> edges;

    /**
     * Instantiates a new Forest from Map (only used for copying).
     *
     * @param nodes the map for the new forest
     */
    private Forest(final Map<E, List<E>> nodes) {
        this.edges = copyEdges(nodes);
    }

    /**
     * Instantiates a new Forest object without any Nodes.
     */
    public Forest() {
        this.edges = new HashMap<>();
    }

    private Map<E, List<E>> copyEdges(final Map<E, List<E>> nodes) {
        HashMap<E, List<E>> newEdges = new HashMap<>();
        for (E firstNode : nodes.keySet()) {
            List<E> nextNodes = new ArrayList<>(nodes.get(firstNode));
            newEdges.put(firstNode, nextNodes);
        }
        return newEdges;
    }

    /**
     * Returns Set of undirected edges in the Forest.
     *
     * @return the edges of the forest
     */
    public Set<List<E>> getAdjacencySet() {
        List<List<E>> adjacencyList = new ArrayList<>();
        for (E node : edges.keySet()) {
            //add list of sorted (node, adjacentNode) pairs
            adjacencyList.addAll(edges.get(node).stream()
                    .map((E adjacentNode) -> Stream.of(node, adjacentNode).sorted().collect(Collectors.toList()))
                    .collect(Collectors.toList()));
        }
        return new HashSet<>(adjacencyList);
    }

    /**
     * Copies forest, but does not copy contained nodes. Using this method with mutable nodes is not recommended.
     *
     * @return copy of the forest
     */
    public Forest<E> copy() {
        return new Forest<>(copyEdges(edges));
    }

    /**
     * Get levels returns the nodes below the root node in lists by their level in the tree.
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
        List<E> oldLevel = levels.get(levels.size() - 1);

        //get the next level (all children of oldLevel) by getting all adjacent nodes that are not already visited.
        List<E> newLevel = oldLevel.stream().flatMap((E x) -> getAdjacent(x).stream().filter(isChildPredicate(visited)))
                .distinct().sorted().collect(Collectors.toList());

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
     * Gets height of the tree from root.
     *
     * @param root the root
     * @return the height
     */
    public int getHeight(final E root) {
        return getLevels(root).size() - 1;
    }

    /**
     * List returns new list of the Nodes.
     *
     * @return list of edges
     */
    public List<E> list() {
        return getNodes().stream().sorted().collect(Collectors.toList());
    }

    private Set<E> getNodes() {
        return edges.keySet();
    }

    /**
     * Remove edges between a graph even if it violates the Guarantees for a Forest.
     *
     * @param a first node
     * @param b second node
     * @return true iff successfully removed.
     */
    public boolean remove(final E a, final E b) {
        removeOneSided(a, b);
        return removeOneSided(b, a);
    }


    /**
     * Removes edge one entry in the edgeMap if successful.
     *
     * @param a from edge
     * @param b to edge
     * @return true iff edge successfully added.
     */
    private boolean removeOneSided(final E a, final E b) {
        if (edges.containsKey(a) && this.edges.get(a).remove(b)) {
            if (getAdjacent(a).isEmpty()) {
                edges.remove(a);
            }
            return true;
        }
        return false;
    }

    /**
     * Contains returns true if element is in the Graph
     *
     * @param element the element
     * @return iff this contains element.
     */
    public boolean contains(final E element) {
        return edges.containsKey(element);
    }

    /**
     * Gets route in the graph between start and end node. Returns empty List if no route exists or start equals the
     * end.
     *
     * @param start the start
     * @param end   the end
     * @return the route from start to end
     */
    public List<E> getRoute(final E start, final E end) {
        if (!(this.contains(start) && this.contains(end)) || !areConnected(start, end) || start.equals(end)) {
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
        // guaranteed to have one and only one parent
        for (int i = levels.size() - 2; i >= 0; i--) {
            final E currentNodeCopy = currentNode; //We make this final copy of the node because Java does not support
            // real closures: external variables used inside the lambda must be final.

            //We filter the level above currentNode by whether they are adjacent to CurrentNode.
            List<E> parentList = levels.get(i).stream()
                    .filter((E potentialParent) -> areAdjacent(potentialParent, currentNodeCopy))
                    .collect(Collectors.toList());
            assert parentList.size() == 1;
            E parent = parentList.get(0);
            route.add(parent);
            currentNode = parent;
        }
        Collections.reverse(route); //We need to reverse the list since we've gone backward from end to start.
        return route;
    }

    /**
     * Returns true iff start and end are connected in the forest.
     *
     * @param start the start
     * @param end   the end
     * @return the boolean
     */
    public boolean areConnected(final E start, final E end) {
        if (start == null || end == null) {
            return false;
        }
        return getLevels(start).stream().flatMap(List::stream).collect(Collectors.toList()).contains(end);
    }

    /**
     * Returns true iff start and end are adjacent in the forest.
     *
     * @param first  the first
     * @param second the second
     * @return the boolean
     */
    public boolean areAdjacent(final E first, final E second) {
        if (first == null || second == null) {
            return false;
        }
        return this.getAdjacent(first).contains(second);
    }

    /**
     * Get list of edges adjacent to element. Returns empty list if input is null.
     *
     * @param element the element
     * @return adjacent edges
     */
    public List<E> getAdjacent(final E element) {
        if (edges.get(element) == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(edges.get(element));
    }

    /**
     * Add edge between first and second to the graph and returns if there was no edge between them before and if it
     * does not violate the forest format.
     *
     * @param first  the first node
     * @param second the second node
     * @return true iff successful.
     */
    public boolean add(final E first, final E second) {
        if (first == null || second == null) {
            return false;
        }
        if (areConnected(first, second)) {
            return false;
            //If first and second are already adjacent, we cannot add a new node. If they aren't, but are connected then
            //adding an edge without adding a new node creates a loop in a connected subgraph <=> not a tree anymore.
        }
        addOneDirection(first, second);
        return addOneDirection(second, first);
    }

    private boolean addOneDirection(final E first, final E second) {
        if (edges.containsKey(first)) {
            final List<E> adjacentNodes = getAdjacent(first);
            if (!adjacentNodes.contains(first)) {
                adjacentNodes.add(second);
                edges.put(first, adjacentNodes);
                return true;
            }
            return false;
        } else {
            edges.put(first, new ArrayList<>(List.of(second)));
            return true;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Forest)) return false;
        Forest<?> forest = (Forest<?>) o;
        //we compare the edges, since order of the Edges does not matter.
        Set<List<E>> first = this.getAdjacencySet();
        Set<? extends List<?>> second = forest.getAdjacencySet();
        return Objects.equals(first, second);
    }


    @Override
    public int hashCode() {
        return Objects.hash(getAdjacencySet());
    }


}
