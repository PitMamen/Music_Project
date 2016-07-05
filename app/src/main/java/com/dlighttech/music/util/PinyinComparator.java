package com.dlighttech.music.util;

import java.util.Comparator;

import com.dlighttech.music.model.ContentItem;

/**
 * @Description:拼音的比较器
 * @author http://blog.csdn.net/finddreams
 */ 
public class PinyinComparator implements Comparator<ContentItem> {

	public int compare(ContentItem o1, ContentItem o2) {
		if (o1.getTitle().equals("@")
				|| o2.getTitle().equals("#")) {
			return -1;
		} else if (o1.getTitle().equals("#")
				|| o2.getTitle().equals("@")) {
			return 1;
		} else {
			return o1.getTitle().compareTo(o2.getTitle());
		}
	}

}
