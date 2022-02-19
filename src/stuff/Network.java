package stuff;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static stuff.IP.REGEX_IP;

/**
 * The type Network.
 */
public class Network {
    // TODO: 18.02.22 check for at least two matches within parenthesis
    // TODO: 18.02.22 nachgeschaut, dass überall objectreferenz/equals wie noetig verwendet wurde
    // TODO: 18.02.22 geschaut, was in Treenode kann
    // TODO: 18.02.22 look at codecoverage
    // TODO: 18.02.22 koennen wir bei Add davon ausgehen, dass das subnetz legal ist? Wie groß dürfen die
    // überschneidungen der Verbindungen sein?

    private SortedSet<Node<IP>> nodes;
    private UndirectedTree graph;

    /**
     * The constant REGEX_NODE.
     */
    public static final String REGEX_NODE = REGEX_IP + "|(" + REGEX_IP + ")+"; // TODO: 18.02.22 do we need this
    // second part?

    /**
     * Instantiates a new Network.
     *
     * @param root     the root
     * @param children the children
     */
    public Network(final IP root, final List<IP> children) {
        // TODO: 16.02.22 find a more elegant solution where it's obvious which elements get changed? (make parent
        //  part of arguments?

        this.nodes = new TreeSet<>();
        Node<IP> rootNode = new Node<>(root);
        this.nodes.add(new Node<>(root));
        for (IP child : children) {
            Node<IP> leaf = new Node<>(child);
            leaf.addUndirectedEdge(rootNode);
        }
        if (!rootNode.isDirected()) {
            throw new RuntimeException("There is a loop in this Tree!");
        }
    }


    /**
     * Parse network tree node.
     *
     * @param bracketNotation the bracket notation
     * @return the tree node
     * @throws ParseException the parse exception
     */
    public static IP parseLispTree(final String bracketNotation) throws ParseException {
        return parseLispTree(bracketNotation, null);
    }

    /**
     * Parse network tree node.
     *
     * @param bracketNotation the bracket notation
     * @param parent          the parent
     * @return the tree node
     * @throws ParseException the parse exception
     */
    public static IP parseLispTree(final String bracketNotation, Node<IP> parent) throws ParseException {
        // TODO: 17.02.22 check that at least one Node
        // TODO: 17.02.22 check how the rules handle whitespace in general
        // TODO: 17.02.22 is there an easy way to do substitution in java? that way we could substitute all the
        //  strings that are inside of parenthesis.
        // TODO: 18.02.22 refactor part for parsing IP
        if (bracketNotation.charAt(0) == '(' && bracketNotation.charAt(bracketNotation.length() - 1) == ')') {
            String ipString = bracketNotation.substring(1, bracketNotation.length() - 1);
            Pattern nodePattern = Pattern.compile(REGEX_IP); // TODO: 18.02.22 rename ipk?
            Matcher nodeMatcher = nodePattern.matcher(ipString);
            List<String> nodeStrings = new ArrayList<>();

            // TODO: 18.02.22 figure out how not to match empty strings (and make more robust in general?j
            while (nodeMatcher.find()){
                nodeStrings.add(nodeMatcher.group());
            }

            UndirectedTree graph = new HashmapGraph();
            IP root = new IP(nodeStrings.remove(0));
            graph.add(root, parent);
            //would have just used map here if java wasn't so annoying with exceptions in lambdas
            for (String nodeString : nodeStrings) {
                parseLispTree(nodeString, root);
            }
            return root;
        }
        IP leaf = new IP(bracketNotation);
        leaf.addUndirectedEdge(parent);
        return leaf;
    }

    /**
     * Instantiates a new Network.
     *
     * @param bracketNotation the bracket notation
     * @throws ParseException the parse exception
     */
    public Network(final String bracketNotation) throws ParseException {
        // TODO: 16.02.22 implement
        this.graph = new HashmapGraph(new TreeSet<>().addAll(parseLispTree(bracketNotation).getConnected()));
    }


    /**
     * Add boolean.
     *
     * @param subnet the subnet
     * @return the boolean
     */
    public boolean add(final Network subnet) {
        // TODO: 16.02.22 implement
        // TODO: 18.02.22 Is this new node only allowed to add nodes, or is the root replacing the old one?
        // TODO: MUST both have an overlapping subnet? (must the resulting Tree have a strictly greater list than the
        // other?
        return false;
    }

    /**
     * List list.
     *
     * @return the list
     */
    public List<IP> list() {
        return this.nodes.stream().map(Node::list).flatMap(List::stream).sorted().collect(Collectors.toList());
    }

    /**
     * Connect boolean.
     *
     * @param ip1 the ip 1
     * @param ip2 the ip 2
     * @return the boolean
     */
    public boolean connect(final IP ip1, final IP ip2) {
        // TODO: 16.02.22 implement
        // TODO? is die richtung in die hinzugefügt wird egal?
        // TODO: 17.02.22 What happens if the thing is nowhere connected?
        return false;
    }

    /**
     * Disconnect boolean.
     *
     * @param ip1 the ip 1
     * @param ip2 the ip 2
     * @return the boolean
     */
    public boolean disconnect(final IP ip1, final IP ip2) {
        // TODO: 16.02.22 implement 
        return false;
    }

    /**
     * Contains boolean.
     *
     * @param ip the ip
     * @return the boolean
     */
    public boolean contains(final IP ip) {
        return this.nodes.stream().anyMatch((Node<IP> x) -> x.getValue() == ip);
    }

    /**
     * Gets height.
     *
     * @param root the root
     * @return the height
     */
    public int getHeight(final IP root) {
        // // TODO: 16.02.22 check that this gets set properly
        if (!list().contains(root)) {
            return 0;
        }
        return getNode(root).getLevel();
    }


    /**
     * Get levels list. Implements breadth search. Inspired by findPath solution I used one year ago for this course
     *
     * @param root the root
     * @return the list
     */
    List<List<IP>> getLevels(final IP root){
        final Set<IP> visited = new HashSet<>();
        final Queue<IP> nextNodes = new LinkedList<>();
        nextNodes.add(root);
        final Map<String, String> parents = new HashMap<>();

    }

    private Node<IP> getNode(IP ip){
        // TODO: 18.02.22 figure out what to do when assertion is violated
        // TODO: 18.02.22 figure out whether we want to have copy or not?
        //currently returns null if not found
        // TODO: 18.02.22 make this work: assert root.list().contains(ip);
        for (Node<IP> root: nodes) {
            Node<IP> currentNode = getNode(ip, root);
            if (currentNode != null) {
                return currentNode;
            }
        }
        return null;
    }

    private Node<IP> getNode(IP ip, Node<IP> currentNode){
        // TODO: 18.02.22 implement breitensuche as well?
        assert currentNode !=null;
        if (currentNode.getValue().equals(ip)){
            return currentNode;
        }
        if (!currentNode.getConnectedNodes().isEmpty()) {
            for (Node<IP> child: currentNode.getConnectedNodes()){
                Node<IP> childNode = getNode(ip, child);
                if (childNode != null) {
                    return childNode;
                }

            }

        }
        return null;
    }

    /**
     * Gets route.
     *
     * @param start the start
     * @param end   the end
     * @return the route
     */
    public List<IP> getRoute(final IP start, final IP end) {
        // TODO: 16.02.22 implement 
        return null;
    }



    /**
     * To string string.
     *
     * @param root the root
     * @return the string
     */
    public String toString(IP root) {
        // TODO: 17.02.22 Make case if root not in my nodes
        // TODO: 18.02.22 find out what to return on error?
        // TODO: 18.02.22 innterhalb der Klammerschreibweise nach Wert sortieren (breitensuche?)
        if (getNode(root)==null) {
            return "";
        }
        return getNode(root).toString();

    }
    // TODO: 16.02.22 Implement


    @Override
    public boolean equals(final Object o) {
        // TODO: 18.02.22 Check this actually true
        // implement by comparing all roots
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Network network = (Network) o;
        //return roots.stream().allMatch(x -> )
        return false;
    }

}
