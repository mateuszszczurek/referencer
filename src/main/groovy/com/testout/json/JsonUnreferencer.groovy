package com.testout.json

import com.testout.entities.entities.Entities
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class JsonUnreferencer {

    private final json

    static def jsonUnreferencer(String json) {
        new JsonUnreferencer(json)
    }

    JsonUnreferencer(json) {
        this.json = json
    }

    String unroll(String entityName) {
        def entities = new Entities(new JsonSlurper().parseText(json));

        def resolvedEntities = entities.resolveEntities(entityName)

        JsonOutput.toJson(resolvedEntities.getEntities())
    }
}
