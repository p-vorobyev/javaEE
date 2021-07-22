package ru.voroby.interceptors;

import lombok.extern.slf4j.Slf4j;
import ru.voroby.entity.User;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
@UserAudit(action = UserAction.UNUSED)
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class UserActionInterceptor {

    @AroundInvoke
    public Object methodInvoke(InvocationContext ctx) throws Exception {
        Method method = ctx.getMethod();
        UserAudit annotation = method.getAnnotation(UserAudit.class);
        switch (annotation.action()) {
            case GET -> getAudit(ctx);
            case SAVED -> saveAudit(ctx);
            case GET_ALL -> log.info("Get all users method invoked.");
        }

        return ctx.proceed();
    }

    private void saveAudit(InvocationContext ctx) {
        Object[] params = ctx.getParameters();
        try {
            User user = (User) params[0];
            Optional.ofNullable(user.getId())
                    .map(id -> {
                        log.info("Update user method invoked: [id: {}]", id);
                        return null;
                    }).orElseGet(() -> {
                        log.info("Save user method invoked.");
                        return null;
            });
        } catch (ClassCastException e) {
            log.warn(e.getMessage());
        }
    }

    private void getAudit(InvocationContext ctx) {
        Object[] params = ctx.getParameters();
        try {
            Integer id = (Integer) params[0];
            log.info("Get user method invoked: [id: {}]", id);
        } catch (ClassCastException e) {
            log.warn(e.getMessage());
        }
    }

}
