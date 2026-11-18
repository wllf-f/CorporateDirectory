package com.wei.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchTools {

    public List<Map<String, Object>> searchTools(List<Map<String, Object>> list,String key1,String key2,String key3,String key4){
        List<Map<String, Object>> newList = new ArrayList<>();
        Map<Integer,String> checkMap = new HashMap<>();

        if(key1!=null){
            checkMap.put(1,key1);
        }
        if(key2!=null){
            checkMap.put(2,key2);
        }
        if (key3!=null){
            checkMap.put(3,key3);
        }
        if (key4!=null){
            checkMap.put(4,key4);
        }
        int listLength =checkMap.size();
        if(listLength==0){
            newList = list;
        }else{
            for(Map<String,Object> map:list){
                boolean flag = true;
                for(int key:checkMap.keySet()){
                    if(key==1){
                        String value = checkMap.get(key);
                        String name = (String) map.get("emp_name");
                        if(!name.contains(value)){
                            flag = false;
                        }
                    }else if(key==2){
                        String value = checkMap.get(key);
                        String phone = (String) map.get("emp_phone");
                        if(!phone.contains(value)){
                            flag = false;
                        }
                    }else if(key==3){
                        String value = checkMap.get(key);
                        String email = (String) map.get("emp_email");
                        if(!email.contains(value)){
                            flag = false;
                        }
                    }else{
                        String value = checkMap.get(key);
                        String deptName = (String) map.get("dept_name");
                        if (!value.equals(deptName)){
                            flag = false;
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
}
