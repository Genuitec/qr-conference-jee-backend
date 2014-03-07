package com.genuitec.qfconf.backend.model;

import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TagsAdapter extends XmlAdapter<String, Set<String>> {

	@Override
	public String marshal(Set<String> tags) throws Exception {
		StringBuffer result = new StringBuffer();
		for (String next : new TreeSet<String>(tags)) {
			if (result.length() > 0)
				result.append(',');
			result.append(next);
		}
		return result.toString();
	}

	@Override
	public Set<String> unmarshal(String tags) throws Exception {
		Set<String> result = new TreeSet<String>();
		for (String next : tags.split(",")) {
			next = next.trim();
			if (next.length() > 0)
				result.add(next);
		}
		return result;
	}
}