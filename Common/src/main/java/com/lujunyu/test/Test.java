package com.lujunyu.test;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<?> list = new ArrayList<>();
		List<? extends Object> l = new ArrayList<>();
	}
}