package com.stitch.gateway.exception;

//import com.stitch.payment.exception.TransVolumeException;
import com.stitch.user.exception.UserException;
import com.stitch.commons.exception.StitchException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        log.error(ex.getMessage(), ex);
        if (ex instanceof UserException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        } else if (ex instanceof AuthenticationException || ex instanceof AccessDeniedException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.UNAUTHORIZED)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        } else if (ex instanceof StitchException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.INTERNAL_ERROR)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }
//        else if (ex instanceof TransVolumeException) {
//            return GraphqlErrorBuilder.newError()
//                .errorType(ErrorType.BAD_REQUEST)
//                .message(ex.getMessage())
//                .path(env.getExecutionStepInfo().getPath())
//                .location(env.getField().getSourceLocation())
//                .build();
//        }
        else {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.INTERNAL_ERROR)
                    .message("Error processing your request. Please try again later or contact Support.")
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }
    }
}
