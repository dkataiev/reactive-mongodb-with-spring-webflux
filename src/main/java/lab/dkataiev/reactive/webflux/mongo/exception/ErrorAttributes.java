package lab.dkataiev.reactive.webflux.mongo.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorAttributes extends DefaultErrorAttributes {

    private static final String ERROR_CODE = "errorCode";
    private static final String ERROR_MESSAGE = "errorMessage";

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        return assembleError(request);
    }

    private Map<String, Object> assembleError(ServerRequest serverRequest) {
        Map<String, Object> errorAttributes = new HashMap<>();
        Throwable error = getError(serverRequest);
        if (error instanceof OptimisticLockingFailureException) {
            errorAttributes.put(ERROR_CODE, HttpStatus.BAD_REQUEST.value());
            errorAttributes.put(ERROR_MESSAGE, "Version mismatch");
        } else {
            errorAttributes.put(ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorAttributes.put(ERROR_MESSAGE, "Internal server error");
        }
        return errorAttributes;
    }

}
