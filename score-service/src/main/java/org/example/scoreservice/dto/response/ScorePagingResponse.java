package org.example.scoreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ScorePagingResponse<T> {

    private List<T> items;
    private int nextCursor;
    private boolean hasNext;
}
