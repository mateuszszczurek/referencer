package com.testout.entities.entities

import groovy.transform.Immutable

@Immutable
class Entities {

    Map entities;

    Entity getEntity(String entityName) {
        return new Entity(entityName, entities[entityName])
    }

    Entities resolveEntities(String entityName) {

        def entities = []

        follow(entityName, entities)

        entities.inject(new EntitiesBuilder()) {
            result, entName -> result.addEntity(getEntity(entName))
        }.build()
    }

    private def follow(String entityName, List<String> knownEntities) {
        Entity entity = getEntity(entityName)

        if (knownEntities.contains(entityName)) {
            return;
        }

        knownEntities.add(entityName)

        if (!entity.hasReferences()) {
            return;
        }

        def references = entity.getReferences()

        references.each {
            follow(it, knownEntities)
        }

    }

}
