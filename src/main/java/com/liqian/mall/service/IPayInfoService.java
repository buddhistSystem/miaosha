package com.liqian.mall.service;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;


public interface IPayInfoService {

    void add(HttpServletRequest request) throws UnsupportedEncodingException, ParseException;
}
