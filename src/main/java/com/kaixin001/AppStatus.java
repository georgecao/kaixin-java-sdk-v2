package com.kaixin001;

import com.kaixin001.http.Response;
import com.kaixin001.org.json.JSONArray;
import com.kaixin001.org.json.JSONException;
import com.kaixin001.org.json.JSONObject;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author polaris
 */
public class AppStatus  extends KaixinResponse implements java.io.Serializable {
    
    private long uid;
    
    private int install;
    
    public AppStatus(Response res, Kaixin kaixin) throws KaixinException {
        super(res);
        Element elem = res.asDocument().getDocumentElement();
        init(res, elem, kaixin);
    }

    public AppStatus(Response res, Element elem, Kaixin kaixin) throws
            KaixinException {
        super(res);
        init(res, elem, kaixin);
    }

    public AppStatus(Response res)throws KaixinException {
    	super(res);
    	JSONObject json = res.asJSONObject();
    	constructJson(json);
    }
    
    public AppStatus(JSONObject json) throws KaixinException {
        super();
        init(json);
    }
    
    private void init(JSONObject json) throws KaixinException {
        if (json != null) {
            try {
                uid = json.getLong("uid");
                install = json.getInt("install");
            } catch (JSONException jsone) {
                throw new KaixinException(jsone.getMessage() + ":" + json.toString(), jsone);
            }
        }
    }
    
    private void init(Response res, Element elem, Kaixin kaixin) throws KaixinException {
        ensureRootNodeNameIs("status", elem);
        
        uid = getChildLong("uid", elem);
        install = getChildInt("install", elem);
    }
    
    private void constructJson(JSONObject json) throws KaixinException {
        try {
                uid = json.getLong("uid");
                install = json.getInt("install");
        } catch (JSONException je) {
                throw new KaixinException(je.getMessage() + ":" + json.toString(), je);
        }
    }
    
    public static List<AppStatus> constructStatus(Response res) throws KaixinException {

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
            throw new KaixinException(je);
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
