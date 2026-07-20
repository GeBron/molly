package com.demo.molly.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 敏感数据掩码工具
 */
public class SensitiveDataUtil {

    private static final Set<String> DEFAULT_SENSITIVE_FIELDS = new HashSet<>(Arrays.asList(
            "password", "oldPassword", "newPassword", "confirmPassword",
            "token", "accessToken", "refreshToken",
            "secret", "secretKey", "authorization", "cookie",
            "phone", "mobile", "email", "idCard", "creditCard"
    ));

    private static final String MASK = "******";

    private SensitiveDataUtil() {
    }

    /**
     * 对 JSON 字符串中的敏感字段进行掩码
     */
    public static String maskJson(String json, ObjectMapper objectMapper, String... extraFields) {
        if (json == null || json.trim().isEmpty()) {
            return json;
        }
        try {
            JsonNode node = objectMapper.readTree(json);
            Set<String> fields = new HashSet<>(DEFAULT_SENSITIVE_FIELDS);
            fields.addAll(Arrays.asList(extraFields));
            maskNode(node, fields);
            return objectMapper.writeValueAsString(node);
        } catch (Exception e) {
            return json;
        }
    }

    private static void maskNode(JsonNode node, Set<String> fields) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            List<String> keys = new ArrayList<>();
            objectNode.fieldNames().forEachRemaining(keys::add);
            for (String key : keys) {
                JsonNode value = objectNode.get(key);
                if (fields.contains(key)) {
                    objectNode.put(key, MASK);
                } else {
                    maskNode(value, fields);
                }
            }
        } else if (node.isArray()) {
            for (JsonNode element : node) {
                maskNode(element, fields);
            }
        }
    }
}
