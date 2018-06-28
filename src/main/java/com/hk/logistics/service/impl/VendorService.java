package com.hk.logistics.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class VendorService{

	public static volatile Map<String,String> vendorShortCodes=new HashMap<>();
	
}
