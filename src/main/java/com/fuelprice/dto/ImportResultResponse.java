package com.fuelprice.dto;

public record ImportResultResponse(
    int stationsRead,
    int pricesRead,
    int savedOrUpdated
) {}
