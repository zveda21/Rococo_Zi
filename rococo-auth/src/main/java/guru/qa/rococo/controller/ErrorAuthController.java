package guru.qa.rococo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorAuthController implements ErrorController {

    private static final String ERROR_VIEW_NAME = "error";

    private final String rococoFrontUri;

    public ErrorAuthController(@Value("${rococo-front.base-uri}") String rococoFrontUri) {
        this.rococoFrontUri = rococoFrontUri;
    }

    @GetMapping("/error")
    public String error(HttpServletRequest request, HttpServletResponse response, Model model) {
        int status = response.getStatus();
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        model.addAttribute("status", status);
        model.addAttribute("frontUri", rococoFrontUri + "/main");
        model.addAttribute("error", throwable != null ? throwable.getMessage() : "Unknown error");
        return ERROR_VIEW_NAME;
    }
}
