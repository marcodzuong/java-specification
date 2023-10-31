package com.marco.specification.specification;


import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
/**
 * @author MarcoDuong
 */
public class SearchFilter {
    public enum Operator {
        EQ, NE, LIKE, LIKEL, LIKER, GT, LT, GTE, LTE, IN, NOTIN, ISNULL, ISNOTNULL, BETWEEN
    }

    public enum Join {
        and, or
    }

    public Join join = Join.and;
    public String fieldName;
    public Object value;
    public Operator operator;

    public static SearchFilter build(String fieldName, Object value) {
        return new SearchFilter(fieldName, Operator.EQ, value);
    }

    public static SearchFilter build(String fieldName,Operator operator) {
        return new SearchFilter(fieldName,operator);
    }
    public static SearchFilter build(String fieldName, Operator operator, Object value) {
        if (ObjectUtils.isEmpty(value) || ObjectUtils.isEmpty(fieldName)) return null;
        return new SearchFilter(fieldName, operator, value);
    }


    public static SearchFilter build(String fieldName, Object value, Join join) {
        return new SearchFilter(fieldName, Operator.EQ, value, join);

    }
    public SearchFilter(String fieldName, Operator operator) {
        this.fieldName = fieldName;
        this.operator = operator;

    }
    public SearchFilter(String fieldName, Operator operator, Object value) {
        if (!ObjectUtils.isEmpty(value)) {
            this.fieldName = fieldName;
            this.value = value;
            this.operator = operator;
        }
    }

    public SearchFilter(String fieldName, Operator operator, Object value, Join join) {
        if (!ObjectUtils.isEmpty(value)) {
            this.fieldName = fieldName;
            this.value = value;
            this.operator = operator;
            this.join = join;
        }
    }


    public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = new HashMap<>();

        for (Map.Entry<String, Object> entry : searchParams.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();
			/*if (StringUtil.isBlank((String) value)) {
				continue;
			}*/
            String[] names = key.split( "_");
            if (names.length != 2) {
                throw new IllegalArgumentException(key + " is not a valid search filter name");
            }
            String filedName = names[1];
            Operator operator = Operator.valueOf(names[0]);

            SearchFilter filter = new SearchFilter(filedName, operator, value);
            filters.put(key, filter);
        }

        return filters;
    }

}
