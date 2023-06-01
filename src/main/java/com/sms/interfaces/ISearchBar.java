package com.sms.interfaces;

import java.util.ArrayList;
import java.util.function.Function;

import javax.lang.model.type.NullType;

// interface for search bars which must provide a way to change url
//  and drop down items
public interface ISearchBar {
    public void setChangeUrl(Function<String, NullType> changeUrl);
    public void setDropDownItems(ArrayList<ArrayList> dropDownItems);
}
