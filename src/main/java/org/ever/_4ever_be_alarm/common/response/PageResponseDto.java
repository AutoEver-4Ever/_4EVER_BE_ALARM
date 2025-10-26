package org.ever._4ever_be_alarm.common.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PageResponseDto<T> {

    private List<T> items;
    private PageDto page;
}