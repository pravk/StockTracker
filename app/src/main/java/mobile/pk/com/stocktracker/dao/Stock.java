package mobile.pk.com.stocktracker.dao;

import com.orm.SugarRecord;

/**
 * Created by hello on 8/10/2015.
 */
public class Stock extends SugarRecord<Stock> {
    private String name;
    private String bseId;
    private String nseId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBseId() {
        return bseId;
    }

    public void setBseId(String bseId) {
        this.bseId = bseId;
    }

    public String getNseId() {
        return nseId;
    }

    public void setNseId(String nseId) {
        this.nseId = nseId;
    }
}
