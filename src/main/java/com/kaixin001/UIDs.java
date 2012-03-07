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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Arrays;

/**
 * A data class representing array of numeric UIDs.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @editor polaris
 */
public class UIDs extends KaixinResponse {
    private long[] uids;
    private long prev;
    private long next;
    private static final long serialVersionUID = -6585026560164704953L;
    private static String[] ROOT_NODE_NAMES = {"id_list", "ids"};

    UIDs(Response res) throws KaixinException {
        super(res);
        Element elem = res.asDocument().getDocumentElement();
        ensureRootNodeNameIs(ROOT_NODE_NAMES, elem);
        NodeList idlist = elem.getElementsByTagName("id");
        uids = new long[idlist.getLength()];
        for (int i = 0; i < idlist.getLength(); i++) {
            try {
                uids[i] = Long.parseLong(idlist.item(i).getFirstChild().getNodeValue());
            } catch (NumberFormatException nfe) {
                throw new KaixinException("KaiXin API returned malformed response: " + elem, nfe);
            }
        }
        prev = getChildLong("prev", elem);
        next = getChildLong("next", elem);
    }

    UIDs(Response res, Kaixin w) throws KaixinException {
        super(res);
        if ("[]\n".equals(res.asString())) {
            prev = 0;
            next = 0;
            uids = new long[0];
            return;
        }
        JSONObject json = res.asJSONObject();
        try {
            prev = json.getLong("prev");
            next = json.getLong("next");

            if (!json.isNull("uids")) {
                JSONArray jsona = json.getJSONArray("uids");
                int size = jsona.length();
                uids = new long[size];
                for (int i = 0; i < size; i++) {
                    uids[i] = jsona.getLong(i);
                }
            }

        } catch (JSONException jsone) {
            throw new KaixinException(jsone);
        }

    }

    public long[] getIDs() {
        return uids;
    }

    /**
     * @since Kx4J 1.0
     */
    public boolean hasPrevious() {
        return 0 != prev;
    }

    /**
     * @since Kx4j 1.0
     */
    public long getPreviousCursor() {
        return prev;
    }

    /**
     * @since Kx4j 1.0
     */
    public boolean hasNext() {
        return 0 != next;
    }

    /**
     * @since Kx4j 1.0
     */
    public long getNextCursor() {
        return next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UIDs)) return false;

        UIDs iDs = (UIDs) o;

        if (!Arrays.equals(uids, iDs.uids)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uids != null ? Arrays.hashCode(uids) : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        int len = uids.length;
        for (int i = 0; i < len; i++) {
            sb.append(uids[i]);
            if (i < len - 1) {
                sb.append(",");
            }
        }
        return "IDs{" +
                "uids=[" + sb.toString() +
                "], prev=" + prev +
                ", next=" + next +
                '}';
    }
}