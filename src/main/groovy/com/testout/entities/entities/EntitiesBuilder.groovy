package com.testout.entities.entities

class EntitiesBuilder {

    List<Entity> entities = []

    def addEntity(Entity entity) {
        entities.add(entity)
        return this
    }

    def build() {
        def merged = entities.inject([:])
                {
                    result, entity ->
                        result[entity.entityName] = entity.fields
                        result
                }
        return new Entities(merged)
    }

}
