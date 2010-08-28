package com.openske.model.networking;

import com.openske.model.Element;

public class Host extends Element {

    protected String address = "";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}