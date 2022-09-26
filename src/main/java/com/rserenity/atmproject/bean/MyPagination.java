package com.rserenity.atmproject.bean;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MyPagination<T> {

    int elementCount;
    int pageSize;
    List<T> elements;
    int pageCount;

    public MyPagination() {}


    public MyPagination(int pageSize, List<T> elements) {
        this.elementCount = elements.size();
        this.elements = elements;
        this.pageSize = pageSize;
        this.pageCount = (int)Math.ceil(elementCount/(double)pageSize);
    }

    public int getPageCount(){
        return pageSize;
    }

    public List<T> getPageElements(int pageIndex){
        List<T> returnList = new ArrayList<>();
        int max_index;
        if(pageIndex == pageCount){
            max_index = elementCount;
        }else{
            max_index=4*pageIndex;
        }
        for(int i =(4*pageIndex-4); i<max_index; i++){
            returnList.add(elements.get(i));
        }
        return returnList;
    }

    public int getPageCount(int pageSize, List<T> elements){
        return (int)Math.ceil(elements.size()/(double)pageSize);
    }

    public List<T> getPageElementsIns(int pageSize, List<T> elements,int pageIndex){
        int pageCountIns = (int)Math.ceil(elements.size()/(double)pageSize);
        List<T> returnList = new ArrayList<>();
        int max_index;
        if(pageIndex == pageCountIns){
            max_index = elements.size();
        }else{
            max_index=4*pageIndex;
        }
        for(int i =(4*pageIndex-4); i<max_index; i++){
            returnList.add(elements.get(i));
        }
        return returnList;

    }
}
