package com.lock.lib.api.site;

/**
 * Created by lixufeng on 16/6/13.
 * Class desc
 */
public class House {
    public Address address;
    public String doorplate; // 门牌号
    public String area;
    public int type; // 新房，旧房
    public Layout layout;// 户型
    public long measuerDate;
    public Source source;
    public Owner owner;

    public final static class Address {
        public String community;
        public String title;
        public String specialAddress;
        public double longitude;
        public double latitude;
    }

    public final static class Layout {//户型
        public int id;
        public String name;
    }

    public final static class Source {
        public int id;
        public String name;
    }

    public final static class Owner {
        public String id;
        public String name;
        public String phoneNum;
    }

}
