package com.beyt.dto;

import java.util.List;

/**
 * Created by tdilber at 7/17/2020
 */
public interface IFetchPartiallyProcessor<Entity> {
    void process(List<Entity> entityList);
}
