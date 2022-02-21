package stuff;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Hashmap graph.
 *
 * @param <E> the type parameter
 */
public class HashmapGraph<E extends Comparable<E>> implements UndirectedTree<E> {
    /**
     * The constant DIRECTED_EDGE_PER_EDGE.
     */
    public static final int DIRECTED_EDGE_PER_EDGE = 2;
    /*
     * @project FinalAssignmentNoBloat
     * @author Tassilo Neubauer
     */

    private HashMap<E, List<E>> edges;

    /**
     * Instantiates a new Hashmap graph.
     *
     * @param nodes the nodes
     */
    public HashmapGraph(HashMap<E, List<E>> nodes) {
        this.edges = copyEdges(nodes);
    }

    /**
     * Instantiates a new Hashmap graph.
     */
    public HashmapGraph() {
        this.edges = new HashMap<>();
    }

    private HashMap<E, List<E>> copyEdges(final HashMap<E, List<E>> nodes) {
        // TODO: 19.02.22 asserts already proper format!
        HashMap<E, List<E>> newEdges = new HashMap<>();
        for (E firstNode : nodes.keySet()) {
            List<E> nextNodes = new ArrayList<>(nodes.get(firstNode));
            newEdges.put(firstNode, nextNodes);
        }
        return newEdges;
    }

    private HashMap<E, List<E>> getMap() {
        // TODO: 20.02.22 test this actually works? use library function to copy stuff?
        return copyEdges(edges);
    }

    @Override
    public List<List<E>> getEdges() {
        List<List<E>> edges = new ArrayList<>();
        HashMap<E, List<E>> newMap = getMap();
        for (E node : newMap.keySet()) {
            edges.addAll(newMap.get(node).stream().map((E connectedNode) -> List.of(node, connectedNode))
                    .collect(Collectors.toList()));
        }
        return edges;
    }

    @Override
    public HashmapGraph<E> copy() {
        return new HashmapGraph<>(getMap());
    }

    @Override
    public List<List<E>> getLevels(final E root) {
        final Set<E> visited = new HashSet<>();
        final List<List<E>> levels = new ArrayList<>(List.of(List.of(root)));
        visited.add(root);
        return getLevelsRecursive(visited, levels);
    }


    private List<List<E>> getLevelsRecursive(final Set<E> visited, final List<List<E>> levels) {
        // TODO: 20.02.22 what is final doing here?
        // TODO: 19.02.22 maybe be simplified with tree assumption
        List<E> lastNodes = levels.get(levels.size() - 1);
        List<E> newLevel = lastNodes.stream().flatMap((E x) ->
                        this.edges.get(x).stream().filter((E y) -> !(visited.contains(y))))
                .distinct().sorted().collect(Collectors.toList());
        if (newLevel.isEmpty()) {
            return levels;
        }
        levels.add(newLevel);
        visited.addAll(newLevel);
        return getLevelsRecursive(visited, levels);
    }

    @Override
    public int getHeight(final E root) {
        return getLevels(root).size() - 1;
    }

    @Override
    public List<E> list() {
        // TODO: 19.02.22 make pretty
        return edges.keySet().stream().sorted().collect(Collectors.toList());
    }

    private List<E> getConnected(E element) {
        // TODO: 20.02.22 make sure this does create a new list?
        return getLevels(element).stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public boolean add(final UndirectedTree<E> tree) {
        // TODO: 19.02.22
        return false;
    }

    @Override
    public boolean connect(final E a, final E b) {
        // TODO: 19.02.22  
        return false;
    }

    @Override
    public boolean disconnect(final E a, final E b) {
        assert !(this.edges.keySet().size() < 2);
        if (this.edges.keySet().size() == 2) { //last edge cannot be removed
            return false;
        }
        return remove(a, b);
    }

    @Override
    public boolean remove(final E a, final E b) {
        removeOneSided(a, b);
        return removeOneSided(b, a);
    }

    private boolean removeOneSided(final E a, final E b) {
        // TODO: 19.02.22 changes side-effects!
        if (edges.containsKey(a) && edges.get(a).remove(b)) {
            if (edges.get(a).isEmpty()) {
                edges.remove(a);
            }
            return true;
        }
        return false;
    }

    private void disconnect(final E element) {
        for (E child : this.edges.get(element)) {
            this.edges.remove(child, element);
        }
    }

    @Override
    public boolean contains(final E element) {
        return this.edges.containsKey(element);
    }

    @Override
    public List<E> getRoute(final E start, final E end) {
        return null;
    }

    @Override
    public List<E> get(final E element) {
        assert element != null;
        if (edges.get(element) == null) {
            return new ArrayList<E>();
        }
        return new ArrayList<>(edges.get(element));
    }

    @Override
    public void add(final E first, final E second) {
        // TODO: 19.02.22 needs documentation that we modify state
        // TODO: 20.02.22 what if connection already exists? Do we keep quiet?
        assert first != null;
        //assert !first.equals(second); //why assert this? // TODO: 20.02.22 check if necessary
        if (second == null) {
            return;
        }
        addOneDirection(first, second);
        addOneDirection(second, first);
    }

    private void addOneDirection(final E first, final E second) {
        if (edges.containsKey(first)) {
            final List<E> oldEdge = edges.get(first);
            if (!oldEdge.contains(second)) { //we only want to add the node if it is not already connected.
                oldEdge.add(second);
                return;
            }
        } else {
            edges.put(first, new ArrayList<>(List.of(second)));
        }
    }


    @Override
    public boolean isTree() {
        // TODO: 19.02.22 think about conserving this explicitly?
        // TODO: 19.02.22 what if empty
        // TODO: 20.02.22 write tests for isTree?
        List<E> nodes = this.list();
        while (!nodes.isEmpty()) {
            E currentRoot = nodes.get(0);
            List<E> subgraph = new ArrayList<>(getConnected(currentRoot));
            nodes.removeAll(subgraph);
            // directed edges in both directions
            long subgraphEdgeNumber = (this.edges.values().stream()
                  .flatMap(x -> x.stream().filter(subgraph::contains)).count() / DIRECTED_EDGE_PER_EDGE);
            // wedivide by 2 since we model
            // the undirected edges with 2 directed edges
            if (subgraphEdgeNumber != subgraph.size() - 1) {
                // See https://en.wikipedia.org/wiki/Tree_(graph_theory) for why this is equivalent to a tree
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(final UndirectedTree<E> tree) {
        // TODO: 21.02.22 figure out if overloading is enough for tutors
        E root = this.list().get(0);
        return this.getLevels(root).equals(tree.getLevels(root));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEdges());
    }
}
