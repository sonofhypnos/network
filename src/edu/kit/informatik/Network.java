package edu.kit.informatik;

import edu.kit.informatik.resources.ErrorMessages;
import edu.kit.informatik.resources.NetworkException;
import edu.kit.informatik.resources.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.kit.informatik.IP.REGEX_IP;

/**
 * The Network class. Implements all functions from the assignment. Most graph functionality is implemented in the
 * Forest class while this class handles parsing, toString and throwing Exceptions.
 *
 * @author upkim
 * @version 1.0 2022-03-12 22:13
 */
public class Network {
    /**
     * Minimum nodes every tree in the network needs to have.
     */
    static final int MIN_NODES = 2;
    private static final String SEPARATION_STRING = " ";
    private static final String PREFIX = "(";
    private static final String SUFFIX = ")";
    private static final int SPLIT_LIMIT = 2; //split applies pattern 'limit-1' times, so we use 2 to apply it once.
    private static final String EMPTY_STRING = "";
    private static final String SUFFIX_REGEX = "\\)";
    private Forest<IP> graph;


    /**
     * Instantiates a new Network with edges added between children and root.
     *
     * @param root     the root
     * @param children the children
     * @throws NetworkException if the input network does not describe a valid network.
     */
    public Network(final IP root, final List<IP> children) {
        if (root == null || children == null || children.contains(root)) {
            throw new NetworkException(ErrorMessages.ERROR_NOT_A_VALID_FOREST);
        }
        if (children.size() != children.stream().distinct().count()) {
            throw new NetworkException(ErrorMessages.ERROR_THERE_ARE_DUPLICATE_CHILDREN);
        }
        this.graph = new Forest<>();
        for (IP child : children) {
            graph.add(root, child);
        }
    }


    /**
     * Instantiates a new Network. Throws ParseException if the input does not follow the bracketFormat or does not
     * describe a valid Network.
     *
     * @param bracketNotation the bracket notation
     * @throws ParseException the parse exception
     */
    public Network(final String bracketNotation) throws ParseException {
        Forest<IP> graph = new Forest<>();
        this.graph = parseNetwork(bracketNotation, graph);
    }

    private static Forest<IP> parseNetwork(final String bracketNotation, final Forest<IP> graph) throws ParseException {
        if (bracketNotation == null) {
            throw new ParseException(ErrorMessages.ERROR_NETWORK_STRING_IS_NULL);
        }
        if (bracketNotation.isEmpty()) {
            throw new ParseException(ErrorMessages.ERROR_NETWORK_STRING_IS_EMPTY);
        }
        if (!bracketNotation.endsWith(SUFFIX)) {
            throw new ParseException(ErrorMessages.ERROR_NOT_A_VALID_FOREST);
        }

        List<String> ips = new ArrayList<>();
        Matcher ipMatcher = Pattern.compile(REGEX_IP).matcher(bracketNotation);
        while (ipMatcher.find()) {
            ips.add(ipMatcher.group());
        }
        if (ips.stream().distinct().count() != ips.size()) {
            throw new ParseException(ErrorMessages.ERROR_DUPLICATE_I_PS);
        }
        if (ips.size() < MIN_NODES) {
            throw new ParseException(ErrorMessages.ERROR_MINIMUM_OF_1_CONNECTION);
        }


        if (!parseNetworkRecursive(bracketNotation, null, graph).equals("")) {
            throw new ParseException(ErrorMessages.ERROR_BRACKET);
        }
        return graph;
    }

    /**
     * Parse network string. Modifies the input-graph, to simplify the recursive calls.
     * <p>
     * Separates entries by separation string. Sets first entry as root and adds connections between children and root
     * afterwards, calls itself recursively if entry starts with "prefix" and returns when finding "suffix", so that the
     * remaining string can be handled by calling function.
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

            throw new ParseException(ErrorMessages.ERROR_BRACKET);
        }

        String ipString = bracketNotation.substring(PREFIX.length()); //remove prefix
        IP root = null;

        while (ipString.contains(SEPARATION_STRING)) {
            String[] separatedString = ipString.split(SEPARATION_STRING, SPLIT_LIMIT);
            String node = separatedString[0];
            ipString = separatedString[1];

            if (root == null) root = addChild(graph, parent, node);

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
        String[] separatedSuffix = ipString.split(SUFFIX_REGEX, SPLIT_LIMIT);
        node = separatedSuffix[0];
        String newString = separatedSuffix[1];
        if (!node.equals(EMPTY_STRING)) {
            addChild(graph, root, node);
        }
        return newString;
    }

    private static IP addChild(final Forest<IP> graph, final IP parent, final String node) throws ParseException {
        IP child;
        try {
            child = new IP(node);
        } catch (ParseException e) {
            throw new ParseException(ErrorMessages.ERROR_INVALID_IPS);
        }

        //we catch the ForestException here in order to avoid code duplication of the add-method.
        if (parent == null) {
            return child;
        }
        if (!graph.add(parent, child)) {
            throw new ParseException(ErrorMessages.ERROR_NOT_A_VALID_FOREST);
        }
        return child;
    }


    /**
     * Add subnet to Network iff adding all edges to the network results in a new valid network.
     *
     * @param subnet the subnet
     * @return true iff successfully added subnetwork.
     */
    public boolean add(final Network subnet) {
        if (subnet == null) {
            return false;
        }
        Forest<IP> newGraph = this.graph.copy();
        Set<List<IP>> newEdges = subnet.graph.getAdjacencySet();
        for (List<IP> edge : newEdges) {
            final IP first = edge.get(0);
            final IP second = edge.get(1);
            final boolean addsLoop = !newGraph.areAdjacent(first, second) && newGraph.areConnected(first, second);
            //adding an edge between two connected nodes adds a second path between the two
            // ==> a loop <==> not a tree anymore.
            if (addsLoop) {
                return false;
            }
            newGraph.add(first, second);
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
     * Add connection between two ips. Returns true iff the two ips were not adjacent, existent in the network, and the
     * network is afterwards still a valid Network.
     *
     * @param ip1 the ip 1
     * @param ip2 the ip 2
     * @return the boolean
     */
    public boolean connect(final IP ip1, final IP ip2) {
        if (ip1 == null || ip2 == null || ip1.equals(ip2) || !(this.graph.contains(ip1) && this.graph.contains(ip2))) {
            return false;
        }
        return this.graph.add(ip1, ip2);
    }

    /**
     * Disconnect two ips. Returns true iff the two ips were previously in network adjacent and removes edge between
     * them, unless the Network would afterwards not describe a valid Forest.
     *
     * @param ip1 the ip 1
     * @param ip2 the ip 2
     * @return the boolean
     */
    public boolean disconnect(final IP ip1, final IP ip2) {
        if (ip1 == null || ip2 == null || ip1.equals(ip2)) {
            return false;
        }
        boolean isLastEdge = list().size() == MIN_NODES;
        if (isLastEdge) {
            return false;
        }
        return graph.remove(ip1, ip2);
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
     * Get levels of the tree specified by root.
     *
     * @param root the root
     * @return the list
     */
    public List<List<IP>> getLevels(final IP root) {
        if (root == null) {
            return new ArrayList<>();
        }
        return graph.getLevels(root);
    }

    /**
     * Gets the Route from start to end. Returns empty list if no route is found or start equals end.
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
     * Returns String format for Network in parenthesis-notation for the Tree with the variable root as the root of the
     * tree belonging to the Network.
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
        if (newGraph.getAdjacent(root).size() == 0) {
            return root.toString();
        }
        List<String> ipStrings = new ArrayList<>();
        ipStrings.add(root.toString());

        List<IP> children = new ArrayList<>(newGraph.getAdjacent(root));
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
