package com.qpro.groceryapi;

import lombok.Data;

import java.util.List;

@Data
public class ListWrapper<T> {
    List<T> wrappedList;
}
