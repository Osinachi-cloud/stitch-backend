package com.stitch.gateway.config.graphql;


import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {
    @Bean
    public GraphQLScalarType bigDecimalScalar() {
        return ExtendedScalars.GraphQLBigDecimal;
    }

//    @Bean
//    public graphql.schema.GraphQLScalarType extendedscalarlong() {
//        return ExtendedScalars.GraphQLBigDecimal;
//    }

//    @Bean
//    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
//        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.GraphQLBigDecimal);
//    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(){
        return  wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.GraphQLBigDecimal)
                .scalar(ExtendedScalars.GraphQLLong);
    }
}