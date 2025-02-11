package com.mjc.school.service.dto;

import java.util.List;

public record ParametersDtoRequest(
        String newsTitle,
        String newsContent,
        String authorName,
        List<Integer> tagIds,
        List<String> tagNames) {
}
