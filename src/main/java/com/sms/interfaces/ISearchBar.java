package com.sms.interfaces;

import java.util.ArrayList;
import java.util.function.Function;

import javax.lang.model.type.NullType;

public interface ISearchBar {
    public void setChangeUrl(Function<String, NullType> changeUrl);
    public void setDropDownItems(ArrayList<ArrayList> dropDownItems);
}
