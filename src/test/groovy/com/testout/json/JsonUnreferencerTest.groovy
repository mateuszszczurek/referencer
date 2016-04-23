package com.testout.json

import groovy.json.JsonSlurper
import spock.lang.Specification

import static com.testout.json.JsonUnreferencer.jsonUnreferencer

class JsonUnreferencerTest extends Specification {

    def simpleJson = '''
    {
        "entity1" :
            {
                "field" : "value",
                "field2" : "value"
            },
        "entiy2" :
            {
                "field" : "value",
                "other" : "otherValue"
            }
    }
    '''


    def "should be able to resolve single entity, from json"() {
        given:
        def expected = ''' {
            "entity1" :
                {
                    "field" : "value",
                    "field2" : "value"
                }
        }'''

        def sut = jsonUnreferencer(simpleJson)

        when:
        def result = sut.unroll("entity1")

        then:
        compareJsons(result, expected)
    }

    def jsonWithReferences = '''
    {
        "entity1" :
            {
                "field" : "value",
                "field2" : "@entity2"
            },
        "entity2" :
            {
                "field" : "value",
                "other" : "otherValue"
            }
    }

    '''

    def "given a json with references, should be able to resolve entity with reference"() {
        given:
        def sut = jsonUnreferencer(jsonWithReferences)

        when:
        def result = sut.unroll("entity1")

        then:
        compareJsons result, jsonWithReferences
    }

    def void compareJsons(def firstJson, def secondJson) {
        def first = new JsonSlurper().parseText(firstJson)
        def second = new JsonSlurper().parseText(secondJson)
        assert first == second
    }


}