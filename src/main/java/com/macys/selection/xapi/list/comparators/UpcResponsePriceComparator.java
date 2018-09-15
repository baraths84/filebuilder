package com.macys.selection.xapi.list.comparators;

import com.macys.selection.xapi.list.client.response.fcc.UpcResponse;

import java.util.Comparator;

public class UpcResponsePriceComparator implements Comparator<UpcResponse> {
    @Override
    public int compare(UpcResponse u1, UpcResponse u2) {
        Double unitPrice1 = null;
        Double unitPrice2 = null;
        int result;
        if (u1.getPrice() != null) {
            unitPrice1 = u1.getPrice().getRetailPrice();
        }
        if (u2.getPrice() != null) {
            unitPrice2 = u2.getPrice().getRetailPrice();
        }
        if (unitPrice1 == null && unitPrice2 == null) {
            result = 0;
        } else if (unitPrice1 == null) {
            result = -1;
        } else if (unitPrice2 == null) {
            result = 1;
        } else {
            result = unitPrice1.compareTo(unitPrice2);
        }
        return result;
    }
}
