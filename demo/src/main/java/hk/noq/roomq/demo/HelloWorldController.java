package hk.noq.roomq.demo;

import hk.noq.roomq.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HelloWorldController {

  private RoomQ roomq = new RoomQ("mm-dev", "a234sd56f7g8h9", "https://roomq-ticket-issuer-qdjywctibq-df.a.run.app/tickets", "https://roomq-dev.noqstatus.com/api/rooms", true);

  @GetMapping("/hello-world")
  @ResponseBody
  public String sayHello(HttpServletRequest request, HttpServletResponse response) {
    ValidationResult result = roomq.validate(request, response, null, null);
    if (result.needRedirect()) {
      response.setHeader("Location", result.getRedirectURL());
      response.setStatus(302);
      return null;
    }

    try {
      roomq.extend(request, response, 60);
    } catch (QueueStoppedException | HTTPRequestException | InvalidTokenException | NotServingException e) {
      e.printStackTrace();
    }

//    try {
//      roomq.deleteServing(request, response);
//    } catch (QueueStoppedException | HTTPRequestException | InvalidTokenException | NotServingException e) {
//      e.printStackTrace();
//    }

    try {
      long deadline = roomq.getServing(request);
      return String.valueOf(deadline);
    } catch (QueueStoppedException | HTTPRequestException | InvalidTokenException | NotServingException e) {
      e.printStackTrace();
    }
    return "";
  }

}
