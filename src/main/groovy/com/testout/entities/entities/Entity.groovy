package com.testout.entities.entities

import groovy.transform.Immutable

@Immutable
class Entity {

    String entityName
    Map fields

    static Closure<Boolean> isAReferenceCheck = {
        value -> value instanceof String && value.startsWith("@")
    }

    boolean hasReferences() {
        fields.values().any isAReferenceCheck
    }

    List<String> getReferences() {
        def references = fields.values().findAll isAReferenceCheck
        references*.drop(1)
    }

}
