package com.klever.bok.payload.response;

import com.klever.bok.exceptions.BadRequestException;
import com.klever.bok.utils.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;


    public static void validatePageNumberAndSize(int page, int pageSize) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (pageSize > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }

        if (pageSize < 0) {
            throw new BadRequestException("Page size must not be less than zero");
        }
    }
}
