package cn.telbox.web;

import cn.telbox.login.Login;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by George on 2016/7/14.
 */
@RestController
public class LoginController {
    final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final AtomicLong counter = new AtomicLong();

    @ApiOperation(value = "login", nickname = "login", notes = "user login end-point.", response = Login.class)
    @RequestMapping(method = RequestMethod.POST, path="/login", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginId", value = "User's loginId", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "User's password", required = true, dataType = "string", paramType = "form")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Login.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Login greeting(@ApiParam(name = "loginId", value = "user's login id.", required = true) @RequestParam(value="loginId") String loginId,
                          @ApiParam(name = "password", value = "user's password.", required = true) @RequestParam(value="password") String password) {

        logger.info("************************* Don't see my pwd!   loginId={}, password={}", loginId, password);
        return new Login(counter.incrementAndGet(), loginId, password);
    }
}
