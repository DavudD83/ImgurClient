package davydov.dmytro.core_api

import com.fasterxml.jackson.databind.ObjectMapper


interface ObjectMapperProvider {
    fun objectsMapper(): ObjectMapper
}