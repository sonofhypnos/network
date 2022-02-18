package stuff;

import java.util.*;

/**
 * The type Tree node.
 *
 * @param <E> the type parameter
 */
public class TreeNode<E extends Comparable<E>>  {
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
    private final List<TreeNode<E>> children;
    /**
     * The Parent.
     */
    private final TreeNode<E> parent;
    private int level;



    /**
     * Instantiates a new Tree node.
     *
     * @param value  the value
     * @param parent the parent
     */
    public TreeNode(E value, final TreeNode<E> parent) {
        this.parent = parent;
        this.value = value;
        this.children = new ArrayList<>();
        if (!(parent==null)){
            parent.addChild(this);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode<?> treeNode = (TreeNode<?>) o;
        if (parent.equals(treeNode.parent) || children.size() != treeNode.getChildSize()) return false;
        if (children.size() == 0) return true;
        // TODO: 17.02.22 Equals method shsshoudl probably change here
        for (TreeNode<E> child: children) {
            if (!treeNode.getChildren().contains(child)) return false;
        }
        return true;
    }
    
    protected Boolean isTree(){
        // TODO: 18.02.22 kann man einfach schauen, ob list eintrag doppelt hat?
        return this.list().stream().distinct().count() != this.list().size();
    }

    /**
     * Get child size int.
     *
     * @return the int
     */
    public int getChildSize(){
        return children.size();
    }

    /**
     * Get children array list.
     *
     * @return the array list
     */
    public List<TreeNode<E>> getChildren(){
        // TODO: 17.02.22 copy?
        return List.copyOf(children);
    }

    /**
     * Add child.
     *
     * @param child the child
     */
    public void addChild(TreeNode<E> child) {
        // TODO: 18.02.22 remove if not used?
        this.children.add(child);
    }

    /**
     * Sets level. Explain relation to Unify-datastructure
     *
     * @param level the level
     */
    public void setLevel(int level) {
        this.level = level;
        if (isRoot()) {
            return;
        }
        if (level >= parent.getLevel() -1){
            parent.setLevel(level+1);
        }
    }

    private Boolean isRoot(){
        return this.parent == null;
    }

    /**
     * Get level int.
     *
     * @return the int
     */
    public int getLevel(){
        return level;
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
        for (TreeNode<E> child: children) {
            elements.addAll(child.list()); //todo is this copying?
        }
        return elements;
    }

    public E getValue() {
        return value;
    }

    public String toString() {
        if (getChildren().size() == 0){
            return this.value.toString();
        }
        List<String> ipStrings = new ArrayList<>();
        ipStrings.add(this.value.toString());

        List<TreeNode<E>> children = getChildren();
        children.sort(Comparator.comparing(TreeNode::getValue));
        for (TreeNode<E> child: getChildren() ) {
            ipStrings.add(child.toString());
        }
        return "(" + String.join(" ", ipStrings) + ")";
    }
}
