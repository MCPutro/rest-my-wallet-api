package com.mywallet.api.documentation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;


@ApiIgnore
@RestController
public class DocUrl {

    @ApiOperation(value = "documentation", hidden = true)
    @GetMapping("/doc")
    public RedirectView redirect(){
        return new RedirectView("/swagger-ui.html");
    }
}
