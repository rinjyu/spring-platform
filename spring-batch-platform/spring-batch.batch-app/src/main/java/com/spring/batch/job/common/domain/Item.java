package com.spring.batch.job.common.domain;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private String itemId;
    private String itemName;
    private String unitItemId;
    private String unitItemName;
    private String registerId;
    private String modifierId;
}
