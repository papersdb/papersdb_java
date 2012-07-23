package edu.ualberta.cs.papersdb.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    protected final static Logger logger = LoggerFactory
        .getLogger(IndexController.class);

    public IndexController() {
    }

    @RequestMapping({ "/", "/home" })
    public String index() {
        logger.debug("index");
        return "home";
    }

}
