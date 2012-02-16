/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package kx2_4j;

import kx2_4j.http.Response;
import kx2_4j.org.json.JSONArray;
import kx2_4j.org.json.JSONException;
import kx2_4j.org.json.JSONObject;
import org.w3c.dom.Element;

import java.util.Arrays;

/**
 * A data class representing array of numeric IDs.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @editor polaris
 */
public class InvitedUIDs extends KxResponse {
    private AppStatus[] statuses;
    private long prev;
    private long next;
    private static final long serialVersionUID = -6585026560164704953L;
    private static String[] ROOT_NODE_NAMES = {"statuses_list", "statuses"};

    /*package*/ InvitedUIDs(Response res) throws KxException {
        super(res);
        Element elem = res.asDocument().getDocumentElement();
        
        prev = getChildLong("prev", elem);
        next = getChildLong("next", elem);
    }

    /*package*/ InvitedUIDs(Response res,KxSDK w) throws KxException {
        super(res);
        if("[]\n".equals(res.asString())){
        	prev=0;
        	next=0;
        	statuses= new AppStatus[0];
        	return;
        }
        JSONObject json=  res.asJSONObject();
        try {
            prev = json.getLong("prev");
            next = json.getLong("next");
        
            if(!json.isNull("statuses")){
                    JSONArray jsona = json.getJSONArray("statuses");
                    int size = jsona.length();
                    statuses = new AppStatus[ size];
                    for (int i = 0; i < size; i++) {
                        statuses[i] = new AppStatus(jsona.getJSONObject(i));
                    }
            }
        	
         } catch (JSONException jsone) {
             throw new KxException(jsone);
         } 
        
    }

    public AppStatus[] getAppStatuses() {
        return statuses;
    }

    /**
     *
     * @since Kx4J 1.2.0
     */
    public boolean hasPrevious(){
        return 0 != prev;
    }

    /**
     *
     * @since Kx4J 1.2.0
     */
    public long getPreviousCursor() {
        return prev;
    }

    /**
     *
     * @since Kx4J 1.2.0
     */
    public boolean hasNext(){
        return next >= 0;
    }

    /**
     *
     * @since Kx4J 1.2.0
     */
    public long getNextCursor() {
        return next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvitedUIDs)) return false;

        InvitedUIDs iDs = (InvitedUIDs) o;

        if (!Arrays.equals(statuses, iDs.statuses)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return statuses != null ? Arrays.hashCode(statuses) : 0;
    }

    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder("");
        
        int len = statuses.length;
        
        for(int i = 0; i < len; i++) {
            sb.append(statuses[i].toString());
            
            if(i < len - 1) {
                sb.append(",");
            }
        }
        return "InvitedUIDs{" +
                "statuses=[" + sb.toString() +
                "], prev=" + prev +
                ", next=" + next +
                '}';
    }
}