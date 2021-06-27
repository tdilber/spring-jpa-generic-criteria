package com.beyt.resolver;

import com.beyt.dto.CriteriaFilter;
import com.beyt.dto.SearchQuery;
import com.beyt.dto.enums.Order;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.core.MethodParameter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class SearchQueryArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String SELECT_FIELD_START = "select";
    private static final String SELECT_AS_FIELD_START = "selectAs";
    private static final String ORDER_BY_FIELD_START = "orderBy";
    private static final String ORDER_BY_DIRECTION_FIELD_START = "orderByDirection";
    private static final String PAGE_FIELD_START = "page";
    private static final String PAGE_SIZE_FIELD_START = "pageSize";
    private static final String DISTINCT_FIELD_START = "distinct";
    private final CriteriaFilterArgumentResolver criteriaFilterArgumentResolver;

    public SearchQueryArgumentResolver(CriteriaFilterArgumentResolver criteriaFilterArgumentResolver) {
        this.criteriaFilterArgumentResolver = criteriaFilterArgumentResolver;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return SearchQuery.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        SearchQuery filter = new SearchQuery();

        String pageSizeText = webRequest.getParameter(PAGE_SIZE_FIELD_START);
        String pageText = webRequest.getParameter(PAGE_FIELD_START);
        String distinctText = webRequest.getParameter(DISTINCT_FIELD_START);

        if (Strings.isBlank(pageText)) {
            pageText = "0";
        }

        if (Objects.nonNull(pageSizeText)) {
            filter.setPageNumber(Integer.parseInt(pageText));
            filter.setPageSize(Integer.parseInt(pageSizeText));
        }

        if (Objects.nonNull(distinctText)) {
            filter.setDistinct(true);
        }

        CriteriaFilter criteriaFilter = (CriteriaFilter) criteriaFilterArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        filter.setWhere(criteriaFilter);

        for (int i = 0; ; i++) {
            String selectParam = SELECT_FIELD_START + i;
            String selectAsParam = SELECT_AS_FIELD_START + i;
            String select = webRequest.getParameter(selectParam);
            String selectAs = webRequest.getParameter(selectAsParam);
            if (select != null) {
                filter.getSelect().add(Pair.of(select, Objects.nonNull(selectAs) ? selectAs : select));
            } else {
                break;
            }
        }

        for (int i = 0; ; i++) {
            String orderByParam = ORDER_BY_FIELD_START + i;
            String orderByDirectionParam = ORDER_BY_DIRECTION_FIELD_START + i;
            String orderBy = webRequest.getParameter(orderByParam);
            String orderByDirection = webRequest.getParameter(orderByDirectionParam);
            if (orderBy != null && orderByDirection != null) {
                filter.getOrderBy().add(Pair.of(orderBy, Order.of(orderByDirection)));
            } else {
                break;
            }
        }
        return filter;
    }
}
