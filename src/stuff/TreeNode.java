package stuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Tree node.
 *
 * @param <E> the type parameter
 */
public class TreeNode<E> {
    private final E value;
    /*
     * @project FinalAssignmentNoBloat
     * @author Tassilo Neubauer
     */
    /**
     * The Children.
     */
    private List<TreeNode<E>> children;
    /**
     * The Parent.
     */
    private TreeNode<E> parent;
    private int level;

    /**
     * Instantiates a new Tree node.
     *
     * @param value  the value
     * @param parent the parent
     */
    public TreeNode(E value, TreeNode<E> parent) {
        this.parent = parent;
        this.value = value;
        this.children = new ArrayList<>();
        if (!(parent==null)){
            parent.addChild(this);
        }
    }

    @Override
    public boolean equals(final Object o) {
        //todo might want to include level
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode<?> treeNode = (TreeNode<?>) o;
        if (parent.equals(treeNode.parent) || children.size() != treeNode.getChildSize()) return false;
        if (children.size() == 0) return true;
        for (TreeNode<E> child: children) {
            if (!treeNode.getChildren().contains(child)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(children, parent);
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
    public ArrayList<TreeNode<E>> getChildren(){
        // TODO: 16.02.22 might be faster without copy
        return new ArrayList<TreeNode<E>>(children);
    }

    /**
     * Add child.
     *
     * @param child the child
     */
    public void addChild(TreeNode<E> child) {
        this.children.add(child);
    }

    /**
     * Sets level.
     *
     * @param level the level
     */
    public void setLevel(int level) {
        this.level = level;
        if (level >= parent.getLevel() -1){
            parent.setLevel(level+1);
        }
    }
    public int getLevel(){
        return level;
    }
    


}
