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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * A data class representing Basic user information element
 */
public class User extends KxResponse implements java.io.Serializable {

    static final String[] POSSIBLE_ROOT_NAMES = new String[]{"user", "sender", "recipient", "retweeting_user"};
    private KxSDK kxSDK;
    private long uid;
    private String name;
    private int gender;     // 性别, 1.男, 2.女, 0.未知
    private String hometown;
    private String city;
    private String logo120;
    private String logo50;
    private String birthday;
    private String bodyform;
    private String blood;
    private String marriage;
    private String trainwith;
    private String interest;
    private String favbook;
    private String favmovie;
    private String favtv;
    private String idol;
    private String motto;
    private String wishlist;
    private String intro;
    private List<Education> education = new ArrayList<Education>();
//    private String schooltype;
//    private String school;
//    private String strClass;        // 班级
//    private String year;
    private List<Career> career = new ArrayList<Career>();
//    private String company;
//    private String dept;
//    private String beginyear;
//    private String beginmonth;
//    private String endyear;
//    private String endmonth;
    private int isStar;         // 0,非明星；1：明星
    private static final long serialVersionUID = -6345893237975349030L;

    public User(Response res, KxSDK kxSDK) throws KxException {
        super(res);
        Element elem = res.asDocument().getDocumentElement();
        init(elem, kxSDK);
    }

    public User(Response res, Element elem, KxSDK kxSDK) throws KxException {
        super(res);
        init(elem, kxSDK);
    }

    public User(JSONObject json) throws KxException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws KxException {
        if (json != null) {
            try {
                uid = json.getLong("uid");
                name = json.getString("name");
                hometown = json.getString("hometown");
                city = json.getString("city");
                logo120 = json.getString("logo120");
                logo50 = json.getString("logo50");
                birthday = json.getString("birthday");
                bodyform = json.getString("bodyform");
                blood = json.getString("blood");
                marriage = json.getString("marriage");
                trainwith = json.getString("trainwith");
                interest = json.getString("interest");
                favbook = json.getString("favbook");
                favmovie = json.getString("favmovie");
                favtv = json.getString("favtv");
                idol = json.getString("idol");
                motto = json.getString("motto");
                wishlist = json.getString("wishlist");
                intro = json.getString("intro");
                
                if(json.getString("education").length() != 0) {
                    JSONArray educationJsonArr = json.getJSONArray("education");
                    int len = educationJsonArr.length();
                    for (int i = 0; i < len; i++) {
                        Education oneEdu = new Education(educationJsonArr.getJSONObject(i));
                        education.add(oneEdu);
                    }
                }
//            schooltype = json.getString("schooltype");
//            school = json.getString("school");
//            strClass = json.getString("strClass");
//            year = json.getString("year");
              if(json.getString("career").length() != 0) {
                    JSONArray careerJsonArr = json.getJSONArray("career");
                    int len = careerJsonArr.length();
                    for (int i = 0; i < len; i++) {
                        Career oneCareer = new Career(careerJsonArr.getJSONObject(i));
                        career.add(oneCareer);
                    }
              }
//                company = json.getString("company");
//                dept = json.getString("dept");
//                beginyear = json.getString("beginyear");
//                endyear = json.getString("endyear");
//                endmonth = json.getString("endmonth");
//                beginmonth = json.getString("beginmonth");
                
                isStar = json.getInt("isStar");
                gender = json.getInt("gender");
            } catch (JSONException jsone) {
                throw new KxException(jsone.getMessage() + ":" + json.toString(), jsone);
            }
        }
    }

    private void init(Element elem, KxSDK kxSDK) throws KxException {
        this.kxSDK = kxSDK;
        ensureRootNodeNameIs(POSSIBLE_ROOT_NAMES, elem);
        uid = getChildLong("uid", elem);
        name = getChildText("name", elem);
        hometown = getChildText("hometown", elem);
        city = getChildText("city", elem);
        logo120 = getChildText("logo120", elem);
        logo50 = getChildText("logo50", elem);
        birthday = getChildText("birthday", elem);
        bodyform = getChildText("bodyform", elem);
        blood = getChildText("blood", elem);
        marriage = getChildText("marriage", elem);
        trainwith = getChildText("trainwith", elem);
        interest = getChildText("interest", elem);
        favbook = getChildText("favbook", elem);
        favmovie = getChildText("favmovie", elem);
        favtv = getChildText("favtv", elem);
        idol = getChildText("idol", elem);
        motto = getChildText("motto", elem);
        wishlist = getChildText("wishlist", elem);
        intro = getChildText("intro", elem);
        //education = getChildText("education", elem);
//        schooltype = getChildText("schooltype", elem);
//        school = getChildText("school", elem);
//        strClass = getChildText("strClass", elem);
//        year = getChildText("year", elem);
//        career = getChildText("career", elem);
//        company = getChildText("company", elem);
//        dept = getChildText("dept", elem);
//        beginyear = getChildText("beginyear", elem);
//        endyear = getChildText("endyear", elem);
//        endmonth = getChildText("endmonth", elem);
//        beginmonth = getChildText("beginmonth", elem);
        
        isStar = getChildInt("isStar", elem);
        
        gender = getChildInt("gender", elem);
    }

    /**
     * Returns the uid of the user
     *
     * @return the uid of the user
     */
    public long getUid() {
        return uid;
    }

    /**
     * Returns the name of the user
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the hometown of the user
     *
     * @return the hometown of the user
     */
    public String getHometown() {
        return hometown;
    }

    /**
     * Returns the city of the user
     *
     * @return the city of the user
     */
    public String getCity() {
        return city;
    }

    public static List<User> constructUser(Response res) throws KxException {

        JSONObject json = res.asJSONObject();
        try {
            //			int next_cursor = json.getInt("next_cursor");
            //			int previous_cursor = json.getInt("previous_cursor");



            JSONArray list = json.getJSONArray("users");
            int size = list.length();
            List<User> users = new ArrayList<User>(size);
            for (int i = 0; i < size; i++) {
                users.add(new User(list.getJSONObject(i)));
            }
            return users;


        } catch (JSONException je) {
            throw new KxException(je);
        }

    }

    public static List<User> constructUsers(Response res, KxSDK kxSDK) throws KxException {
        Document doc = res.asDocument();
        if (isRootNodeNilClasses(doc)) {
            return new ArrayList<User>(0);
        } else {
            try {
                ensureRootNodeNameIs("users", doc);
//                NodeList list = doc.getDocumentElement().getElementsByTagName(
//                        "user");
//                int size = list.getLength();
//                List<User> users = new ArrayList<User>(size);
//                for (int i = 0; i < size; i++) {
//                    users.add(new User(res, (Element) list.item(i), kxSDK));
//                }

                //去除掉嵌套的bug
                NodeList list = doc.getDocumentElement().getChildNodes();
                List<User> users = new ArrayList<User>(list.getLength());
                Node node;
                for (int i = 0; i < list.getLength(); i++) {
                    node = list.item(i);
                    if (node.getNodeName().equals("user")) {
                        users.add(new User(res, (Element) node, kxSDK));
                    }
                }

                return users;
            } catch (KxException te) {
                if (isRootNodeNilClasses(doc)) {
                    return new ArrayList<User>(0);
                } else {
                    throw te;
                }
            }
        }
    }

    public static UserWapper constructWapperUsers(Response res, KxSDK kxSDK) throws KxException {
        Document doc = res.asDocument();
        if (isRootNodeNilClasses(doc)) {
            return new UserWapper(new ArrayList<User>(0), 0, 0);
        } else {
            try {
                ensureRootNodeNameIs("users_list", doc);
                Element root = doc.getDocumentElement();
                NodeList user = root.getElementsByTagName("users");
                int length = user.getLength();
                if (length == 0) {
                    return new UserWapper(new ArrayList<User>(0), 0, 0);
                }
                // 
                Element listsRoot = (Element) user.item(0);
                NodeList list = listsRoot.getChildNodes();
                List<User> users = new ArrayList<User>(list.getLength());
                Node node;
                for (int i = 0; i < list.getLength(); i++) {
                    node = list.item(i);
                    if (node.getNodeName().equals("user")) {
                        users.add(new User(res, (Element) node, kxSDK));
                    }
                }
                //
                long previousCursor = getChildLong("previous_curosr", root);
                long nextCursor = getChildLong("next_curosr", root);
                if (nextCursor == -1) { // 兼容不同标签名称
                    nextCursor = getChildLong("nextCurosr", root);
                }
                return new UserWapper(users, previousCursor, nextCursor);
            } catch (KxException te) {
                if (isRootNodeNilClasses(doc)) {
                    return new UserWapper(new ArrayList<User>(0), 0, 0);
                } else {
                    throw te;
                }
            }
        }
    }

    public static List<User> constructUsers(Response res) throws KxException {
        try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            List<User> users = new ArrayList<User>(size);
            for (int i = 0; i < size; i++) {
                users.add(new User(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new KxException(jsone);
        } catch (KxException te) {
            throw te;
        }
    }

    /**
     * 
     * @param res
     * @return
     * @throws KxException
     */
    public static UserWapper constructWapperUsers(Response res) throws KxException {
        JSONObject jsonUsers = res.asJSONObject(); //asJSONArray();
        try {
            JSONArray user = jsonUsers.getJSONArray("users");
            int size = user.length();
            List<User> users = new ArrayList<User>(size);
            for (int i = 0; i < size; i++) {
                users.add(new User(user.getJSONObject(i)));
            }
            long previousCursor = jsonUsers.getLong("previous_curosr");
            long nextCursor = jsonUsers.getLong("next_cursor");
            if (nextCursor == -1) { // 兼容不同标签名称
                nextCursor = jsonUsers.getLong("nextCursor");
            }
            return new UserWapper(users, previousCursor, nextCursor);
        } catch (JSONException jsone) {
            throw new KxException(jsone);
        }
    }

    /**
     * @param res 
     * @return 
     * @throws KxException
     */
    public static List<User> constructResult(Response res) throws KxException {
        JSONArray list = res.asJSONArray();
        try {
            int size = list.length();
            List<User> users = new ArrayList<User>(size);
            for (int i = 0; i < size; i++) {
                users.add(new User(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException e) {
        }
        return null;
    }

    public String getMotto() {
        return motto;
    }

    public int getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBlood() {
        return blood;
    }

    public String getBodyform() {
        return bodyform;
    }

    public List<Career> getCareer() {
        return career;
    }
    
    public List<Education> getEducation() {
        return education;
    }

    public String getFavbook() {
        return favbook;
    }

    public String getFavmovie() {
        return favmovie;
    }

    public String getFavtv() {
        return favtv;
    }

    public String getIdol() {
        return idol;
    }

    public String getInterest() {
        return interest;
    }

    public String getIntro() {
        return intro;
    }

    public int getIsStar() {
        return isStar;
    }

    public String getLogo120() {
        return logo120;
    }

    public String getLogo50() {
        return logo50;
    }

    public String getMarriage() {
        return marriage;
    }

//    public String getSchool() {
//        return school;
//    }
//
//    public String getSchooltype() {
//        return schooltype;
//    }
//
//    public String getStrClass() {
//        return strClass;
//    }
    public String getTrainwith() {
        return trainwith;
    }

    public String getWishlist() {
        return wishlist;
    }

//    public String getYear() {
//        return year;
//    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (uid ^ (uid >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (uid != other.uid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        int len = education.size();

        StringBuilder educations = new StringBuilder("");
        for (int i = 0; i < len; i++) {
            educations.append(education.get(i).toString());

            if (i < len - 1) {
                educations.append(",");
            }
        }
        
        len = career.size();
        StringBuilder careers = new StringBuilder("");
        for (int i = 0; i < len; i++) {
            careers.append(career.get(i).toString());

            if (i < len - 1) {
                careers.append(",");
            }
        }

        return "User{"
                + "KxSDK=" + kxSDK
                + ", uid=" + uid
                + ", name='" + name + '\''
                + ", logo120='" + logo120 + '\''
                + ", hometown='" + hometown + '\''
                + ", city='" + city + '\''
                + ", logo50='" + logo50 + '\''
                + ", birthday='" + birthday + '\''
                + ", bodyform ='" + bodyform + '\''
                + ", gender ='" + gender + '\''
                + ", blood='" + blood + '\''
                + ", marriage=" + marriage
                + ", trainwith=" + trainwith
                + ", interest=" + interest
                + ", favbook=" + favbook
                + ", favmovie='" + favmovie + '\''
                + ", favtv='" + favtv + '\''
                + ", idol=" + idol
                + ", motto=" + motto
                + ", wishlist=" + wishlist
                + ", intro=" + intro
                + ", educations[" + educations.toString()
                + "]"
                 //                ", schooltype='" + schooltype + '\'' +
                //                ", school='" + school + '\'' +
                //                ", strClass='" + strClass + '\'' +
                //                ", year='" + year + '\'' +
                 + ", careers[" + careers.toString()
                 + "]"
//                + ", company=" + company
//                + ", dept=" + dept
//                + ", beginyear=" + beginyear
//                + ", beginmonth=" + beginmonth
//                + ", endyear='" + endyear + '\''
//                + ", endmonth='" + endmonth + '\''
                + ", isStar='" + isStar + '\''
                + '}';
    }

    class Education {

        private String schooltype;
        private String school;
        private String strClass;
        private String year;

        public Education(JSONObject json) throws KxException {
            init(json);
        }

        private void init(JSONObject json) throws KxException {
            if (json != null) {
                try {
                    schooltype = json.getString("schooltype");
                    school = json.getString("school");
                    strClass = json.getString("strClass");
                    year = json.getString("year");
                } catch (JSONException jsone) {
                    throw new KxException(jsone.getMessage() + ":" + json.toString(), jsone);
                }
            }
        }

        public String getSchool() {
            return school;
        }

        public String getSchooltype() {
            return schooltype;
        }

        public String getStrClass() {
            return strClass;
        }

        public String getYear() {
            return year;
        }

        public String toString() {
            return "Education{"
                    + "schooltype='" + schooltype + '\''
                    + ", school='" + school + '\''
                    + ", strClass='" + strClass + '\''
                    + ", year='" + year + '\''
                    + '}';
        }
    }
    
    class Career {

        private String company;
        private String dept;
        private String beginyear;
        private String beginmonth;
        private String endyear;
        private String endmonth;

        public Career(JSONObject json) throws KxException {
            init(json);
        }

        private void init(JSONObject json) throws KxException {
            if (json != null) {
                try {
                    company = json.getString("company");
                    dept = json.getString("dept");
                    beginyear = json.getString("beginyear");
                    beginmonth = json.getString("beginmonth");
                    endyear = json.getString("endyear");
                    endmonth = json.getString("endmonth");
                } catch (JSONException jsone) {
                    throw new KxException(jsone.getMessage() + ":" + json.toString(), jsone);
                }
            }
        }

        public String getBeginmonth() {
            return beginmonth;
        }

        public String getBeginyear() {
            return beginyear;
        }

        public String getCompany() {
            return company;
        }

        public String getDept() {
            return dept;
        }

        public String getEndmonth() {
            return endmonth;
        }

        public String getEndyear() {
            return endyear;
        }

        public String toString() {
            return "Career{"
                    + "company='" + company + '\''
                    + ", dept='" + dept + '\''
                    + ", beginyear='" + beginyear + '\''
                    + ", beginmonth='" + beginmonth + '\''
                    + ", endyear='" + endyear + '\''
                    + ", endmonth='" + endmonth + '\''
                    + '}';
        }
    }
}