package stuff;


import org.junit.jupiter.api.Assertions;

import java.util.List;


public class MoreTests {
    /*
     * @project FinalAssignment2022_1
     * @author Tassilo Neubauer
     */
    private static final String SMALL_NET_SORTED
            = "(85.193.148.81 34.49.145.239 (141.255.1.133 0.146.197.108 122.117.67.158) "
            + "(231.189.0.127 39.20.222.120 77.135.84.171 116.132.83.77 252.29.23.0))";


    public static void main(String[] args) throws ParseException {
        // Construct initial network
        //testEquals

        Network smallNet = new Network(SMALL_NET_SORTED);
        Assertions.assertEquals(SMALL_NET_SORTED, smallNet.toString(new IP("85.193.148.81")));
        IP root = new IP("141.255.1.133");
        IP secondRoot = new IP("141.255.1.133");
        Assertions.assertEquals(root, secondRoot);
        List<List<IP>> levels = List.of(List.of(root),
                List.of(new IP("0.146.197.108"), new IP("122.117.67.158")));
        final Network stringNetwork = new Network("(141.255.1.133 0.146.197.108 122.117.67.158)");
        Assertions.assertEquals("(141.255.1.133 0.146.197.108 122.117.67.158)", stringNetwork.toString(root));
        final Network network = new Network(root, levels.get(1));
        // (141.255.1.133 0.146.197.108 122.117.67.158)
        Assertions.assertEquals("(141.255.1.133 0.146.197.108 122.117.67.158)", network.toString(root));
        Assertions.assertEquals("141.255.1.133", root.toString());
        Assertions.assertEquals(3, network.list().size());
        // true
        Assertions.assertEquals((levels.size() - 1), network.getHeight(root));
        // true
        Assertions.assertEquals(List.of(List.of(root), levels.get(1)), network.getLevels(root));
        // "Change" root and call toString, getHeight and getLevels again
        root = new IP("122.117.67.158");
        levels = List.of(List.of(root), List.of(new IP("141.255.1.133")),
                List.of(new IP("0.146.197.108")));
        // true
        Assertions.assertEquals("(122.117.67.158 (141.255.1.133 0.146.197.108))", network.toString(root));
        // true
        Assertions.assertEquals((levels.size() - 1), network.getHeight(root));
        // true
        Assertions.assertEquals(levels, network.getLevels(root));
        // Try to add circular dependency
        // false
        Assertions.assertFalse(network.add(new Network("(122.117.67.158 0.146.197.108)")));
        // Merge two subnets with initial network
        // true
        Assertions.assertTrue(network.add(new Network(
                "(85.193.148.81 34.49.145.239 231.189.0.127 141.255.1.133)")));
        // true
        Assertions.assertTrue(network.add(new Network("(231.189.0.127 252.29.23.0"
                + " 116.132.83.77 39.20.222.120 77.135.84.171)")));
        // "Change" root and call toString, getHeight and getLevels again
        root = new IP("85.193.148.81");
        levels = List.of(List.of(root),
                List.of(new IP("34.49.145.239"), new IP("141.255.1.133"),
                        new IP("231.189.0.127")),
                List.of(new IP("0.146.197.108"), new IP("39.20.222.120"),
                        new IP("77.135.84.171"), new IP("116.132.83.77"),
                        new IP("122.117.67.158"), new IP("252.29.23.0")));
        // true
        Assertions.assertEquals(("(85.193.148.81 34.49.145.239 (141.255.1.133 0.146.197.108 122.117.67.158) (231.189.0.127 39.20.222.120"
                + " 77.135.84.171 116.132.83.77 252.29.23.0))"), network.toString(root));
        // true
        Assertions.assertEquals((levels.size() - 1), network.getHeight(root));
        // true
        Assertions.assertEquals(levels, network.getLevels(root));
        // true
        Assertions.assertEquals(List.of(new IP("141.255.1.133"), new IP("85.193.148.81"),
                        new IP("231.189.0.127")), network.getRoute(new IP("141.255.1.133"),
                new IP("231.189.0.127")));
        // "Change" root and call getHeight again
        root = new IP("34.49.145.239");
        levels = List.of(List.of(root), List.of(new IP("85.193.148.81")),
                List.of(new IP("141.255.1.133"), new IP("231.189.0.127")),
                List.of(new IP("0.146.197.108"), new IP("39.20.222.120"),
                        new IP("77.135.84.171"), new IP("116.132.83.77"),
                        new IP("122.117.67.158"), new IP("252.29.23.0")));
        // true
        Assertions.assertEquals((levels.size() - 1), network.getHeight(root));
        // Remove edge and list tree afterwards
        // true
        Assertions.assertTrue(network.disconnect(new IP("85.193.148.81"),
                new IP("34.49.145.239")));
        // true
        Assertions.assertEquals(List.of(new IP("0.146.197.108"), new IP("39.20.222.120"),
                new IP("77.135.84.171"), new IP("85.193.148.81"),
                new IP("116.132.83.77"), new IP("122.117.67.158"),
                new IP("141.255.1.133"), new IP("231.189.0.127"),
                new IP("252.29.23.0")), network.list());
    }
}