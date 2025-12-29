# Install

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```

Step 2. Add the dependency

```groovy
dependencies {
  implementation 'com.github.redso:noq-java-sdk:1.21.0'
}
```

# RoomQ Backend SDK - Java

The [RoomQ](https://www.noq.hk/en/roomq) Backend SDK is used for server-side integration to your server. It was developed with Java.

## High Level Logic

![The SDK Flow](https://raw.githubusercontent.com/redso/roomq.backend-sdk.nodejs/master/RoomQ-Backend-SDK-JS-high-level-logic-diagram.png)

1.  End user requests a page on your server
2.  The SDK verify if the request contain a valid ticket and in Serving state. If not, the SDK send him to the queue.
3.  End user obtain a ticket and wait in the queue until the ticket turns into Serving state.
4.  End user is redirected back to your website, now with a valid ticket
5.  The SDK verify if the request contain a valid ticket and in Serving state. End user stay in the requested page.
6.  The end user browses to a new page, and the SDK continue to check if the ticket is valid.

## How to integrate

### Prerequisite

To integrate with the SDK, you need to have the following information provided by RoomQ

1.  ROOM_ID
2.  ROOM_SECRET
3.  ROOMQ_TICKET_ISSUER
4.  ROOMQ_STATUS_API

### Major steps

To validate that the end user is allowed to access your site (has been through the queue) these steps are needed:

1.  Initialise RoomQ
2.  Determine if the current request page/path required to be protected by RoomQ
3.  Initialise Http Context Provider
4.  Validate the request
5.  If the end user should goes to the queue, set cache control
6.  Redirect user to queue

### Integration on specific path

It is recommended to integrate on the page/path which are selected to be provided. For the static files, e.g. images, css files, js files, ..., it is recommended to be skipped from the validation.
You can determine the requests type before pass it to the validation.

## Implementation Example

The following is an RoomQ integration example in java.

```java
package hk.noq.roomq.demo;

import hk.noq.roomq.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HelloWorldController {

  private RoomQ roomq = new RoomQ("clientID", "JWT Secret", "Ticket issuer", "Status Endpoint", false);

  @GetMapping("/")
  @ResponseBody
  public String sayHello(HttpServletRequest request, HttpServletResponse response) {
    ValidationResult result = roomq.validate(request, response, null, null);
    if (result.needRedirect()) {
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");
      response.setHeader("Pragma", "no-cache");
      response.setHeader("Expires", "Fri, 01 Jan 1990 00:00:00 GMT");
      response.setHeader("Location", result.getRedirectURL());
      response.setStatus(302);
      return null;
    }

    try {
      roomq.extend(request, response, 60);
    } catch (QueueStoppedException | HTTPRequestException | InvalidTokenException | NotServingException e) {
      e.printStackTrace();
    }

    try {
      roomq.deleteServing(request, response);
    } catch (QueueStoppedException | HTTPRequestException | InvalidTokenException | NotServingException e) {
      e.printStackTrace();
    }

    try {
      long deadline = roomq.getServing(request);
      return String.valueOf(deadline);
    } catch (QueueStoppedException | HTTPRequestException | InvalidTokenException | NotServingException e) {
      e.printStackTrace();
    }
    return "";
  }

}

```

### Ajax calls

RoomQ doesn't support validate ticket in Ajax calls yet.

### Browser / CDN cache

If your responses are cached on browser or CDN, the new requests will not process by RoomQ.
In general, for the page / path integrated with RoomQ, you are not likely to cache the responses on CDN or browser.

### Hash of URL

As hash of URL will not send to server, hash information will be lost.
