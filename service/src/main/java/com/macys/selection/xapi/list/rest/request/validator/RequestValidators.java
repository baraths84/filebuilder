package com.macys.selection.xapi.list.rest.request.validator;

public interface RequestValidators<CustomerList, T> {  
  public T isValid(CustomerList customerList);
}
