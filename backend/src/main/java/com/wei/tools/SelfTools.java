package com.wei.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SelfTools {

    public List<Map<String,Object>> searchCustomer(List<Map<String,Object>> list,Map<String,Object> searchMap){
        List<Map<String,Object>> newList = new ArrayList<>();
        if (searchMap.isEmpty()){
            newList=list;
        }else{
            for(Map<String,Object> map:list){
                boolean flag = true;
                for(String key:searchMap.keySet()){
                    if(map.get(key)!=null&& searchMap.get(key)!=null){
                        String value = (String) map.get(key);
                        String checkValue = (String) searchMap.get(key);
                        if(!value.contains(checkValue)){
                            flag=false;
                        }
                    }
                }
                if(flag){
                    newList.add(map);
                }
            }
        }
        return newList;

    }
    public HashSet<String> getGroup(List<Map<String,Object>> list){
        List<String> newList = new ArrayList<>();
        for(Map<String,Object> item:list){
            newList.add((String) item.get("personalGroup"));
        }
        return new HashSet<>(newList);
    }


}
