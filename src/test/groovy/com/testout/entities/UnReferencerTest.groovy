package com.testout.entities

import com.testout.entities.entities.Entities
import com.testout.entities.entities.Entity
import spock.lang.Specification
import spock.lang.Unroll

class UnReferencerTest extends Specification {

    def "given entities, should be able to filter one entity"() {
        given:

        def sut = new Entities(
                [
                        firstEntity : [key: 'somethingHere'],
                        secondEntity: [otherKey: 'somethingElse']
                ]
        )

        when:
        def result = sut.getEntity("firstEntity")

        then:
        result == new Entity("firstEntity", [key: 'somethingHere'])

    }

    @Unroll
    def "should be able to identify if entity contains references"(def value, def expectedResponse) {
        given:
        def sut = new Entity("entityName", [key: value])

        expect:
        sut.hasReferences() == expectedResponse

        where:
        value             || expectedResponse
        "@ItItAReference" || true
        "notAReference"   || false
    }

    def "should be able to get all the reference names"() {
        given:
        def sut = new Entity("entityName",
                [key1: "@reference1", key2: "@reference2", key3: "notAReference"])

        expect:
        sut.getReferences().containsAll(["reference1", "reference2"])
    }

    def "should be able to resolve entities with it's references"() {
        given:
        def sut = new Entities(
                [
                        firstEntity : [key: '@secondEntity'],
                        secondEntity: [otherKey: 'somethingElse']
                ]
        )

        when:
        def result = sut.resolveEntities("firstEntity")

        then:
        result == new Entities(
                [
                        firstEntity : [key: '@secondEntity'],
                        secondEntity: [otherKey: 'somethingElse']
                ]
        )
    }

    def "reference resolution is stable even there are circular dependencies"() {
        given:
        def sut = new Entities(
                [
                        firstEntity : [key: '@secondEntity'],
                        secondEntity: [otherKey: '@firstEntity']
                ]
        )

        when:
        def result = sut.resolveEntities("firstEntity")

        then:
        result == new Entities(
                [
                        firstEntity : [key: '@secondEntity'],
                        secondEntity: [otherKey: '@firstEntity']
                ]
        )
    }

}


