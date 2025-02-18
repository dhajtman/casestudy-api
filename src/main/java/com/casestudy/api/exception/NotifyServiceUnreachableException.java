package com.casestudy.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;

@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
public class NotifyServiceUnreachableException extends HttpServerErrorException {

  public NotifyServiceUnreachableException(HttpStatusCode httpStatusCode) {
      super(httpStatusCode);
  }
}
