package com.spring.batch.job.common.domain;

import lombok.*;

import javax.xml.bind.annotation.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "inventory")
@XmlType(propOrder = { "itemId", "unitItemId", "baseQty", "usableQty", "registerId", "modifierId" })
public class Inventory {

    @XmlElement
    private String itemId;

    @XmlElement
    private String unitItemId;

    @XmlElement
    private Integer baseQty;

    @XmlElement
    private Integer usableQty;

    @XmlElement
    private String registerId;

    @XmlElement
    private String modifierId;
}
