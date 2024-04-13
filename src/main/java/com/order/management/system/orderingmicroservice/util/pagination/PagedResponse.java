package com.order.management.system.orderingmicroservice.util.pagination;

import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.Dto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PagedResponse<T extends Dto> {

    private Pagination page;

    private List<T> data;
}
