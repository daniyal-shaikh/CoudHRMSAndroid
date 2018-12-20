package com.cloudhr.attendancepoc.core.model;

public  class SwipeDetailEntity {
        /**
         * id : 9
         * lat : 19.192565
         * long : 72.839995
         * address : Malad West, Mumbai, MH, India
         * userid : 62
         * type : OUT
         * createddate : 2018-12-10 12:08:25
         */

        private int id;
        private String lat;

//        private String lon;
        private String address;
        private String userid;
        private String type;
        private String createddate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

//        public String getLong() {
//            return lon;
//        }
//
//        public void setLong(String lon) {
//            this.lon = lon;
//        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateddate() {
            return createddate;
        }

        public void setCreateddate(String createddate) {
            this.createddate = createddate;
        }
    }