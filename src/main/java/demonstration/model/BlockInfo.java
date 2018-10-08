package demonstration.model;

import java.util.List;

/**
 * @author lishaojie
 * @Date: 2018/8/1
 * @company Dingxuan
 */
public class BlockInfo {

    private String dataHash;
    private String previousHash;
    private long numid;
    private String txid;

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public long getNumid() {
        return numid;
    }

    public void setNumid(long numid) {
        this.numid = numid;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }
}
