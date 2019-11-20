//package com.test.h2;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional(rollbackFor = Exception.class)
//public class H2Service {
//    @Autowired
//    LocationRep locationRep;
//    public List getList(){
//        return locationRep.getLocationdataByName("aaa");
//    }
//
//    public Locationdata save(Locationdata locationdata) throws Exception{
//        Locationdata list = locationRep.save(locationdata);
////        System.out.println(1/0);
//        return list;
//    }
//}
