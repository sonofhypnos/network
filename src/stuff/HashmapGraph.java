package stuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HashmapGraph<E extends Comparable<E>> implements UndirectedTree <E>{
    /*
     * @project FinalAssignmentNoBloat
     * @author Tassilo Neubauer
     */

    private HashMap<E,List<E>> edges;
    
    public HashmapGraph(HashMap<E,List<E>> nodes){
        this.edges = copyEdges(nodes);
    }

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

    public HashmapGraph<E> copy(){
        return new HashmapGraph<>(copyEdges(edges));
    }

    public HashmapGraph(List<List<Node<E>>> levels){

    }

    @Override
    public List<List<E>> getLevels(final IP root) {
        return null;
    }

    @Override
    public List list() {
        // TODO: 19.02.22 make pretty
        return (List) Collections.unmodifiableSet(edges.keySet());
    }

    @Override
    public boolean add(final UndirectedTree tree) {
    }

    @Override
    public boolean connect(final E a, final E b) {
        newGraph = this.copy()
    }

    @Override
    public boolean disconnect(final E a, final E b) {
        return false;
    }

    @Override
    public boolean contains(final E element) {
        return false;
    }

    @Override
    public int getHeight(final E root) {
        return 0;
    }

    @Override
    public List<List<E>> getLevels(final E root) {
        return null;
    }

    @Override
    public List<E> getRoute(final E start, final E end) {
        return null;
    }

    @Override
    public void add(final E first, final E second){
        // TODO: 19.02.22 needs documentation that we modify state
        assert !edges.get(first).contains(second);
        assert !first.equals(second);
        if (edges.containsKey(first)){
            final List<E> oldEdge = edges.get(first);
            oldEdge.add(second);
        } else {
            edges.put(first, new ArrayList<>(List.of(second)));
        }

    }

    @Override
    public boolean isTree() {
        return false;
    }


}
