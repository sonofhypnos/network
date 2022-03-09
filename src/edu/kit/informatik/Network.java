package edu.kit.informatik;

import edu.kit.informatik.resources.ForestException;
import edu.kit.informatik.resources.NetworkException;
import edu.kit.informatik.resources.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.kit.informatik.IP.REGEX_IP;

/**
 * The Network class. Implements all functions from the assignment. Most graph functionality is implemented in the
 * Forest class while this class handles parsing, printing and throwing Exceptions.
 *
 * @author :upkim
 * @version : 1.0 2022-03-08 11:52
 */
public class Network {
    private static final String SEPARATION_STRING = " ";
    private static final String PREFIX = "(";
    private static final String SUFFIX = ")";
    private static final int SEP_NUMBER = 2; //2 instead of 1 for reasonable behaviour by the split method
    private static final String EMPTY_STRING = "";
    private static final String SUFFIX_REGEX = "\\)";

    //Error Messages
    private static final String ERROR_INVALID_IPS = "Error, network contains invalid ips";
    private static final String ERROR_BRACKET = "Error, there is a parenthesis mismatch in the provided string.";
    private static final String ERROR_NETWORK_STRING_IS_NULL = "Error, network string is null.";
    private static final String ERROR_NETWORK_STRING_IS_EMPTY = "Error, network string is empty.";
    private static final String ERROR_DUPLICATE_I_PS = "Error, duplicate entries in provided String.";
    private static final String ERROR_THERE_ARE_DUPLICATE_CHILDREN = "Error, there are duplicate children.";
    private static final String ERROR_NOT_A_VALID_FOREST = "Error, network does not describe a valid forest.";
    private static final String ERROR_MINIMUM_OF_1_CONNECTION = "Error, The network must have a minimum of one "
            + "connection";

    private Forest<IP> graph;

    /**
     * Instantiates a new Network.
     *
     * @param root     the root
     * @param children the children
     */
    public Network(final IP root, final List<IP> children) {
        if (root == null || children == null || children.contains(root)) {
            throw new NetworkException(ERROR_NOT_A_VALID_FOREST);
        }
        if (children.size() != children.stream().distinct().count()) {
            throw new NetworkException(ERROR_THERE_ARE_DUPLICATE_CHILDREN);
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
        Forest<IP> graph = new Forest<>();
        this.graph = parseNetwork(bracketNotation, graph);
    }

    /**
     * Parse network tree node.
     *
     * @param bracketNotation the bracket notation
     * @param graph           adds Edges from bracket-notation to the graph
     * @return the tree node
     * @throws ParseException if bracketNotation does not describe a valid Forest
     */
    private static Forest<IP> parseNetwork(final String bracketNotation, final Forest<IP> graph) throws ParseException {
        if (bracketNotation == null) {
            throw new ParseException(ERROR_NETWORK_STRING_IS_NULL);
        }
        if (bracketNotation.equals("")) {
            throw new ParseException(ERROR_NETWORK_STRING_IS_EMPTY);
        }
        if (!bracketNotation.endsWith(SUFFIX)) {
            throw new ParseException(ERROR_NOT_A_VALID_FOREST);
        }

        List<String> ips = new ArrayList<>();
        Matcher ipMatcher = Pattern.compile(REGEX_IP).matcher(bracketNotation);
        while (ipMatcher.find()) {
            ips.add(ipMatcher.group());
        }
        if (ips.stream().distinct().count() != ips.size()) {
            throw new ParseException(ERROR_DUPLICATE_I_PS);
        }
        if (ips.size() < Forest.MIN_NODES) {
            throw new ParseException(ERROR_MINIMUM_OF_1_CONNECTION);
        }


        if (!parseNetworkRecursive(bracketNotation, null, graph).equals("")) {
            throw new ParseException(ERROR_BRACKET);
        }
        return graph;
    }

    /**
     * Parse network string. Modifies the input-graph, to simplify the recursive calls.
     * <p>
     * Separates entries by separation string. Sets first entry as root and adds connections between children and
     * root afterwards, calls itself recursively if entry starts with "prefix" and returns when finding "suffix",
     * so that the remaining string can be handled by calling function.
     *
     * @param bracketNotation Remaining Input to be parsed
     * @param parent          the parent
     * @param graph           adds edges with parent and its children to graph
     * @return the string
     * @throws ParseException the parse exception
     */
    private static String parseNetworkRecursive(final String bracketNotation, final IP parent, Forest<IP> graph)
            throws ParseException {
        if (!bracketNotation.startsWith(PREFIX)) {

            throw new ParseException(ERROR_BRACKET);
        }

        String ipString = bracketNotation.substring(PREFIX.length()); //remove prefix
        IP root = null;

        while (ipString.contains(SEPARATION_STRING)) {
            String[] separatedString = ipString.split(SEPARATION_STRING, SEP_NUMBER);
            String node = separatedString[0];
            ipString = separatedString[1];

            if (root == null)
                root = addChild(graph, parent, node);

            else if (node.startsWith(PREFIX))
                ipString = parseNetworkRecursive(node + SEPARATION_STRING + ipString, root, graph);

            else if (node.endsWith(SUFFIX)) {
                String remainingParentheses = parseLast(graph, node, root);
                if (remainingParentheses.equals(EMPTY_STRING)) return ipString;
                return remainingParentheses + SEPARATION_STRING + ipString;

            } else addChild(graph, root, node);
        }
        return parseLast(graph, ipString, root);
    }

    private static String parseLast(final Forest<IP> graph, final String ipString, final IP root)
            throws ParseException {
        String node;
        String[] separatedSuffix = ipString.split(SUFFIX_REGEX, SEP_NUMBER);
        node = separatedSuffix[0];
        String newString = separatedSuffix[1];
        if (!node.equals(EMPTY_STRING)) {
            addChild(graph, root, node);
        }
        return newString;
    }

    private static IP addChild(final Forest<IP> graph, final IP root, final String node) throws ParseException {
        IP child;
        try {
            child = new IP(node);
        } catch (ParseException e) {
            throw new ParseException(ERROR_INVALID_IPS);
        }

        try {
            graph.add(child, root);
        } catch (ForestException e) {
            throw new ParseException(ERROR_NOT_A_VALID_FOREST);
        }
        return child;
    }


    /**
     * Add subnet to Network. Return true if successful.
     *
     * @param subnet the subnet
     * @return the boolean
     */
    public boolean add(final Network subnet) {
        if (subnet == null) {
            return false;
        }
        Forest<IP> newGraph = this.graph.copy();
        List<List<IP>> newEdges = subnet.graph.getEdges();
        for (List<IP> edge : newEdges) {
            try {
                newGraph.add(edge.get(0), edge.get(1));
            } catch (ForestException e) {
                return false;
            }
        }
        if (this.graph.equals(newGraph)) {
            return false;
        }
        this.graph = newGraph;
        return true;
    }

    /**
     * List all containing IPs
     *
     * @return the list
     */
    public List<IP> list() {
        return this.graph.list();
    }

    /**
     * Add connection between two ips. Returns true on success, false otherwise.
     *
     * @param ip1 the ip 1
     * @param ip2 the ip 2
     * @return the boolean
     */
    public boolean connect(final IP ip1, final IP ip2) {
        if (ip1 == null || ip2 == null || ip1.equals(ip2) || !(this.graph.contains(ip1) && this.graph.contains(ip2))) {
            return false;
        }
        try {
            return this.graph.add(ip1, ip2);
        } catch (ForestException e) {
            return false;
        }
    }

    /**
     * Disconnect two ips. Returns on success false otherwise.
     *
     * @param ip1 the ip 1
     * @param ip2 the ip 2
     * @return the boolean
     */
    public boolean disconnect(final IP ip1, final IP ip2) {
        if (ip1 == null || ip2 == null || ip1.equals(ip2)) {
            return false;
        }
        return graph.disconnect(ip1, ip2);
    }

    /**
     * returns true if the network contains the ip.
     *
     * @param ip the ip
     * @return the boolean
     */
    public boolean contains(final IP ip) {
        if (ip == null) {
            return false;
        }
        return this.graph.contains(ip);
    }

    /**
     * Get height of the tree when viewed from supplied root tree.
     *
     * @param root the root
     * @return the height
     */
    public int getHeight(final IP root) {
        if (root == null) {
            return 0;
        }
        if (!list().contains(root)) {
            return 0;
        }
        return this.graph.getHeight(root);
    }


    /**
     * Get levels of the tree specified by D
     *
     * @param root the root
     * @return the list
     */
    public List<List<IP>> getLevels(final IP root) {
        if (root == null) {
            return new ArrayList<>();
        }
        return graph.copy().getLevels(root);
    }

    /**
     * Gets the Route from start to end. Returns empty list if no route is found or start == end.
     *
     * @param start the start
     * @param end   the end
     * @return the route
     */
    public List<IP> getRoute(final IP start, final IP end) {
        if (start == null || end == null) {
            return new ArrayList<>();
        }
        return graph.getRoute(start, end);
    }


    /**
     * To string string.
     *
     * @param root the root
     * @return the string
     */
    public String toString(final IP root) {
        if (!this.graph.contains(root)) {
            return EMPTY_STRING;
        }
        Forest<IP> newGraph = this.graph.copy();
        return toStringRecursive(root, newGraph);
    }

    private String toStringRecursive(final IP root, final Forest<IP> newGraph) {
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
        return PREFIX + String.join(SEPARATION_STRING, ipStrings) + SUFFIX;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Network network = (Network) o;

        return this.graph.equals(network.graph);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph);
    }


}
