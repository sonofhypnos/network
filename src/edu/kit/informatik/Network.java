package edu.kit.informatik;

import model.TreeTopology;
import resources.Errors;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Network.
 *
 * @author :upkim
 * @version : 1.0
 */
public class Network {
    // TODO: 05.03.22 add final keywords!
    // TODO: 18.02.22 check for at least two matches within parenthesis
    // TODO: 18.02.22 nachgeschaut, dass überall objectreferenz/equals wie noetig verwendet wurde
    // TODO: 18.02.22 geschaut, was in Treenode kann
    // TODO: 18.02.22 look at codecoverage
    // TODO: 18.02.22 koennen wir bei Add davon ausgehen, dass das subnetz legal ist? Wie groß dürfen die
    // überschneidungen der Verbindungen sein?

    /**
     * The constant REGEX_NODE.
     */
    public static final String PREFIX = "(";
    /**
     * The constant END_CHAR.
     */
    public static final String SUFFIX = ")";
    /**
     * The constant REGEX_NODE.
     */
    public static final String REGEX_NODE = "\\(.*\\)";
    /**
     * The constant SEPARATION_STRING.
     */
    public static final String SEPARATION_STRING = " ";
    /**
     * The constant SEP_NUMBER.
     */
    public static final int SEP_NUMBER = 2; //that this value is 2 instead of 1 has to do with the peculiarities of
    /**
     * The constant THERE_IS_A_BRACKET_MISMATCH_IN_THE_PROVIDED_STRING.
     */
// the split-function separating the string n-1 times.
    public static final String THERE_IS_A_BRACKET_MISMATCH_IN_THE_PROVIDED_STRING = "there is a parenthesis mismatch " + "in the" + " provided string";
    /**
     * The constant NETWORK_DOES_NOT_DESCRIBE_A_VALID_TREETOPOLOGY.
     */
    public static final String NETWORK_DOES_NOT_DESCRIBE_A_VALID_TREETOPOLOGY = "network does not describe a valid " + "Treetopology";
    /**
     * The constant SUFFIX_REGEX.
     */
    public static final String SUFFIX_REGEX = "\\)";
    public static final String NETWORK_STRING_IS_NULL = "network string is null.";
    public static final String NETWORK_STRING_IS_EMPTY = "network string is empty";
    // TODO: 21.02.22 was after colon
    //
    // TODO: 18.02.22 do
    // we
    // need this
    private TreeTopology<IP> graph;
    // ip2 part?
    // TODO: 28.02.22 under all circumstances: Read instructions again for your assignments! There are some hints
    //  what makes you fail the course!
    // TODO: 28.02.22 remove personal info unrelated to U-kürzel! (does that include git commits?)
    // TODO: 28.02.22 ask forum: does linking wikipedia violate anything?
    // TODO: 28.02.22 Jede Fehlermeldung muss aber mit Error ,
    //beginnen und darf keine Sonderzeichen, wie beispielsweise Zeilenumbrüche oder Umlaute,
    //enthalten.
    // TODO: 28.02.22 make error messages to enum?
    // TODO: 28.02.22 Ask if we can use the code from earlier years?
    // TODO: 28.02.22 let Johannes look over my code


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
            throw new RuntimeException("Given ... does not describe a valid Tree Topology");  // TODO: 19.02.22 pretty
        }
        if (children.size() != children.stream().distinct().count()) {
            throw new RuntimeException("There are duplicate children");
            // TODO: 21.02.22 make sure this is ok to throw? Maybe this was not explicitly mentioned?
            // TODO: 21.02.22 Punkt am Ende vom Satz?
        }
        this.graph = new Forest<>();
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
        TreeTopology<IP> graph = new Forest<>();
        this.graph = parseNetwork(bracketNotation, graph);
        // TODO: 06.03.22 use matcher-find for matching?
        // TODO: 05.03.22 are we supposed to include assertions in code?
        if (!(this.graph == null) && !this.graph.isForest()) { //connectedness is given through parsing;
            throw new ParseException(String.format(NETWORK_DOES_NOT_DESCRIBE_A_VALID_TREETOPOLOGY));
        }
    }

    /**
     * Parse network tree node.
     *
     * @param bracketNotation the bracket notation
     * @param graph           the graph
     * @return the tree node
     * @throws ParseException the parse exception
     */
    public static TreeTopology<IP> parseNetwork(final String bracketNotation, final TreeTopology<IP> graph) throws ParseException {
        // TODO: 05.03.22 document sideeffects
        if (bracketNotation == null) {
            throw new ParseException(NETWORK_STRING_IS_NULL);
        }
        if (bracketNotation.equals("")) {
            throw new ParseException(NETWORK_STRING_IS_EMPTY);
        }
        if (!bracketNotation.endsWith(SUFFIX)) { // we check this here already so that in the recursive call it is
            // guaranteed that the string ends in the suffix, thus we don't have to check in the recursive call for
            // the suffix and for the end of the string separately.
            throw new ParseException(Errors.S_NOT_NETWORK);
        }
        if (!parseNetwork(bracketNotation, null, graph).equals("")) {
            throw new ParseException(THERE_IS_A_BRACKET_MISMATCH_IN_THE_PROVIDED_STRING);
        }
        return graph;
    }

    /**
     * Parse network string.
     *
     * @param bracketNotation the bracket notation
     * @param parent          the parent
     * @param graph           the graph
     * @return the string
     * @throws ParseException the parse exception
     */
    public static String parseNetwork(final String bracketNotation, IP parent, TreeTopology<IP> graph) throws ParseException {
        if (bracketNotation.startsWith(PREFIX)) {
            String ipString = bracketNotation.substring(PREFIX.length());
            //remove prefix
            // TODO: 05.03.22 use normal function to remove string
            // TODO: 05.03.22 check that my string is never empty
            IP root = null;
            String node;
            while (true) {
                if (!ipString.contains(SEPARATION_STRING)) {
                    return parseLast(graph, ipString, root);
                }
                String[] separatedString = ipString.split(SEPARATION_STRING, SEP_NUMBER);
                node = separatedString[0];
                ipString = separatedString[1];
                if (root == null) {
                    root = addChild(graph, parent, node);
                } else if (node.startsWith(PREFIX)) {
                    ipString = parseNetwork(node + SEPARATION_STRING + ipString, root, graph);
                } else if (node.endsWith(SUFFIX)) {
                    node = parseLast(graph, node, root);
                    if (node.equals("")) {
                        return ipString;
                    }
                    return node + SEPARATION_STRING + ipString;

                } else {
                    addChild(graph, root, node);
                }
            }
        }
        throw new ParseException(THERE_IS_A_BRACKET_MISMATCH_IN_THE_PROVIDED_STRING);
    }

    private static String parseLast(final TreeTopology<IP> graph, final String ipString, final IP root) throws ParseException {
        String node;
        String[] separatedSuffix = ipString.split(SUFFIX_REGEX, SEP_NUMBER);
        node = separatedSuffix[0];
        String newString = separatedSuffix[1];
        if (!node.equals("")) {
            addChild(graph, root, node);
        }
        return newString;
    }

    private static IP addChild(final TreeTopology<IP> graph, final IP root, final String node) throws ParseException {
        IP child;
        try {
            child = new IP(node);
        } catch (ParseException e) {
            throw new ParseException(e.getMessage());// TODO: 05.03.22 correct this exception
        }

        if (!graph.add(child, root)) {
            throw new ParseException("Duplicate IP");
        }
        return child;
    }


    /**
     * Add boolean.
     *
     * @param subnet the subnet
     * @return the boolean
     */
    public boolean add(final Network subnet) {
        // TODO: 18.02.22 Is this new node only allowed to add nodes, or is the root replacing the old one?
        // TODO: MUST both have an overlapping subnet? (must the resulting Tree have a strictly greater list than the
        // TODO: 20.02.22 was ist wennn es keinen Overlap gibt?
        // other?
        if (subnet == null) {
            return false;
        }
        TreeTopology<IP> newGraph = this.graph.copy();
        // TODO: 21.02.22 figure out why on earth this assignment works? Isn't graph private?
        List<List<IP>> newEdges = subnet.graph.getEdges();
        // TODO: 20.02.22 addStuff
        // TODO: 20.02.22 handle adding loop
        for (List<IP> edge : newEdges) {
            newGraph.add(edge.get(0), edge.get(1));
        }
        if (!newGraph.isForest() || this.graph.equals(newGraph)) {
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
        if (ip1 == null || ip2 == null || ip1.equals(ip2)) {
            return false;
        }
        return this.graph.add(ip1, ip2);
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
        if (!this.graph.contains(root)) {
            return "";
        }
        TreeTopology<IP> newGraph = this.graph.copy();
        return toStringRecursive(root, newGraph);
    }

    private String toStringRecursive(IP root, TreeTopology<IP> newGraph) {
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
