package com.rsuniverse.jobify_user.models.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaEvent {
    private String status;
    private String type;
    private Object payload;
}
