package com.rsuniverse.jobify_user.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopic {

    USER_REGISTERED("user-registered"),
    USER_UPDATED("user-updated"),
    USER_DELETED("user-deleted");

    private final String name;

}
