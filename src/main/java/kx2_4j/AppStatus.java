package kx2_4j;

import kx2_4j.http.Response;
import kx2_4j.org.json.JSONArray;
import kx2_4j.org.json.JSONException;
import kx2_4j.org.json.JSONObject;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author polaris
 */
public class AppStatus  extends KxResponse implements java.io.Serializable {
    
    private long uid;
    
    private int install;
    
    public AppStatus(Response res, KxSDK kxSDK) throws KxException {
        super(res);
        Element elem = res.asDocument().getDocumentElement();
        init(res, elem, kxSDK);
    }

    public AppStatus(Response res, Element elem, KxSDK kxSDK) throws
            KxException {
        super(res);
        init(res, elem, kxSDK);
    }

    public AppStatus(Response res)throws KxException{
    	super(res);
    	JSONObject json = res.asJSONObject();
    	constructJson(json);
    }
    
    public AppStatus(JSONObject json) throws KxException {
        super();
        init(json);
    }
    
    private void init(JSONObject json) throws KxException {
        if (json != null) {
            try {
                uid = json.getLong("uid");
                install = json.getInt("install");
            } catch (JSONException jsone) {
                throw new KxException(jsone.getMessage() + ":" + json.toString(), jsone);
            }
        }
    }
    
    private void init(Response res, Element elem, KxSDK kxSDK) throws KxException {
        ensureRootNodeNameIs("status", elem);
        
        uid = getChildLong("uid", elem);
        install = getChildInt("install", elem);
    }
    
    private void constructJson(JSONObject json) throws KxException {
        try {
                uid = json.getLong("uid");
                install = json.getInt("install");
        } catch (JSONException je) {
                throw new KxException(je.getMessage() + ":" + json.toString(), je);
        }
    }
    
    public static List<AppStatus> constructStatus(Response res) throws KxException {

        JSONObject json = res.asJSONObject();
        try {
            //			int next_cursor = json.getInt("next_cursor");
            //			int previous_cursor = json.getInt("previous_cursor");



            JSONArray list = json.getJSONArray("statuses");
            int size = list.length();
            List<AppStatus> appStatuses = new ArrayList<AppStatus>(size);
            for (int i = 0; i < size; i++) {
                appStatuses.add(new AppStatus(list.getJSONObject(i)));
            }
            return appStatuses;


        } catch (JSONException je) {
            throw new KxException(je);
        }

    }
    
    public int getInstall() {
        return install;
    }

    public long getUid() {
        return uid;
    }
    
    public String toString() {
        return "AppStatus{"
                + "uid='" + uid + '\''
                + ", install='" + install + '\''
                + '}';
    }
    
}
