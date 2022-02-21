package stuff;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static stuff.IP.REGEX_IP;

/**
 * The type Network.
 *
 * @author :Tassilo Neubauer
 * @version : 1.0
 */
public class Network {
    // TODO: 18.02.22 check for at least two matches within parenthesis
    // TODO: 18.02.22 nachgeschaut, dass überall objectreferenz/equals wie noetig verwendet wurde
    // TODO: 18.02.22 geschaut, was in Treenode kann
    // TODO: 18.02.22 look at codecoverage
    // TODO: 18.02.22 koennen wir bei Add davon ausgehen, dass das subnetz legal ist? Wie groß dürfen die
    // überschneidungen der Verbindungen sein?

    /**
     * The constant REGEX_NODE.
     */
    public static final String REGEX_NODE = REGEX_IP + "|(" + REGEX_IP + ")+"; // TODO: 18.02.22 do we need this
    private UndirectedTree<IP> graph;
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
        if (root == null || children == null || children.contains(root)) {
            // TODO: 20.02.22 benutze eigene Runtime-Exception?
            // TODO: 20.02.22 Heist das wir sollen Runtime-Klasse verwenden, oder wir sollen eine eigene implementieren?
            //  Für mich nonobvious
            throw new RuntimeException("Given ... does not describe a Tree Topology");  // TODO: 19.02.22 pretty
        }
        this.graph = new HashmapGraph<>();
        for (IP child : children) {
            graph.add(root, child);
        }
    }


    /**
     * Instantiates a new Network.
     *
     * @param bracketNotation the bracket notation
     * @throws ParseException the parse exception
     */
    public Network(final String bracketNotation) throws ParseException {
        // TODO: 16.02.22 implement
        this.graph = parseLispTree(bracketNotation);
        assert this.graph.isTree();
    }

    /**
     * Parse network tree node.
     *
     * @param bracketNotation the bracket notation
     * @return the tree node
     * @throws ParseException the parse exception
     */
    public static UndirectedTree<IP> parseLispTree(final String bracketNotation) throws ParseException {
        UndirectedTree<IP> graph = new HashmapGraph<>();
        return parseLispTree(bracketNotation, null, graph);
    }

    /**
     * Parse network tree node.
     *
     * @param bracketNotation the bracket notation
     * @param parent          the parent
     * @param graph           the graph
     * @return the tree node
     * @throws ParseException the parse exception
     */
    public static UndirectedTree<IP> parseLispTree(final String bracketNotation, IP parent, UndirectedTree<IP> graph) throws ParseException {
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
            while (nodeMatcher.find()) {
                nodeStrings.add(nodeMatcher.group());
            }

            IP root = new IP(nodeStrings.remove(0));
            graph.add(root, parent);
            //would have just used map here if java wasn't so annoying with exceptions in lambdas
            for (String nodeString : nodeStrings) {
                parseLispTree(nodeString, root, graph);
            }
            return graph;
        }
        IP leaf = new IP(bracketNotation);
        graph.add(leaf, parent);
        return null;
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
        // TODO: 20.02.22 was ist wennn es keinen Overlap gibt?
        // other?
        if (subnet == null) {
            return false;
        }
        UndirectedTree<IP> newGraph = this.graph.copy();
        List<List<IP>> newEdges = subnet.graph.getEdges();
        // TODO: 20.02.22 addStuff
        // TODO: 20.02.22 handle adding loop
        for (List<IP> edge : newEdges) {
            newGraph.add(edge.get(0), edge.get(1));
        }
        if (!newGraph.isTree() || this.graph.equals(newGraph)) {
            return false;
        }
        this.graph = newGraph;
        return true;
    }

    /**
     * List list.
     *
     * @return the list
     */
    public List<IP> list() {
        return this.graph.list();
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
        // TODO: 19.02.22
        // TODO: 19.02.22 remove stuff if shit gets real
        return graph.disconnect(ip1, ip2);
    }

    /**
     * Contains boolean.
     *
     * @param ip the ip
     * @return the boolean
     */
    public boolean contains(final IP ip) {
        return this.graph.contains(ip);
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
        return this.graph.getHeight(root);
    }


    /**
     * Get levels list. Implements breadth search. Inspired by findPath solution I used one year ago for this course
     *
     * @param root the root
     * @return the list
     */
    List<List<IP>> getLevels(final IP root) {
        if (root == null) {
            return new ArrayList<>();
        }
        return graph.copy().getLevels(root);
    }

    /**
     * Gets route.
     *
     * @param start the start
     * @param end   the end
     * @return the route
     */
    public List<IP> getRoute(final IP start, final IP end) {
        if (start == null || end == null) {
            return new ArrayList<>();
        }
        List<List<IP>> levels = getLevels(start);
        boolean connected = levels.stream().anyMatch((Collection<IP> level) -> level.contains(end));

        if (!(graph.contains(start) && graph.contains(end) && connected)) {
            return new ArrayList<>();
        }
        // Since graph is a tree, the "end"-node only has one parent, which must be on the level above "end" in the
        // tree. We therefore find the route by going through the lineage of "end's" ancestors.
        while (!levels.get(levels.size() - 1).contains(end)) {
            levels.remove(levels.size() - 1);
        }
        List<IP> route = new ArrayList<>();
        route.add(end);
        // TODO: 21.02.22 find better solution for this
        var ref = new Object() {
            IP currentIP = end;
        };
        for (int i = levels.size() - 2; i >= 0; i--) { // TODO: 19.02.22 don't get burned for this again
            List<IP> connectedNodes = levels.get(i).stream().filter((IP potentialParent) -> graph.get(potentialParent).contains(ref.currentIP)).collect(Collectors.toList());
            connectedNodes.retainAll(levels.get(i));
            IP parent = connectedNodes.get(0);
            route.add(parent);
            ref.currentIP = parent;
        }
        Collections.reverse(route); //we need to reverse the list since we've gone backward.
        return route;
        // TODO: 19.02.22 check if not in thingy
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
        UndirectedTree<IP> newGraph = this.graph.copy();
        return toStringRecursive(root, newGraph);
    }

    private String toStringRecursive(IP root, UndirectedTree<IP> newGraph) {
        if (newGraph.get(root).size() == 0) {
            return root.toString();
        }
        List<String> ipStrings = new ArrayList<>();
        ipStrings.add(root.toString());

        List<IP> children = newGraph.get(root);
        Collections.sort(children);

        for (IP child : children) {
            newGraph.remove(root, child);
            ipStrings.add(toStringRecursive(child, newGraph));
        }
        return "(" + String.join(" ", ipStrings) + ")"; // TODO: 18.02.22 make this seperate thingy
    }

    @Override
    public boolean equals(final Object o) {
        // TODO: 20.02.22 darf ich das mit Object?
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Network network = (Network) o;

        IP root = list().get(0);
        return this.getLevels(root).equals(network.getLevels(root));
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph);
    }
}
