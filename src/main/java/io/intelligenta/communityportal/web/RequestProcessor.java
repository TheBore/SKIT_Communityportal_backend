package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.BaseEntity;
import io.intelligenta.communityportal.web.specifications.BaseSpecification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class RequestProcessor {

    public static Sort sorting(HttpServletRequest request) {
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.startsWith("sorting")) {
                String field = key.substring(key.indexOf("[") + 1,
                        key.indexOf("]"));
                String direction = request.getParameter(key);
                Sort sort = new Sort(Sort.Direction.fromString(direction),
                        field);
                return sort;
            }
        }
        return Sort.by(Sort.Direction.DESC, "id");
    }

    public static Sort sortingByName(HttpServletRequest request) {
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.startsWith("sorting")) {
                String field = key.substring(key.indexOf("[") + 1,
                        key.indexOf("]"));
                String direction = request.getParameter(key);
                Sort sort = new Sort(Sort.Direction.fromString(direction),
                        field);
                return sort;
            }
        }
        return Sort.by(Sort.Direction.DESC, "name");
    }

    public static Sort sortingByCode(HttpServletRequest request) {
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.startsWith("sorting")) {
                String field = key.substring(key.indexOf("[") + 1,
                        key.indexOf("]"));
                String direction = request.getParameter(key);
                Sort sort = new Sort(Sort.Direction.fromString(direction),
                        field);
                return sort;
            }
        }
        return Sort.by(Sort.Direction.DESC, "code");
    }

    public static <T extends BaseEntity> Specification<T> getSpecifications(
            HttpServletRequest request) {
        return getSpecifications(request, null, null);
    }

    public static <T extends BaseEntity> Specification<T> getSpecifications(
            HttpServletRequest request, BaseSpecification<T> specifications) {
        return getSpecifications(request, specifications, null);
    }


    public static <T extends BaseEntity> Specification<T> getSpecifications(
            JSONObject filter, BaseSpecification<T> specifications, Map<String, Specification<T>> defaults) {
        Iterator<String> keys = filter.keys();
        Specification<T> result = null;
        HashSet<String> processedFields = new HashSet<>();
        while (keys.hasNext()) {
            String field = keys.next().toString();
            JSONObject valueObject;
            String value = null;
            try {
                valueObject = filter.getJSONObject(field);
                value = valueObject.getString("id");
            } catch (JSONException e) {
                try {
                    value = filter.getString(field);
                } catch (JSONException e1) {
                }
            }
            if (value == null || value.equals("null"))
                continue;
            result = Specification.where(result).and(
                    specifications.getSpecification(field, value));
        }
        if (defaults != null) {
            for (String key : defaults.keySet()) {
                if (!processedFields.contains(key)) {
                    result = Specification.where(result).and(defaults.get(key));
                }
            }
        }
        return result;
    }

    public static String getFilter(
            JSONObject filter, String requestedField) {
        Iterator<String> keys = filter.keys();
        String result = "";

        while (keys.hasNext()) {
            String field = keys.next().toString();
            if (!field.equals(requestedField))
                continue;
            JSONObject valueObject;
            String value = null;
            try {
                valueObject = filter.getJSONObject(field);
                value = valueObject.getString("id");
                result = value;
                break;
            } catch (JSONException e) {
                try {
                    value = filter.getString(field);
                    result = value;
                    break;
                } catch (JSONException e1) {
                }
            }

        }
        if (result == null || result.equals("null"))
            return null;
        return result;
    }

    public static <T extends BaseEntity> Specification<T> getSpecifications(
            HttpServletRequest request, BaseSpecification<T> specifications, Map<String, Specification<T>> defaults) {
        Enumeration<String> keys = request.getParameterNames();
        Specification<T> result = null;
        HashSet<String> processedFields = new HashSet<>();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.startsWith("filter")) {
                String field = key.substring(key.indexOf("[") + 1,
                        key.indexOf("]"));
                processedFields.add(field);
                String value = request.getParameter(key);
                try {
                    JSONObject obj = new JSONObject(value);
                    value = obj.getString("id");
                } catch (JSONException e) {
                }
                result = Specifications.where(result).and(
                        specifications.getSpecification(field, value));
            }
        }
        if (defaults != null) {
            for (String key : defaults.keySet()) {
                if (!processedFields.contains(key)) {
                    result = Specifications.where(result).and(defaults.get(key));
                }
            }
        }
        return result;
    }


    public static String getFilter(HttpServletRequest request, String field) {
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.startsWith("filter")) {
                String f = key
                        .substring(key.indexOf("[") + 1, key.indexOf("]"));
                if (f.equals(field)) {
                    String value = request.getParameter(key);
                    try {
                        JSONObject obj = new JSONObject(value);
                        value = obj.getString("id");
                    } catch (JSONException e) {
                    }
                    return value;
                }
            }
        }
        return null;
    }

    public static Sort sorting(JSONObject json) {
        Iterator<String> keys = json.keys();
        String field = keys.next().toString();
        String direction = null;
        try {
            direction = json.getString(field);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Sort sort = new Sort("id");
        if (direction != null) {
            sort = new Sort(Sort.Direction.fromString(direction), field);
        }
        return sort;
    }


    public static <T extends BaseEntity> Specification<T> getSpecificationsWithArray(
            JSONObject filter, BaseSpecification<T> specifications) {
        Iterator<String> keys = filter.keys();
        Map<String, List<String>> mapOfValues = new HashMap<String, List<String>>();
        Specification<T> result = null;
        while (keys.hasNext()) {
            String key = keys.next().toString();
            JSONObject valueObject;
            String value = null;
            try {
                value = filter.getString(key);
                if (value.indexOf("[") != -1) {
                    JSONArray array = filter.getJSONArray(key);
                    if (array.length() == 0)
                        continue;
                    List<String> ids = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        ids.add(obj.getString("id"));
                    }
                    mapOfValues.put(key, ids);
                    continue;
                }
                if (value.indexOf("{") != -1) {
                    valueObject = new JSONObject(value);
                    value = valueObject.getString("id");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (value == null || value.equals("null") || value.equals("")) {
                continue;
            }

            if (mapOfValues.containsKey(key)) {
                mapOfValues.get(key).add(value);
            } else {
                List<String> values = new ArrayList<>();
                values.add(value);
                mapOfValues.put(key, values);
            }
        }
        for (Map.Entry<String, List<String>> entry : mapOfValues.entrySet()) {
            result = Specifications.where(result).and(
                    specifications.getSpecification(entry.getKey(), entry.getValue().toString()));
        }
        return result;
    }
}
