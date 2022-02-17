package stuff;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Network.
 */
public class Network {

    private TreeNode<IP> root;
    public static final String REGEX_NODE = IP.REGEX_IP + "|(" + IP.REGEX_IP + ")*";



    /**
     * Instantiates a new Network.
     *
     * @param root     the root
     * @param children the children
     */
    public Network(final IP root, final List<IP> children) {
        // TODO: 16.02.22 implement
    }

    public static TreeNode<IP> parseNetwork(final String bracketNotation) throws ParseException {
        return parseNetwork(bracketNotation, null);
    }

    public static TreeNode<IP> parseNetwork(final String bracketNotation, TreeNode<IP> parent) throws ParseException {
        // TODO: 17.02.22 check that at least one Node
        // TODO: 17.02.22 check how the rules handle whitespace in general
        // TODO: 17.02.22 is there an easy way to do substitution in java? that way we could substitute all the
        //  strings that are inside of parenthesis.
        if (bracketNotation.charAt(1) == '(' && bracketNotation.charAt(bracketNotation.length() - 1) == ')') {
            String ipString = bracketNotation.substring(1, bracketNotation.length() - 1);
            Pattern nodePattern = Pattern.compile(REGEX_NODE);
            Matcher nodeMatcher = nodePattern.matcher(ipString);
            List<String> nodeStrings = new ArrayList<>();
            while (nodeMatcher.find()){
                nodeStrings.add(nodeMatcher.group());
            }
            TreeNode<IP> root = new TreeNode<>(new IP(nodeStrings.remove(0)), parent);
            //would have just used map here if java wasn't so annoying with exceptions in lambdas
            for (String nodeString : nodeStrings) {
                parseNetwork(nodeString, parent);
            }
            return root;
        }
        return new TreeNode<>(new IP(bracketNotation), parent);
    }


    /**
     * Instantiates a new Network.
     *
     * @param bracketNotation the bracket notation
     * @throws ParseException the parse exception
     */
    public Network(final String bracketNotation) throws ParseException {
        // TODO: 16.02.22 implement
        root = parseNetwork(bracketNotation);

    }




    /**
     * Add boolean.
     *
     * @param subnet the subnet
     * @return the boolean
     */
    public boolean add(final Network subnet) {
        // TODO: 16.02.22 implement 
        return false;
    }

    /**
     * List list.
     *
     * @return the list
     */
    public List<IP> list() {
        // TODO: 16.02.22 implement 
        return null;
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
        //// TODO: 16.02.22 implement 
        return false;
    }

    /**
     * Gets height.
     *
     * @param root the root
     * @return the height
     */
    public int getHeight(final IP root) {
        // // TODO: 16.02.22 implement 
        return 0;
    }

    /**
     * Gets levels.
     *
     * @param root the root
     * @return the levels
     */
    public List<List<IP>> getLevels(final IP root) {
        // TODO: 16.02.22 implement 
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
        return null;
    }
    // TODO: 16.02.22 Implement


}
