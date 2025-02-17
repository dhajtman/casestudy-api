package com.casestudy.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
public class ServiceBTimeoutException extends HttpClientErrorException {

  public ServiceBTimeoutException(HttpStatusCode httpStatusCode) {
      super(httpStatusCode);
  }
}
