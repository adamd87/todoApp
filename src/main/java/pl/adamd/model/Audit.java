package pl.adamd.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Embeddable
class Audit {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;


    @PrePersist
    void prePersist(){
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preMerge(){
        updatedOn = LocalDateTime.now();
    }
}
