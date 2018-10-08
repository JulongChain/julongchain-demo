package demonstration.model;

/**
 * @author lishaojie
 * @Date: 2018/8/10
 * @company Dingxuan
 */
public class Point {
    private String nodeHost = "192.168.1.171";
    private int nodePort = 7051;

    private String consenterHost = "192.168.1.175";
    private int consenterPort = 7050;

    public String getNodeHost() {
        return nodeHost;
    }

    public void setNodeHost(String nodeHost) {
        this.nodeHost = nodeHost;
    }

    public int getNodePort() {
        return nodePort;
    }

    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }

    public String getConsenterHost() {
        return consenterHost;
    }

    public void setConsenterHost(String consenterHost) {
        this.consenterHost = consenterHost;
    }

    public int getConsenterPort() {
        return consenterPort;
    }

    public void setConsenterPort(int consenterPort) {
        this.consenterPort = consenterPort;
    }
}
