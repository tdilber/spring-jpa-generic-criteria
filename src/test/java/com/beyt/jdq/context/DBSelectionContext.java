package com.beyt.jdq.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.Serializable;


@Component
@RequestScope
public class DBSelectionContext implements Serializable {
    public enum Database {
        READ, WRITE
    }

    @Getter
    @Setter
    private Database database = Database.WRITE;
}
