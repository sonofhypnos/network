package model;

import edu.kit.informatik.IP;
import edu.kit.informatik.Network;
import edu.kit.informatik.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Main {
    /*
     * @project FinalAssignmentNoBloat
     * @author Tassilo Neubauer
     */
    public static void main(String[] args) {
        //Network net = new Network("(90.240.18.65 (97.22.140.27 (193.77.65.203 206.41.6.234 (137.57.11.178 53.79.153" +
//                ".118 151.175.20" + ".133 72" + ".204.103.14) (172.217.134.246 125.151.42.40 26.135.185.104 12.104.224.21 97.32.83.116)) 27.191.109.156 " + "(207.93.69.7 221.203.203.33 (211.36.119.36 191.214.220.219 7.33.138.146) (126.171.183.35 0.166.201" + ".82 166.114.94.115)) 25.28.90.184) 124.214.225.52 62.116.50.162 (118.255.66.35 228.203.204.177" + " " + "(71.130.3.224 (131.230.153.36 39.231.53.70 1.77.201.101 13.163.16.235) 166.101.129.76)))");
//        System.out.println(net.toString(new IP("90.240.18.65")));


        //Network net2 = new Network("(1.1.1.1 127");
        try {
            IP ip = new IP("127.0.0.01");
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
