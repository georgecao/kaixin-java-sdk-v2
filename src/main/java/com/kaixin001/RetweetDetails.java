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
package com.kaixin001;

import com.kaixin001.http.Response;
import com.kaixin001.org.json.JSONArray;
import com.kaixin001.org.json.JSONException;
import com.kaixin001.org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A data class representing one single retweet details.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Kx4J 1.0
 */
@Deprecated
public class RetweetDetails extends KaixinResponse implements
        java.io.Serializable {
    private long retweetId;
    private Date retweetedAt;
    private User retweetingUser;
    static final long serialVersionUID = 1957982268696560598L;
    
    /*package*/RetweetDetails(Response res, Kaixin kaixin) throws KaixinException {
        super(res);
        Element elem = res.asDocument().getDocumentElement();
        init(res, elem, kaixin);
    }
    
    RetweetDetails(JSONObject json) throws KaixinException {
        super();
        init(json);
    }
    
    

    private void init(JSONObject json) throws KaixinException {
    	try {
    		retweetId = json.getInt("retweetId");
    		retweetedAt = parseDate(json.getString("retweetedAt"), "EEE MMM dd HH:mm:ss z yyyy");
    		retweetingUser=new User(json.getJSONObject("retweetingUser"));
            
        } catch (JSONException jsone) {
            throw new KaixinException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
	}

	/*package*/RetweetDetails(Response res, Element elem, Kaixin kaixin) throws
            KaixinException {
        super(res);
        init(res, elem, kaixin);
    }

    private void init(Response res, Element elem, Kaixin kaixin) throws
            KaixinException {
        ensureRootNodeNameIs("retweet_details", elem);
        retweetId = getChildLong("retweet_id", elem);
        retweetedAt = getChildDate("retweeted_at", elem);
        retweetingUser = new User(res, (Element) elem.getElementsByTagName("retweeting_user").item(0)
                , kaixin);
    }

    public long getRetweetId() {
        return retweetId;
    }

    public Date getRetweetedAt() {
        return retweetedAt;
    }

    public User getRetweetingUser() {
        return retweetingUser;
    }
    /*modify by sycheng add json*/
    /*package*/
    static List<RetweetDetails> createRetweetDetails(Response res) throws KaixinException {
    	try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            List<RetweetDetails> retweets = new ArrayList<RetweetDetails>(size);
            for (int i = 0; i < size; i++) {
            	retweets.add(new RetweetDetails(list.getJSONObject(i)));
            }
            return retweets;
        } catch (JSONException jsone) {
            throw new KaixinException(jsone);
        } catch (KaixinException te) {
            throw te;
        }  
    }
    
    /*package*/
    static List<RetweetDetails> createRetweetDetails(Response res,
                                          Kaixin kaixin) throws KaixinException {
        Document doc = res.asDocument();
        if (isRootNodeNilClasses(doc)) {
            return new ArrayList<RetweetDetails>(0);
        } else {
            try {
                ensureRootNodeNameIs("retweets", doc);
                NodeList list = doc.getDocumentElement().getElementsByTagName(
                        "retweet_details");
                int size = list.getLength();
                List<RetweetDetails> statuses = new ArrayList<RetweetDetails>(size);
                for (int i = 0; i < size; i++) {
                    Element status = (Element) list.item(i);
                    statuses.add(new RetweetDetails(res, status, kaixin));
                }
                return statuses;
            } catch (KaixinException te) {
                ensureRootNodeNameIs("nil-classes", doc);
                return new ArrayList<RetweetDetails>(0);
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RetweetDetails)) return false;

        RetweetDetails that = (RetweetDetails) o;

        return retweetId == that.retweetId;
    }

    @Override
    public int hashCode() {
        int result = (int) (retweetId ^ (retweetId >>> 32));
        result = 31 * result + retweetedAt.hashCode();
        result = 31 * result + retweetingUser.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RetweetDetails{" +
                "retweetId=" + retweetId +
                ", retweetedAt=" + retweetedAt +
                ", retweetingUser=" + retweetingUser +
                '}';
    }
}
